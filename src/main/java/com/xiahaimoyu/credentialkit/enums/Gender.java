/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 性别
 *
 * @author Howard.Li
 */
public enum Gender {

    MALE("MALE", "男"),

    FEMALE("FEMALE", "女"),

    UNKNOWN("UNKNOWN", "未知"),

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
    Gender(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 从数字位解析性别（身份证规则：偶数为女，奇数为男）
     *
     * @param digit 数字位
     * @return 性别
     */
    public static Gender fromDigit(int digit) {
        return (digit % 2 == 0) ? FEMALE : MALE;
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
