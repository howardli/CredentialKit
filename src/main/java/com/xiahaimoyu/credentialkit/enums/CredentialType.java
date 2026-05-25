/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 证件类型接口
 * <p>
 * 该接口定义证件类型的基本属性，用于统一不同证件类型的定义。
 * 实现该接口的枚举类定义具体的证件类型及其名称。
 * </p>
 *
 * @author Howard.Li
 */
public interface CredentialType {

    /**
     * 获取证件类型中文名称
     *
     * @return 中文名称
     */
    String getChineseName();

    /**
     * 获取证件类型英文名称
     *
     * @return 英文名称
     */
    String getEnglishName();
}
