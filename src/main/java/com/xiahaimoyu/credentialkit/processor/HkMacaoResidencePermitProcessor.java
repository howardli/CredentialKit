/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.HkMacaoResidencePermitInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 港澳居民居住证处理器
 * <p>
 * 号码以810000（香港）或820000（澳门）开头，结构同18位居民身份证，
 * 校验与解析逻辑见{@code AbstractResidencePermitProcessor}。
 * </p>
 *
 * @author Howard.Li
 */
public class HkMacaoResidencePermitProcessor extends AbstractResidencePermitProcessor<HkMacaoResidencePermitInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^8[12]0000\\d{11}[0-9X]$");

    /**
     * 前6位地区码到地区信息的映射（810000=香港，820000=澳门）
     */
    private static final Map<String, DomesticRegionInfo> PREFIX_REGIONS;

    static {
        Map<String, DomesticRegionInfo> map = new HashMap<>();
        map.put("810000", CredentialRegionInfos.HONG_KONG_RESIDENCE_PERMIT);
        map.put("820000", CredentialRegionInfos.MACAO_RESIDENCE_PERMIT);
        PREFIX_REGIONS = Collections.unmodifiableMap(map);
    }

    /**
     * 构造器
     */
    public HkMacaoResidencePermitProcessor() {
        super(PATTERN, PREFIX_REGIONS);
    }

    /**
     * 获取港澳居民居住证信息
     *
     * @return 港澳居民居住证信息
     */
    @Override
    protected HkMacaoResidencePermitInfo createInfo() {
        return new HkMacaoResidencePermitInfo();
    }
}
