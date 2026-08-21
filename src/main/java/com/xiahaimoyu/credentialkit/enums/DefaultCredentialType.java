/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 默认证件类型
 *
 * @author Howard.Li
 */
public enum DefaultCredentialType implements CredentialType {

    MAINLAND_RESIDENT_ID("中华人民共和国居民身份证", "Chinese Resident Identity Card", 100),

    HK_MACAO_TRAVEL_PERMIT("港澳居民来往内地通行证", "Mainland Travel Permit for Hong Kong and Macao Residents", 110),

    TAIWAN_TRAVEL_PERMIT("台湾居民来往大陆通行证", "Mainland Travel Permit for Taiwan Residents", 120),

    HK_MACAO_RESIDENCE_PERMIT("港澳居民居住证", "Residence Permit for Hong Kong and Macao Residents", 130),

    TAIWAN_RESIDENCE_PERMIT("台湾居民居住证", "Residence Permit for Taiwan Residents", 140),

    FOREIGNER_PERMANENT_RESIDENCE_ID("外国人永久居留身份证", "Foreign Permanent Resident ID Card", 150),

    MACHINE_READABLE_PASSPORT("可机读护照", "Machine Readable Passport", 160),

    UNIFIED_SOCIAL_CREDIT("统一社会信用代码", "Unified Social Credit Identifier", 170),

    ;

    /**
     * 中文名称
     */
    private final String chineseName;

    /**
     * 英文名称
     */
    private final String englishName;

    /**
     * 智能识别优先级（数值越小优先级越高，使用较大数值让自定义类型默认排在前面）
     */
    private final int detectPriority;

    /**
     * 构造函数
     */
    DefaultCredentialType(String chineseName, String englishName, int detectPriority) {
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.detectPriority = detectPriority;
    }

    /**
     * 获取中文名称
     *
     * @return 中文名称
     */
    @Override
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 获取英文名称
     *
     * @return 英文名称
     */
    @Override
    public String getEnglishName() {
        return englishName;
    }

    /**
     * 获取智能识别优先级
     *
     * @return 优先级（数值越小优先级越高）
     */
    @Override
    public int getDetectPriority() {
        return detectPriority;
    }
}
