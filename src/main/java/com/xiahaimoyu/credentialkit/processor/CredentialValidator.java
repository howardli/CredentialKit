/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

/**
 * 证件校验器
 *
 * @author Howard.Li
 */
public interface CredentialValidator {

    /**
     * 校验
     *
     * @param credential 证件
     */
    void valid(String credential);
}
