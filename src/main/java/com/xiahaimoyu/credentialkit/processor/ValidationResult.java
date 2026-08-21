/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 校验结果
 * <p>
 * 结果对象不可变，失败结果按错误码预缓存，高频调用（如批量校验、智能识别）时零分配。
 * </p>
 *
 * @author Howard.Li
 */
public final class ValidationResult {

    /**
     * 成功结果（全局单例）
     */
    private static final ValidationResult SUCCESS = new ValidationResult(true, null);

    /**
     * 失败结果缓存（按错误码）
     */
    private static final Map<ErrorCode, ValidationResult> FAILURE_CACHE;

    static {
        Map<ErrorCode, ValidationResult> cache = new EnumMap<>(ErrorCode.class);
        for (ErrorCode errorCode : ErrorCode.values()) {
            cache.put(errorCode, new ValidationResult(false, errorCode));
        }
        FAILURE_CACHE = Collections.unmodifiableMap(cache);
    }

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
     * <p>
     * 相同错误码返回同一实例，调用方可安全共享结果对象。
     * </p>
     *
     * @param errorCode 错误码
     * @return 失败结果
     * @throws NullPointerException 如果errorCode为null
     */
    public static ValidationResult failure(ErrorCode errorCode) {
        return FAILURE_CACHE.get(Objects.requireNonNull(errorCode, "errorCode不能为空"));
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
