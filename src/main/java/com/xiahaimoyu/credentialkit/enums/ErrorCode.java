/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 错误码枚举
 * <p>
 * 定义证件校验过程中可能出现的错误类型。
 * </p>
 *
 * @author Howard.Li
 */
public enum ErrorCode {

    /**
     * 基本格式错误
     */
    BASIC_FORMAT_ERROR("基本格式错误"),

    /**
     * 地区错误（国内行政区划或护照签发地区）
     */
    REGION_ERROR("地区错误"),

    /**
     * 国际地区编码错误（ISO 3166或机读码扩展编码无法识别）
     * <p>
     * 编码值可能表示国家、地区（如香港HKG、澳门MAC、台湾TWN在ISO 3166中有独立编码）、
     * 国际组织（如UNO、EUE）或无国籍/难民等特殊类别，故不使用"国籍"表述。
     * </p>
     */
    INTERNATIONAL_REGION_ERROR("国籍/地区错误"),

    /**
     * 名字错误
     */
    NAME_ERROR("名字错误"),

    /**
     * 生日错误
     */
    BIRTH_DATE_ERROR("生日错误"),

    /**
     * 有效期错误
     */
    EXPIRATION_DATE_ERROR("有效期错误"),

    /**
     * 校验位错误
     */
    CHECK_DIGIT_ERROR("校验位错误"),

    /**
     * 机构类别错误
     */
    ORG_CATEGORY_ERROR("机构类别错误"),

    ;

    /**
     * 错误描述
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param desc 错误描述
     */
    ErrorCode(String desc) {
        this.desc = desc;
    }

    /**
     * 获取错误描述
     *
     * @return 错误描述
     */
    public String getDesc() {
        return desc;
    }

}
