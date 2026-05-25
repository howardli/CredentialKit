/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.MainlandResidentIdNumberProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMoTravelPermitNumberProcessor;
import com.xiahaimoyu.credentialkit.processor.TwTravelPermitNumberProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMoResidencePermitNumberProcessor;
import com.xiahaimoyu.credentialkit.processor.TwResidencePermitNumberProcessor;
import com.xiahaimoyu.credentialkit.processor.ForeignerPermanentResidenceIdNumberProcessor;
import com.xiahaimoyu.credentialkit.processor.MachineReadablePassportCodeProcessor;
import com.xiahaimoyu.credentialkit.processor.UnifiedSocialCreditCodeProcessor;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 证件工具类
 *
 * @author Howard.Li
 */
public final class CredentialKit {

    /**
     * 证件和处理器映射
     */
    private static final Map<CredentialType, CredentialProcessor<? extends CredentialInfo>> PROCESSORS = new ConcurrentHashMap<>();

    //注册默认证件处理器
    static {
        register(DefaultCredentialType.MAINLAND_RESIDENT_ID_NUMBER, new MainlandResidentIdNumberProcessor());
        register(DefaultCredentialType.HONGKONG_MACAO_TRAVEL_PERMIT_NUMBER, new HkMoTravelPermitNumberProcessor());
        register(DefaultCredentialType.TAIWAN_TRAVEL_PERMIT_NUMBER, new TwTravelPermitNumberProcessor());
        register(DefaultCredentialType.HONGKONG_MACAO_RESIDENCE_PERMIT_NUMBER, new HkMoResidencePermitNumberProcessor());
        register(DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT_NUMBER, new TwResidencePermitNumberProcessor());
        register(DefaultCredentialType.FOREIGNER_PERMANENT_RESIDENCE_ID_NUMBER, new ForeignerPermanentResidenceIdNumberProcessor());
        register(DefaultCredentialType.MACHINE_READABLE_PASSPORT_CODE, new MachineReadablePassportCodeProcessor());
        register(DefaultCredentialType.UNIFIED_SOCIAL_CREDIT_CODE, new UnifiedSocialCreditCodeProcessor());
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
