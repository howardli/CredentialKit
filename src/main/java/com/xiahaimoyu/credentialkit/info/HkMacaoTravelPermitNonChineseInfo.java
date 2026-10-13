/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;


import java.util.Objects;

/**
 * 港澳居民来往内地通行证（非中国籍）信息
 *
 * @author Howard.Li
 */
public final class HkMacaoTravelPermitNonChineseInfo extends CredentialInfo {

    /**
     * 地区
     */
    private DomesticRegionInfo region;

    /**
     * 获取地区
     *
     * @return 地区
     */
    public DomesticRegionInfo getRegion() {
        return region;
    }

    /**
     * 设置地区
     *
     * @param region 地区
     */
    public void setRegion(DomesticRegionInfo region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HkMacaoTravelPermitNonChineseInfo that = (HkMacaoTravelPermitNonChineseInfo) o;
        return Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region);
    }

    @Override
    public String toString() {
        return "HkMacaoTravelPermitNonChineseInfo{" +
                "region=" + region +
                '}';
    }
}
