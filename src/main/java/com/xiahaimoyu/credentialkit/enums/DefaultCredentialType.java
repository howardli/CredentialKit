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

    MAINLAND_RESIDENT_ID_NUMBER("中华人民共和国居民身份证号码", "Chinese Resident Identity Card Number"),

    HONGKONG_MACAO_TRAVEL_PERMIT_NUMBER("港澳居民来往内地通行证号码", "Mainland Travel Permit for Hong Kong and Macao Residents Number"),

    TAIWAN_TRAVEL_PERMIT_NUMBER("台湾居民来往大陆通行证号码", "Mainland Travel Permit for Taiwan Residents Number"),

    HONGKONG_MACAO_RESIDENCE_PERMIT_NUMBER("港澳居民居住证号码", "Residence Permit Number for Hong Kong and Macao Residents"),

    TAIWAN_RESIDENCE_PERMIT_NUMBER("台湾居民居住证号码", "Residence Permit Number for Taiwan Residents"),

    FOREIGNER_PERMANENT_RESIDENCE_ID_NUMBER("外国人永久居留身份证号码", "Foreign Permanent Resident ID Card Number"),

    MACHINE_READABLE_PASSPORT_CODE("可机读护照编码", "Machine Readable Passport Code"),

    UNIFIED_SOCIAL_CREDIT_CODE("统一社会信用代码", "Unified Social Credit Identifier"),

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
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 获取英文名称
     *
     * @return 英文名称
     */
    public String getEnglishName() {
        return englishName;
    }
}
