/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;

import java.util.Objects;
import java.util.Optional;

/**
 * 校验结果
 *
 * @author Howard.Li
 */
public final class ValidationResult {

    private static final ValidationResult SUCCESS = new ValidationResult(true, null, null);

    private final boolean valid;
    private final ErrorCode errorCode;
    private final String message;

    private ValidationResult(boolean valid, ErrorCode errorCode, String message) {
        this.valid = valid;
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * 获取成功结果
     *
     * @return 成功结果
     */
    public static ValidationResult success() {
        return SUCCESS;
    }

    /**
     * 获取失败结果
     *
     * @param errorCode 错误码
     * @return 失败结果
     * @throws NullPointerException 如果errorCode为null
     */
    public static ValidationResult failure(ErrorCode errorCode) {
        Objects.requireNonNull(errorCode, "errorCode must not be null");
        return new ValidationResult(false, errorCode, null);
    }

    /**
     * 获取失败结果（带详细信息）
     *
     * @param errorCode 错误码
     * @param message   详细信息
     * @return 失败结果
     * @throws NullPointerException 如果errorCode为null
     */
    public static ValidationResult failure(ErrorCode errorCode, String message) {
        Objects.requireNonNull(errorCode, "errorCode must not be null");
        return new ValidationResult(false, errorCode, message);
    }

    /**
     * 是否有效
     *
     * @return 如果有效则返回true，否则返回false
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public Optional<ErrorCode> getErrorCode() {
        return Optional.ofNullable(errorCode);
    }

    /**
     * 获取详细信息
     *
     * @return 详细信息
     */
    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }
}
