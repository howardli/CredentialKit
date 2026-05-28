/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.cache.CredentialCache;
import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.CredentialTypeDetector;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;

import java.util.Optional;

/**
 * CredentialKit配置构建器
 * <p>
 * 提供灵活的配置方式，支持：
 * - 自定义缓存
 * - 自定义推断器
 * - 自定义处理器
 * - 启用/禁用功能
 * </p>
 *
 * @author Howard.Li
 */
public final class CredentialKitBuilder {

    private CredentialCache cache;
    private boolean enableCache = false;
    private boolean enableAutoDetect = true;

    /**
     * 构造器
     */
    public CredentialKitBuilder() {
    }

    /**
     * 设置缓存
     *
     * @param cache 缓存实例
     * @return this
     */
    public CredentialKitBuilder withCache(CredentialCache cache) {
        this.cache = cache;
        this.enableCache = true;
        return this;
    }

    /**
     * 设置缓存大小
     *
     * @param cacheSize 缓存大小
     * @return this
     */
    public CredentialKitBuilder withCacheSize(int cacheSize) {
        this.cache = new CredentialCache(cacheSize);
        this.enableCache = true;
        return this;
    }

    /**
     * 启用缓存
     *
     * @return this
     */
    public CredentialKitBuilder enableCache() {
        this.enableCache = true;
        if (this.cache == null) {
            this.cache = new CredentialCache();
        }
        return this;
    }

    /**
     * 禁用缓存
     *
     * @return this
     */
    public CredentialKitBuilder disableCache() {
        this.enableCache = false;
        return this;
    }

    /**
     * 启用自动识别
     *
     * @return this
     */
    public CredentialKitBuilder enableAutoDetect() {
        this.enableAutoDetect = true;
        return this;
    }

    /**
     * 禁用自动识别
     *
     * @return this
     */
    public CredentialKitBuilder disableAutoDetect() {
        this.enableAutoDetect = false;
        return this;
    }

    /**
     * 注册自定义处理器
     *
     * @param type      证件类型
     * @param processor 处理器
     * @return this
     */
    public CredentialKitBuilder register(CredentialType type, CredentialProcessor<? extends CredentialInfo> processor) {
        CredentialKit.register(type, processor);
        return this;
    }

    /**
     * 注册自定义推断器
     *
     * @param detector 推断器
     * @return this
     */
    public CredentialKitBuilder registerDetector(CredentialTypeDetector detector) {
        CredentialKit.registerDetector(detector);
        return this;
    }

    /**
     * 构建配置实例
     *
     * @return 配置实例
     */
    public CredentialKitConfig build() {
        return new CredentialKitConfig(cache, enableCache, enableAutoDetect);
    }

    /**
     * CredentialKit配置
     */
    public static final class CredentialKitConfig {

        private final CredentialCache cache;
        private final boolean enableCache;
        private final boolean enableAutoDetect;

        private CredentialKitConfig(CredentialCache cache, boolean enableCache, boolean enableAutoDetect) {
            this.cache = cache;
            this.enableCache = enableCache;
            this.enableAutoDetect = enableAutoDetect;
        }

        /**
         * 获取缓存
         *
         * @return 缓存实例
         */
        public CredentialCache getCache() {
            return cache;
        }

        /**
         * 是否启用缓存
         *
         * @return 是否启用
         */
        public boolean isEnableCache() {
            return enableCache;
        }

        /**
         * 是否启用自动识别
         *
         * @return 是否启用
         */
        public boolean isEnableAutoDetect() {
            return enableAutoDetect;
        }

        /**
         * 使用缓存解析
         *
         * @param credential 证件号码
         * @return 解析结果
         */
        public Optional<? extends CredentialInfo> parseWithCache(String credential) {
            if (!enableCache || cache == null) {
                return CredentialKit.parse(credential);
            }
            Optional<? extends CredentialInfo> cached = cache.get(credential);
            if (cached != null) {
                return cached;
            }
            Optional<? extends CredentialInfo> result = CredentialKit.parse(credential);
            cache.put(credential, result);
            return result;
        }

        /**
         * 使用缓存校验
         *
         * @param credential 证件号码
         * @return 校验结果（注意：校验结果不缓存，因为校验结果可能因配置变化）
         */
        public ValidationResult validate(String credential) {
            return CredentialKit.validate(credential);
        }

        /**
         * 清空缓存
         */
        public void clearCache() {
            if (cache != null) {
                cache.clear();
            }
        }
    }
}