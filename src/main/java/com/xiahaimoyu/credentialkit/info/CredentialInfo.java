/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

/**
 * 证件信息基类
 *
 * @author Howard.Li
 */
public abstract class CredentialInfo {

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
