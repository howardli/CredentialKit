/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.Gender;

/**
 * 外国人永久居留身份证号码信息
 *
 * @author Howard.Li
 */
public class ForeignerPermanentResidenceIdNumberInfo extends CredentialInfo {

    /**
     * 首次签发的国籍
     */
    private InternationalRegionInfo internationalRegionInfo;

    /**
     * 首次签发的地区
     */
    private DomesticRegionInfo domesticRegionInfo;

    /**
     * 生日（YYYYMMDD格式）
     */
    private String birthDate;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 获取首次签发的国籍
     *
     * @return 首次签发的国籍
     */
    public InternationalRegionInfo getInternationalRegionInfo() {
        return internationalRegionInfo;
    }

    /**
     * 设置首次签发的国籍
     *
     * @param internationalRegionInfo 首次签发的国籍
     */
    public void setInternationalRegionInfo(InternationalRegionInfo internationalRegionInfo) {
        this.internationalRegionInfo = internationalRegionInfo;
    }

    /**
     * 获取首次签发的地区
     *
     * @return 首次签发的地区
     */
    public DomesticRegionInfo getDomesticRegionInfo() {
        return domesticRegionInfo;
    }

    /**
     * 设置首次签发的地区
     *
     * @param domesticRegionInfo 首次签发的地区
     */
    public void setDomesticRegionInfo(DomesticRegionInfo domesticRegionInfo) {
        this.domesticRegionInfo = domesticRegionInfo;
    }

    /**
     * 获取生日（YYYYMMDD格式）
     *
     * @return 生日（YYYYMMDD格式）
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * 设置生日（YYYYMMDD格式）
     *
     * @param birthDate 生日（YYYYMMDD格式）
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * 获取性别
     *
     * @return 性别
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
