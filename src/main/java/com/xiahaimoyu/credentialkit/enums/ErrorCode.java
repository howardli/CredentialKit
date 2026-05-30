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
     * 地区错误
     */
    REGION_ERROR("地区错误"),

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
