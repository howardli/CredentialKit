/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 证件工具类
 * <p>
 * 提供证件校验、解析、智能识别等功能。
 * 支持多种证件类型的注册、注销和自定义扩展。
 * </p>
 * <p>
 * 本类的静态方法均委托给默认注册中心（{@link #getDefault()}）。
 * 默认注册中心在首次使用时才创建（懒加载，双重检查锁定）：若SPI提供者有缺陷，
 * 每次调用都会抛出携带提供者信息的清晰异常，而不是类初始化失败导致的
 * {@code NoClassDefFoundError}。
 * 需要独立注册表（测试隔离、多租户等场景）时，请使用{@link CredentialRegistry#create()}创建独立实例。
 * </p>
 *
 * @author Howard.Li
 * @see CredentialRegistry
 */
public final class CredentialKit {

    /**
     * 默认注册中心（懒加载，首次访问时创建）
     */
    private static volatile CredentialRegistry defaultRegistry;

    /**
     * 构造器
     */
    private CredentialKit() {
    }

    /**
     * 获取默认注册中心
     * <p>
     * 静态方法（{@code validate}、{@code parse}、{@code detect}等）均委托给该注册中心。
     * 首次调用时创建（注册内置处理器并加载SPI提供者）。
     * </p>
     *
     * @return 默认注册中心
     */
    public static CredentialRegistry getDefault() {
        CredentialRegistry registry = defaultRegistry;
        if (registry == null) {
            synchronized (CredentialKit.class) {
                if (defaultRegistry == null) {
                    defaultRegistry = CredentialRegistry.create();
                }
                registry = defaultRegistry;
            }
        }
        return registry;
    }

    /**
     * 注册处理器（操作默认注册中心）
     *
     * @param type      证件类型
     * @param processor 证件处理器
     */
    public static void register(final CredentialType type, final CredentialProcessor<? extends CredentialInfo> processor) {
        getDefault().register(type, processor);
    }

    /**
     * 注销处理器（操作默认注册中心）
     *
     * @param type 证件类型
     */
    public static void unregister(final CredentialType type) {
        getDefault().unregister(type);
    }

    /**
     * 获取默认注册中心已注册的全部证件类型
     *
     * @return 已注册的证件类型集合（不可变）
     */
    public static Set<CredentialType> getSupportedTypes() {
        return getDefault().getSupportedTypes();
    }

    /**
     * 智能识别证件类型（操作默认注册中心）
     * <p>
     * 遍历所有已注册的处理器，通过校验逻辑识别证件类型。
     * 校验通过的证件类型会被收集返回，按识别优先级升序排列。
     * </p>
     *
     * @param credential 证件号码
     * @return 推断的证件类型列表（空列表表示无匹配，单元素表示唯一类型，多元素表示多个候选）
     */
    public static List<CredentialType> detect(final String credential) {
        return getDefault().detect(credential);
    }

    /**
     * 校验证件并返回详细结果（操作默认注册中心）
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 校验结果
     */
    public static ValidationResult validate(final CredentialType type, final String credential) {
        return getDefault().validate(type, credential);
    }

    /**
     * 解析证件（操作默认注册中心）
     * <p>
     * 解析成功后，证件信息的{@link CredentialInfo#getType()}返回实际注册的证件类型。
     * </p>
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @return 解析后的证件信息，如果解析失败则返回Optional.empty()
     */
    public static Optional<? extends CredentialInfo> parse(final CredentialType type, final String credential) {
        return getDefault().parse(type, credential);
    }

    /**
     * 解析证件并返回指定类型（操作默认注册中心）
     * <p>
     * 与{@link #parse(CredentialType, String)}相比，本方法直接返回具体类型，调用方无需强转。
     * 如果解析结果的实际类型与{@code infoClass}不符则抛出{@link ClassCastException}。
     * </p>
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @param infoClass  证件信息类型
     * @param <T>        证件信息类型
     * @return 解析后的证件信息，如果解析失败则返回Optional.empty()
     * @throws NullPointerException 如果infoClass是空
     * @throws ClassCastException   如果解析结果的实际类型与infoClass不符
     */
    public static <T extends CredentialInfo> Optional<T> parse(final CredentialType type, final String credential, final Class<T> infoClass) {
        return getDefault().parse(type, credential, infoClass);
    }
}
