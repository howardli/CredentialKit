/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.*;

import java.util.Map;
import java.util.Objects;
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
     * 校验证件
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 如果证件号码有效则返回true，否则返回false
     */
    public static boolean valid(final CredentialType type, final String credential) {
        Objects.requireNonNull(type, "证件类型是空");
        Objects.requireNonNull(credential, "证件号码是空");
        final CredentialProcessor<? extends CredentialInfo> processor = PROCESSORS.get(type);
        if (processor == null) {
            throw new UnsupportedOperationException("不支持校验" + type);
        }
        try {
            processor.valid(credential);
            return true;
        } catch (CredentialException ex) {
            return false;
        }
    }

    /**
     * 解析证件
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 解析后的证件信息，如果解析失败则返回null
     */
    public static CredentialInfo parse(final CredentialType type, final String credential) {
        Objects.requireNonNull(type, "证件类型是空");
        Objects.requireNonNull(credential, "证件号码是空");
        final CredentialProcessor<?> processor = PROCESSORS.get(type);
        if (processor == null) {
            throw new UnsupportedOperationException("不支持校验" + type);
        }
        try {
            return processor.parse(credential);
        } catch (CredentialException ex) {
            return null;
        }
    }
}
