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

    MAINLAND_RESIDENT_ID("中华人民共和国居民身份证", "Chinese Resident Identity Card"),

    HK_MACAO_TRAVEL_PERMIT("港澳居民来往内地通行证", "Mainland Travel Permit for Hong Kong and Macao Residents"),

    TAIWAN_TRAVEL_PERMIT("台湾居民来往大陆通行证", "Mainland Travel Permit for Taiwan Residents"),

    HK_MACAO_RESIDENCE_PERMIT("港澳居民居住证", "Residence Permit for Hong Kong and Macao Residents"),

    TAIWAN_RESIDENCE_PERMIT("台湾居民居住证", "Residence Permit for Taiwan Residents"),

    FOREIGNER_PERMANENT_RESIDENCE_ID("外国人永久居留身份证", "Foreign Permanent Resident ID Card"),

    MACHINE_READABLE_PASSPORT("可机读护照", "Machine Readable Passport"),

    UNIFIED_SOCIAL_CREDIT("统一社会信用代码", "Unified Social Credit Identifier"),

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
     * 构造函数
     */
    DefaultCredentialType(String chineseName, String englishName) {
        this.chineseName = chineseName;
        this.englishName = englishName;
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
}
