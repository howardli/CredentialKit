/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import java.util.Objects;

/**
 * 国内地区信息
 *
 * @author Howard.Li
 */
public final class DomesticRegionInfo extends CredentialInfo {

    /**
     * 地区编码（6位数字）
     */
    private final String code;

    /**
     * 省份
     */
    private final String province;

    /**
     * 城市
     */
    private final String city;

    /**
     * 县区
     */
    private final String county;

    /**
     * 构造函数
     *
     * @param code    地区编码（6位数字）
     * @param province 省份
     * @param city     城市
     * @param county   县区
     */
    public DomesticRegionInfo(String code, String province, String city, String county) {
        this.code = code;
        this.province = province;
        this.city = city;
        this.county = county;
    }

    /**
     * 获取地区编码
     *
     * @return 地区编码（6位数字）
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取省份
     *
     * @return 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 获取城市
     *
     * @return 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 获取县区
     *
     * @return 县区
     */
    public String getCounty() {
        return county;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomesticRegionInfo that = (DomesticRegionInfo) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "DomesticRegionInfo{" +
                "code='" + code + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                '}';
    }
}
