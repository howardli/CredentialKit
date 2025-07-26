/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.info.CredentialInfo;

/**
 * 证件解析器
 *
 * @author Howard.Li
 */
public interface CredentialParser<T extends CredentialInfo> {

    /**
     * 解析证件
     *
     * @param credential 证件
     * @param info       信息
     */
    void parse(String credential, T info);
}
