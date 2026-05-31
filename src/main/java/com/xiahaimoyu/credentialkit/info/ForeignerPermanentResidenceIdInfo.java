/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.enums.Gender;

import java.util.Objects;

/**
 * 外国人永久居留身份证信息
 *
 * @author Howard.Li
 */
public final class ForeignerPermanentResidenceIdInfo extends CredentialInfo {

    /**
     * 首次签发的国籍
     */
    private InternationalRegionInfo internationalRegionInfo;

    /**
     * 首次签发的地区
     */
    private DomesticRegionInfo domesticRegionInfo;

    /**
     * 生日
     * <p>
     * 15位证件：YYMMDD格式（6位）
     * 18位证件：YYYYMMDD格式（8位）
     * </p>
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
     * 获取生日
     * <p>
     * 15位证件：YYMMDD格式（6位）
     * 18位证件：YYYYMMDD格式（8位）
     * </p>
     *
     * @return 生日
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * 设置生日
     * <p>
     * 15位证件：YYMMDD格式（6位）
     * 18位证件：YYYYMMDD格式（8位）
     * </p>
     *
     * @param birthDate 生日
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

    /**
     * 获取证件类型
     *
     * @return 外国人永久居留身份证类型
     */
    @Override
    public CredentialType getType() {
        return DefaultCredentialType.FOREIGNER_PERMANENT_RESIDENCE_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeignerPermanentResidenceIdInfo that = (ForeignerPermanentResidenceIdInfo) o;
        return Objects.equals(internationalRegionInfo, that.internationalRegionInfo)
                && Objects.equals(domesticRegionInfo, that.domesticRegionInfo)
                && Objects.equals(birthDate, that.birthDate)
                && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(internationalRegionInfo, domesticRegionInfo, birthDate, gender);
    }

    @Override
    public String toString() {
        return "ForeignerPermanentResidenceIdInfo{" +
                "internationalRegionInfo=" + internationalRegionInfo +
                ", domesticRegionInfo=" + domesticRegionInfo +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                '}';
    }
}