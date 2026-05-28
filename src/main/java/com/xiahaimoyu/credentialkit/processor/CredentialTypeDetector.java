/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;

/**
 * 证件类型推断器
 * <p>
 * 根据证件号码的特征自动推断证件类型。
 * </p>
 *
 * @author Howard.Li
 */
@FunctionalInterface
public interface CredentialTypeDetector {

    /**
     * 根据证件号码推断证件类型
     *
     * @param credential 证件号码（已规格化）
     * @return 推断的证件类型，如果不能确定则返回null
     */
    DefaultCredentialType detect(String credential);
}