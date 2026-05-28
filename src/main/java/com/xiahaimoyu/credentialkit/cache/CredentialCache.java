/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.cache;

import com.xiahaimoyu.credentialkit.info.CredentialInfo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 证件解析结果缓存
 * <p>
 * 使用LRU（最近最少使用）策略缓存证件解析结果，
 * 避免对相同证件号码重复解析，提高性能。
 * </p>
 *
 * @author Howard.Li
 */
public final class CredentialCache {

    /**
     * 默认缓存大小
     */
    private static final int DEFAULT_CACHE_SIZE = 1000;

    /**
     * 缓存映射
     */
    private final Map<String, Optional<? extends CredentialInfo>> cache;

    /**
     * 缓存大小
     */
    private final int cacheSize;

    /**
     * 构造函数（使用默认缓存大小）
     */
    public CredentialCache() {
        this(DEFAULT_CACHE_SIZE);
    }

    /**
     * 构造函数
     *
     * @param cacheSize 缓存大小
     */
    public CredentialCache(int cacheSize) {
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("缓存大小必须大于0");
        }
        this.cacheSize = cacheSize;
        this.cache = new LinkedHashMap<String, Optional<? extends CredentialInfo>>(cacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Optional<? extends CredentialInfo>> eldest) {
                return size() > CredentialCache.this.cacheSize;
            }
        };
    }

    /**
     * 获取缓存结果
     *
     * @param credential 证件号码
     * @return 缓存的解析结果，如果不存在则返回null
     */
    public Optional<? extends CredentialInfo> get(String credential) {
        if (credential == null) {
            return null;
        }
        return cache.get(credential);
    }

    /**
     * 存入缓存
     *
     * @param credential 证件号码
     * @param info       解析结果
     */
    public void put(String credential, Optional<? extends CredentialInfo> info) {
        if (credential != null) {
            cache.put(credential, info);
        }
    }

    /**
     * 清空缓存
     */
    public void clear() {
        cache.clear();
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存大小
     */
    public int size() {
        return cache.size();
    }

    /**
     * 获取最大缓存容量
     *
     * @return 最大缓存容量
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * 检查缓存是否包含某个证件号码
     *
     * @param credential 证件号码
     * @return 是否包含
     */
    public boolean contains(String credential) {
        return credential != null && cache.containsKey(credential);
    }
}