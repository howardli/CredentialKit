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

    /**
     * 男性
     */
    MALE("男"),

    /**
     * 女性
     */
    FEMALE("女"),

    /**
     * 未知
     */
    UNKNOWN("未知"),

    ;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param desc 描述
     */
    Gender(String desc) {
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
     * 获取描述
     *
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }
}
