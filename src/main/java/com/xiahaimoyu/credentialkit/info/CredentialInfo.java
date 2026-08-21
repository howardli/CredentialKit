/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.CredentialType;

import java.util.Objects;

/**
 * 证件信息基类
 * <p>
 * 所有证件解析信息都继承此类。证件类型是注册关系的属性：
 * 通过{@code CredentialKit}/{@code CredentialRegistry}解析时，由注册中心在解析后
 * 写入实际注册的证件类型；直接使用处理器解析（或自行构造）时类型为null，
 * 可通过{@link #setType(CredentialType)}设置。
 * </p>
 *
 * @author Howard.Li
 */
public abstract class CredentialInfo {

    /**
     * 证件类型（由解析流程设置）
     */
    private CredentialType type;

    /**
     * 获取证件类型
     *
     * @return 证件类型；直接使用处理器解析或未设置时为null
     */
    public final CredentialType getType() {
        return type;
    }

    /**
     * 设置证件类型
     * <p>
     * 由{@code CredentialRegistry}在解析完成后写入实际注册的证件类型，
     * 保证{@link #getType()}与解析时的注册类型一致。
     * </p>
     *
     * @param type 证件类型
     * @throws NullPointerException 如果type为null
     */
    public final void setType(CredentialType type) {
        this.type = Objects.requireNonNull(type, "证件类型是空");
    }

    /**
     * 获取字符串表示
     *
     * @return 类名简写
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
