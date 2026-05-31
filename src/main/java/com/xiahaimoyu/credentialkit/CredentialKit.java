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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
        if (credential == null) {
            return Collections.emptyList();
        }
        String normalized = credential.trim().toUpperCase();
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
}