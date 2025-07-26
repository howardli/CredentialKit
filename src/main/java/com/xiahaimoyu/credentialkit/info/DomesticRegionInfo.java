/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

/**
 * 国内地区信息
 *
 * @author Howard.Li
 */
public class DomesticRegionInfo extends CredentialInfo {

    private final String code;

    private final String province;

    private final String city;

    private final String county;

    public DomesticRegionInfo(String code, String province, String city, String county) {
        this.code = code;
        this.province = province;
        this.city = city;
        this.county = county;
    }

    public String getCode() {
        return code;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }
}
