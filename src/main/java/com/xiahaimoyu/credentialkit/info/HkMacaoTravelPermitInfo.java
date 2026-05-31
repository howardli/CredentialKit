/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;

import java.util.Objects;

/**
 * 港澳居民来往内地通行证信息
 *
 * @author Howard.Li
 */
public final class HkMacaoTravelPermitInfo extends CredentialInfo {

    /**
     * 地区
     */
    private DomesticRegionInfo region;

    /**
     * 换证次数（未知是-1）
     */
    private int replacementTime = -1;

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

    /**
     * 获取换证次数
     *
     * @return 换证次数（未知是-1）
     */
    public int getReplacementTime() {
        return replacementTime;
    }

    /**
     * 设置换证次数
     *
     * @param replacementTime 换证次数
     */
    public void setReplacementTime(int replacementTime) {
        this.replacementTime = replacementTime;
    }

    /**
     * 获取证件类型
     *
     * @return 港澳居民来往内地通行证类型
     */
    @Override
    public CredentialType getType() {
        return DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HkMacaoTravelPermitInfo that = (HkMacaoTravelPermitInfo) o;
        return replacementTime == that.replacementTime
                && Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, replacementTime);
    }

    @Override
    public String toString() {
        return "HkMacaoTravelPermitInfo{" +
                "region=" + region +
                ", replacementTime=" + replacementTime +
                '}';
    }
}