/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMacaoTravelPermitProcessor;
import com.xiahaimoyu.credentialkit.processor.TaiwanTravelPermitProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMacaoResidencePermitProcessor;
import com.xiahaimoyu.credentialkit.processor.TaiwanResidencePermitProcessor;
import com.xiahaimoyu.credentialkit.processor.ForeignerPermanentResidenceIdProcessor;
import com.xiahaimoyu.credentialkit.processor.MachineReadablePassportProcessor;
import com.xiahaimoyu.credentialkit.processor.UnifiedSocialCreditProcessor;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 证件工具类
 * <p>
 * 提供证件校验、解析、智能识别等功能。
 * 支持多种证件类型的注册、注销和自定义扩展。
 * </p>
 *
 * @author Howard.Li
 */
public final class CredentialKit {

    /**
     * 证件和处理器映射
     */
    private static final Map<CredentialType, CredentialProcessor<? extends CredentialInfo>> PROCESSORS = new ConcurrentHashMap<>();

    // 注册默认证件处理器
    static {
        register(DefaultCredentialType.MAINLAND_RESIDENT_ID, new MainlandResidentIdProcessor());
        register(DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT, new HkMacaoTravelPermitProcessor());
        register(DefaultCredentialType.TAIWAN_TRAVEL_PERMIT, new TaiwanTravelPermitProcessor());
        register(DefaultCredentialType.HK_MACAO_RESIDENCE_PERMIT, new HkMacaoResidencePermitProcessor());
        register(DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT, new TaiwanResidencePermitProcessor());
        register(DefaultCredentialType.FOREIGNER_PERMANENT_RESIDENCE_ID, new ForeignerPermanentResidenceIdProcessor());
        register(DefaultCredentialType.MACHINE_READABLE_PASSPORT, new MachineReadablePassportProcessor());
        register(DefaultCredentialType.UNIFIED_SOCIAL_CREDIT, new UnifiedSocialCreditProcessor());
    }

    /**
     * 构造器
     */
    private CredentialKit() {
    }

    /**
     * 注册处理器
     *
     * @param type      证件类型
     * @param processor 证件处理器
     */
    public static void register(final CredentialType type, final CredentialProcessor<? extends CredentialInfo> processor) {
        Objects.requireNonNull(type, "证件类型是空");
        Objects.requireNonNull(processor, "证件处理器是空");
        PROCESSORS.put(type, processor);
    }

    /**
     * 注销处理器
     *
     * @param type 证件类型
     */
    public static void unregister(final CredentialType type) {
        Objects.requireNonNull(type, "证件类型是空");
        PROCESSORS.remove(type);
    }

    /**
     * 获取已注册的证件类型列表
     *
     * @return 已注册的证件类型集合
     */
    public static Set<CredentialType> getRegisteredTypes() {
        return PROCESSORS.keySet();
    }

    /**
     * 检查是否支持某证件类型
     *
     * @param type 证件类型
     * @return 是否支持
     */
    public static boolean isSupported(final CredentialType type) {
        return type != null && PROCESSORS.containsKey(type);
    }

    /**
     * 获取证件处理器
     *
     * @param type 证件类型
     * @return 证件处理器
     * @throws UnsupportedOperationException 如果不支持该证件类型
     */
    private static CredentialProcessor<? extends CredentialInfo> getProcessor(final CredentialType type) {
        Objects.requireNonNull(type, "证件类型是空");
        final CredentialProcessor<? extends CredentialInfo> processor = PROCESSORS.get(type);
        if (processor == null) {
            throw new UnsupportedOperationException("不支持校验" + type);
        }
        return processor;
    }

    /**
     * 规格化证件号码
     *
     * @param credential 证件号码
     * @return 规格化后的证件号码（去除空格、转大写）
     */
    public static String normalize(final String credential) {
        if (credential == null) {
            return null;
        }
        return credential.trim().toUpperCase();
    }

    /**
     * 智能识别证件类型
     * <p>
     * 遍历所有已注册的处理器，通过校验逻辑识别证件类型。
     * 校验通过的证件类型会被收集返回。
     * </p>
     *
     * @param credential 证件号码
     * @return 推断的证件类型列表（空列表表示无匹配，单元素表示唯一类型，多元素表示多个候选）
     */
    public static List<CredentialType> detect(final String credential) {
        String normalized = normalize(credential);
        if (normalized == null) {
            return Collections.emptyList();
        }
        List<CredentialType> matchedTypes = new ArrayList<>();
        for (Map.Entry<CredentialType, CredentialProcessor<? extends CredentialInfo>> entry : PROCESSORS.entrySet()) {
            ValidationResult result = entry.getValue().validate(normalized);
            if (result.isValid()) {
                matchedTypes.add(entry.getKey());
            }
        }
        return matchedTypes;
    }

    /**
     * 校验证件（自动识别类型）
     *
     * @param credential 证件号码
     * @return 如果证件号码有效则返回true，否则返回false
     */
    public static boolean valid(final String credential) {
        List<CredentialType> types = detect(credential);
        return !types.isEmpty() && valid(types.get(0), credential);
    }

    /**
     * 校验证件
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 如果证件号码有效则返回true，否则返回false
     */
    public static boolean valid(final CredentialType type, final String credential) {
        Objects.requireNonNull(credential, "证件号码是空");
        final CredentialProcessor<? extends CredentialInfo> processor = getProcessor(type);
        return processor.valid(credential);
    }

    /**
     * 校验证件并返回详细结果（自动识别类型）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    public static ValidationResult validate(final String credential) {
        List<CredentialType> types = detect(credential);
        if (types.isEmpty()) {
            return ValidationResult.failure(com.xiahaimoyu.credentialkit.enums.ErrorCode.BASIC_FORMAT_ERROR, "无法识别证件类型", credential);
        }
        return validate(types.get(0), credential);
    }

    /**
     * 校验证件并返回详细结果
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 校验结果
     */
    public static ValidationResult validate(final CredentialType type, final String credential) {
        Objects.requireNonNull(credential, "证件号码是空");
        final CredentialProcessor<? extends CredentialInfo> processor = getProcessor(type);
        return processor.validate(credential);
    }

    /**
     * 解析证件（自动识别类型）
     *
     * @param credential 证件号码
     * @return 解析后的证件信息，如果解析失败则返回Optional.empty()
     */
    public static Optional<? extends CredentialInfo> parse(final String credential) {
        List<CredentialType> types = detect(credential);
        if (types.isEmpty()) {
            return Optional.empty();
        }
        return parse(types.get(0), credential);
    }

    /**
     * 解析证件
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 解析后的证件信息，如果解析失败则返回Optional.empty()
     */
    public static Optional<? extends CredentialInfo> parse(final CredentialType type, final String credential) {
        Objects.requireNonNull(credential, "证件号码是空");
        final CredentialProcessor<? extends CredentialInfo> processor = getProcessor(type);
        return processor.parse(credential);
    }

    /**
     * 批量校验证件
     *
     * @param credentials 证件号码集合
     * @return 校验结果列表
     */
    public static List<ValidationResult> validateBatch(final Collection<String> credentials) {
        if (credentials == null) {
            return Collections.emptyList();
        }
        return credentials.stream()
                .map(CredentialKit::validate)
                .collect(Collectors.toList());
    }

    /**
     * 批量校验证件（指定类型）
     *
     * @param type        证件类型
     * @param credentials 证件号码集合
     * @return 校验结果列表
     */
    public static List<ValidationResult> validateBatch(final CredentialType type, final Collection<String> credentials) {
        if (credentials == null) {
            return Collections.emptyList();
        }
        return credentials.stream()
                .map(credential -> validate(type, credential))
                .collect(Collectors.toList());
    }

    /**
     * 批量解析证件
     *
     * @param credentials 证件号码集合
     * @return 解析结果映射（证件号码 {@code ->} 解析信息）
     */
    public static Map<String, Optional<? extends CredentialInfo>> parseBatch(final Collection<String> credentials) {
        if (credentials == null) {
            return Collections.emptyMap();
        }
        return credentials.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        CredentialKit::parse,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * 批量解析证件（指定类型）
     *
     * @param type        证件类型
     * @param credentials 证件号码集合
     * @return 解析结果映射（证件号码 {@code ->} 解析信息）
     */
    public static Map<String, Optional<? extends CredentialInfo>> parseBatch(final CredentialType type, final Collection<String> credentials) {
        if (credentials == null) {
            return Collections.emptyMap();
        }
        return credentials.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        credential -> parse(type, credential),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * 获取证件类型统计
     * <p>
     * 对一批证件号码进行类型识别并统计各类型数量。
     * 当一个证件号码匹配多个类型时，使用第一个匹配的类型进行统计。
     * </p>
     *
     * @param credentials 证件号码集合
     * @return 类型统计映射（证件类型 {@code ->} 数量）
     */
    public static Map<CredentialType, Long> getTypeStatistics(final Collection<String> credentials) {
        if (credentials == null) {
            return Collections.emptyMap();
        }
        return credentials.stream()
                .map(CredentialKit::detect)
                .filter(types -> !types.isEmpty())
                .map(types -> types.get(0))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }
}