/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 错误码
 *
 * @author Howard.Li
 */
public enum ErrorCode {

    BASIC_FORMAT_ERROR("BASIC_FORMAT_ERROR", "基本格式错误"),

    REGION_ERROR("REGION_ERROR", "地区错误"),

    NAME_ERROR("NAME_ERROR", "名字错误"),

    BIRTH_DATE_ERROR("BIRTH_DATE_ERROR", "生日错误"),

    EXPIRATION_DATE_ERROR("EXPIRATION_DATE_ERROR", "有效期错误"),

    CHECK_DIGIT_ERROR("CHECK_DIGIT_ERROR", "校验位错误"),

    ORG_CATEGORY_ERROR("ORG_CATEGORY_ERROR", "机构类别错误"),

    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param code 编码
     * @param desc 描述
     */
    ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取编码
     *
     * @return 编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }

}
