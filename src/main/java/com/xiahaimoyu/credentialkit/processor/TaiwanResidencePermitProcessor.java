/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.TaiwanResidencePermitInfo;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 台湾居民居住证处理器
 * <p>
 * 号码固定以830000（台湾）开头，结构同18位居民身份证，
 * 校验与解析逻辑见{@code AbstractResidencePermitProcessor}。
 * </p>
 *
 * @author Howard.Li
 */
public class TaiwanResidencePermitProcessor extends AbstractResidencePermitProcessor<TaiwanResidencePermitInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^830000\\d{11}[0-9X]$");

    /**
     * 前6位地区码到地区信息的映射（台湾居民居住证固定以830000开头）
     */
    private static final Map<String, DomesticRegionInfo> PREFIX_REGIONS =
            Collections.singletonMap("830000", CredentialRegionInfos.TAIWAN_RESIDENCE_PERMIT);

    /**
     * 构造器
     */
    public TaiwanResidencePermitProcessor() {
        super(PATTERN, PREFIX_REGIONS);
    }

    /**
     * 获取台湾居民居住证信息
     *
     * @return 台湾居民居住证信息
     */
    @Override
    protected TaiwanResidencePermitInfo createInfo() {
        return new TaiwanResidencePermitInfo();
    }
}
