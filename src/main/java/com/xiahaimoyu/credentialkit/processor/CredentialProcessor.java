/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.info.CredentialInfo;

import java.util.List;
import java.util.Optional;

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
     * 内部校验方法
     *
     * @param normalizedCredential 规格化后的证件
     * @return 校验结果
     */
    private ValidationResult internalValidate(String normalizedCredential) {
        for (CredentialValidator validator : validators) {
            ValidationResult result = validator.validate(normalizedCredential);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.success();
    }

    /**
     * 校验
     *
     * @param credential 证件
     * @return 如果校验通过则返回true，否则返回false
     */
    public boolean valid(String credential) {
        return validate(credential).isValid();
    }

    /**
     * 校验并返回详细结果
     *
     * @param credential 证件
     * @return 校验结果
     */
    public ValidationResult validate(String credential) {
        return internalValidate(normalize(credential));
    }

    /**
     * 解析
     *
     * @param credential 证件
     * @return 信息
     */
    public Optional<T> parse(String credential) {
        String normalizedCredential = normalize(credential);
        ValidationResult validationResult = internalValidate(normalizedCredential);
        if (!validationResult.isValid()) {
            return Optional.empty();
        }
        T info = createInfo();
        for (CredentialParser<T> parser : parsers) {
            parser.parse(normalizedCredential, info);
        }
        return Optional.of(info);
    }

    /**
     * 规格化
     *
     * @param credential 证件
     * @return 规格化后的证件，如果输入为null则返回null
     */
    protected String normalize(String credential) {
        if (credential == null) {
            return null;
        }
        return credential.trim().toUpperCase();
    }

    /**
     * 判断证件是否为新版18位格式
     *
     * @param credential 证件号码
     * @return 是否为18位格式
     */
    protected boolean isNewCredential(String credential) {
        return credential != null && credential.length() == 18;
    }

    /**
     * 创建证件信息对象
     *
     * @return 证件信息对象
     */
    protected abstract T createInfo();
}
