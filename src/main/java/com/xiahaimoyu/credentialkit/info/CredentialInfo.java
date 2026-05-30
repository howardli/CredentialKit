/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.CredentialType;

/**
 * 证件信息基类
 * <p>
 * 所有证件解析信息都继承此类，提供证件类型获取等通用方法。
 * </p>
 *
 * @author Howard.Li
 */
public abstract class CredentialInfo {

    /**
     * 获取证件类型
     * <p>
     * 子类应覆盖此方法返回具体的证件类型。
 * </p>
     *
     * @return 证件类型，默认返回null
     */
    public CredentialType getType() {
        return null;
    }

    /**
     * 获取字符串表示
     *
     * @return 类名简写
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
