/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.info.CredentialInfo;

import java.util.List;

/**
 * 证件处理器
 *
 * @author Howard.Li
 */
public abstract class CredentialProcessor<T extends CredentialInfo> {

    /**
     * 校验器列表
     */
    private final List<CredentialValidator> validators;

    /**
     * 解析器列表
     */
    private final List<CredentialParser<T>> parsers;

    /**
     * 构造器
     */
    protected CredentialProcessor() {
        validators = buildValidators();
        parsers = buildParsers();
    }

    /**
     * 构造校验器列表
     *
     * @return 校验器列表
     */
    protected abstract List<CredentialValidator> buildValidators();

    /**
     * 构造解析器列表
     *
     * @return 解析器列表
     */
    protected abstract List<CredentialParser<T>> buildParsers();

    /**
     * 校验
     *
     * @param credential 证件
     */
    public void valid(String credential) {
        credential = normalize(credential);
        for (CredentialValidator rule : validators) {
            rule.valid(credential);
        }
    }

    /**
     * 解析
     *
     * @param credential 证件
     * @return 信息
     */
    public T parse(String credential) {
        credential = normalize(credential);
        valid(credential);
        T info = getInfo();
        for (CredentialParser<T> parser : parsers) {
            parser.parse(credential, info);
        }
        return info;
    }

    /**
     * 规格化
     *
     * @param credential 证件
     * @return 规格化后的证件
     */
    protected String normalize(String credential) {
        return credential.trim().toUpperCase();
    }

    /**
     * 获取默认信息
     *
     * @return 信息
     */
    protected abstract T getInfo();
}
