/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;

import java.util.Objects;
import java.util.Optional;

/**
 * 校验结果
 * <p>
 * 包含校验状态、错误码、错误信息以及原始证件号码，
 * 便于调试和日志记录。
 * </p>
 *
 * @author Howard.Li
 */
public final class ValidationResult {

    private static final ValidationResult SUCCESS = new ValidationResult(true, null, null, null);

    private final boolean valid;
    private final ErrorCode errorCode;
    private final String message;
    private final String credential;

    private ValidationResult(boolean valid, ErrorCode errorCode, String message, String credential) {
        this.valid = valid;
        this.errorCode = errorCode;
        this.message = message;
        this.credential = credential;
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
     * 获取成功结果（带证件号码）
     *
     * @param credential 证件号码
     * @return 成功结果
     */
    public static ValidationResult success(String credential) {
        return new ValidationResult(true, null, null, credential);
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
        return new ValidationResult(false, errorCode, null, null);
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
        return new ValidationResult(false, errorCode, message, null);
    }

    /**
     * 获取失败结果（带详细信息及证件号码）
     *
     * @param errorCode  错误码
     * @param message    详细信息
     * @param credential 证件号码
     * @return 失败结果
     * @throws NullPointerException 如果errorCode为null
     */
    public static ValidationResult failure(ErrorCode errorCode, String message, String credential) {
        Objects.requireNonNull(errorCode, "errorCode must not be null");
        return new ValidationResult(false, errorCode, message, credential);
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

    /**
     * 获取证件号码
     *
     * @return 证件号码
     */
    public Optional<String> getCredential() {
        return Optional.ofNullable(credential);
    }

    /**
     * 获取错误描述
     *
     * @return 错误描述，如果校验成功则返回空字符串
     */
    public String getErrorDescription() {
        if (valid) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(errorCode.getCode()).append("]");
        sb.append(message != null ? message : errorCode.getDesc());
        if (credential != null) {
            sb.append(" (证件号码: ").append(credential).append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true}";
        }
        return "ValidationResult{valid=false, errorCode=" + errorCode + ", message='" + message + "', credential='" + credential + "'}";
    }
}
