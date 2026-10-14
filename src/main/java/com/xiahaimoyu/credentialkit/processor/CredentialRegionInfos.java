/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.constant.RegionConstant;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;

/**
 * 内置证件处理器的港澳台地区信息常量
 * <p>
 * 港澳台往来/居住证件使用的编码（H/HA/810000等证件专用编码）不在GB/T 2260
 * 行政区划数据内，处理器需要的固定地区信息在此集中定义，避免各处理器重复构造。
 * 包内可见，不属于公共API。
 * </p>
 *
 * @author Howard.Li
 */
final class CredentialRegionInfos {

    /**
     * 港澳居民来往内地通行证（香港，H字头）
     */
    static final DomesticRegionInfo HONG_KONG_TRAVEL_PERMIT =
            new DomesticRegionInfo("H", RegionConstant.HONG_KONG, null, null);

    /**
     * 港澳居民来往内地通行证（澳门，M字头）
     */
    static final DomesticRegionInfo MACAO_TRAVEL_PERMIT =
            new DomesticRegionInfo("M", RegionConstant.MACAO, null, null);

    /**
     * 港澳居民来往内地通行证（非中国籍，香港，HA字头）
     */
    static final DomesticRegionInfo HONG_KONG_TRAVEL_PERMIT_NON_CHINESE =
            new DomesticRegionInfo("HA", RegionConstant.HONG_KONG, null, null);

    /**
     * 港澳居民来往内地通行证（非中国籍，澳门，MA字头）
     */
    static final DomesticRegionInfo MACAO_TRAVEL_PERMIT_NON_CHINESE =
            new DomesticRegionInfo("MA", RegionConstant.MACAO, null, null);

    /**
     * 港澳居民居住证（香港，810000）
     */
    static final DomesticRegionInfo HONG_KONG_RESIDENCE_PERMIT =
            new DomesticRegionInfo("810000", RegionConstant.HONG_KONG, null, null);

    /**
     * 港澳居民居住证（澳门，820000）
     */
    static final DomesticRegionInfo MACAO_RESIDENCE_PERMIT =
            new DomesticRegionInfo("820000", RegionConstant.MACAO, null, null);

    /**
     * 台湾居民居住证（台湾，830000）
     */
    static final DomesticRegionInfo TAIWAN_RESIDENCE_PERMIT =
            new DomesticRegionInfo("830000", RegionConstant.TAIWAN, null, null);

    /**
     * 私有构造函数，防止实例化
     */
    private CredentialRegionInfos() {
    }
}
