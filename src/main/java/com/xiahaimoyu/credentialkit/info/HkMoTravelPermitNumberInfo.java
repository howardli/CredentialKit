/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

/**
 * 港澳居民来往内地通行证号码信息
 *
 * @author Howard.Li
 */
public class HkMoTravelPermitNumberInfo extends CredentialInfo {

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
     */
    public int getReplacementTime() {
        return replacementTime;
    }

    /**
     * 设置换证次数
     */
    public void setReplacementTime(int replacementTime) {
        this.replacementTime = replacementTime;
    }

}
