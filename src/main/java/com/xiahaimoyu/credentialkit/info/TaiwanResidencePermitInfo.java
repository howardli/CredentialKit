/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.enums.Gender;

import java.util.Objects;

/**
 * 台湾居民居住证信息
 *
 * @author Howard.Li
 */
public final class TaiwanResidencePermitInfo extends CredentialInfo {

    /**
     * 地区（固定为台湾地区）
     */
    private DomesticRegionInfo region;

    /**
     * 生日（YYYYMMDD格式）
     */
    private String birthDate;

    /**
     * 性别
     */
    private Gender gender;

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

    /**
     * 获取证件类型
     *
     * @return 台湾居民居住证类型
     */
    @Override
    public CredentialType getType() {
        return DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiwanResidencePermitInfo that = (TaiwanResidencePermitInfo) o;
        return Objects.equals(region, that.region)
                && Objects.equals(birthDate, that.birthDate)
                && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, birthDate, gender);
    }

    @Override
    public String toString() {
        return "TaiwanResidencePermitInfo{" +
                "region=" + region +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                '}';
    }
}