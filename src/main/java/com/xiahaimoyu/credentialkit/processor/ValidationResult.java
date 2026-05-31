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

    private static final ValidationResult SUCCESS = new ValidationResult(true, null);

    private final boolean valid;
    private final ErrorCode errorCode;

    private ValidationResult(boolean valid, ErrorCode errorCode) {
        this.valid = valid;
        this.errorCode = errorCode;
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
        Objects.requireNonNull(errorCode, "errorCode不能为空");
        return new ValidationResult(false, errorCode);
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
     * 获取错误描述
     *
     * @return 错误描述，如果校验成功则返回空字符串
     */
    public String getErrorDescription() {
        if (valid) {
            return "";
        }
        return "[" + errorCode.name() + "] " + errorCode.getDesc();
    }

    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true}";
        }
        return "ValidationResult{valid=false, errorCode=" + errorCode + "}";
    }
}