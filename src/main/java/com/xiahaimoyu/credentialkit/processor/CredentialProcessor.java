/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.info.CredentialInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * 证件处理器
 * <p>
 * 通过构造器注入校验器和解析器，保证对象构造完成后即处于可用状态，
 * 避免"父类构造器调用可覆盖方法"导致的子类实例字段未初始化问题。
 * </p>
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
     *
     * @param validators 校验器列表（按顺序执行，遇到第一个失败即短路），至少包含一个校验器
     * @param parsers    解析器列表（按顺序执行），允许为空
     * @throws NullPointerException     如果任一列表为空
     * @throws IllegalArgumentException 如果校验器列表为空列表（无校验器的处理器会接受任意输入）
     */
    protected CredentialProcessor(List<CredentialValidator> validators, List<CredentialParser<T>> parsers) {
        Objects.requireNonNull(validators, "校验器列表是空");
        Objects.requireNonNull(parsers, "解析器列表是空");
        if (validators.isEmpty()) {
            throw new IllegalArgumentException("校验器列表不能为空列表");
        }
        this.validators = Collections.unmodifiableList(new ArrayList<>(validators));
        this.parsers = Collections.unmodifiableList(new ArrayList<>(parsers));
    }

    /**
     * 内部校验方法
     * <p>
     * 输入已保证非空（{@link #normalize(String)}对null返回空字符串）。
     * </p>
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
     * 校验并返回详细结果
     *
     * @param credential 证件号码（允许为null，规格化后为空字符串，校验必然失败）
     * @return 校验结果
     */
    public ValidationResult validate(String credential) {
        return internalValidate(normalize(credential));
    }

    /**
     * 解析证件
     *
     * @param credential 证件号码（允许为null，规格化后为空字符串，解析必然失败）
     * @return 解析后的证件信息，如果校验失败则返回Optional.empty()
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
     * 规格化证件号码
     * <p>
     * 使用{@link Locale#ROOT}做大写转换，避免默认区域（如土耳其语locale下i会转为İ）
     * 导致规格化结果与预期不符。
     * </p>
     *
     * @param credential 证件号码
     * @return 规格化后的证件号码（去除首尾空格、转大写），如果输入为null则返回空字符串
     */
    protected String normalize(String credential) {
        if (credential == null) {
            return "";
        }
        return credential.trim().toUpperCase(Locale.ROOT);
    }

    /**
     * 创建证件信息对象
     *
     * @return 证件信息对象
     */
    protected abstract T createInfo();
}
