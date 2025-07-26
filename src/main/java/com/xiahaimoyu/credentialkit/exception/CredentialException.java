/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.exception;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;

import java.text.MessageFormat;

/**
 * 证件异常
 *
 * @author Howard.Li
 */
public class CredentialException extends RuntimeException {

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param desc      描述
     */
    private CredentialException(ErrorCode errorCode, String desc) {
        super(formatMessage(errorCode, desc));
        this.errorCode = errorCode;
        this.desc = desc;
    }

    /**
     * 构造异常
     *
     * @param errorCode 错误码
     */
    public static CredentialException of(ErrorCode errorCode, String customMessage, Object... args) {
        return new CredentialException(errorCode, MessageFormat.format(customMessage, args));
    }

    /**
     * 格式化信息
     *
     * @param errorCode 错误码
     */
    private static String formatMessage(ErrorCode errorCode, String customMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(errorCode.getCode()).append("]");
        sb.append(customMessage != null ? customMessage : errorCode.getDesc());
        return sb.toString();
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }

}
