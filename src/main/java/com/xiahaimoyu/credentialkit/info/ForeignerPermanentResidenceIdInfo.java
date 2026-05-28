/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.util.DateUtil;

import java.time.LocalDate;
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
     * @return this（链式调用）
     */
    public ForeignerPermanentResidenceIdInfo setInternationalRegionInfo(InternationalRegionInfo internationalRegionInfo) {
        this.internationalRegionInfo = internationalRegionInfo;
        return this;
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
     * @return this（链式调用）
     */
    public ForeignerPermanentResidenceIdInfo setDomesticRegionInfo(DomesticRegionInfo domesticRegionInfo) {
        this.domesticRegionInfo = domesticRegionInfo;
        return this;
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
     * @return this（链式调用）
     */
    public ForeignerPermanentResidenceIdInfo setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
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
     * @return this（链式调用）
     */
    public ForeignerPermanentResidenceIdInfo setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    /**
     * 获取年龄
     *
     * @return 年龄，如果生日无效则返回-1
     */
    public int getAge() {
        return DateUtil.calculateAge(birthDate);
    }

    /**
     * 判断是否成年（18岁以上）
     *
     * @return 是否成年
     */
    public boolean isAdult() {
        return DateUtil.isAdult(birthDate);
    }

    /**
     * 获取生日（LocalDate格式）
     *
     * @return 生日，如果解析失败则返回null
     */
    public LocalDate getBirthDateAsLocalDate() {
        if (birthDate == null) {
            return null;
        }
        try {
            return LocalDate.parse(birthDate, java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 获取生日（YYYY-MM-DD格式）
     *
     * @return 生日字符串，如果无效则返回null
     */
    public String getBirthDateFormatted() {
        return DateUtil.formatToIsoDate(birthDate);
    }

    /**
     * 获取国籍中文名称
     *
     * @return 国籍中文名称，如果国籍信息为空则返回null
     */
    public String getNationalityChineseName() {
        return internationalRegionInfo != null ? internationalRegionInfo.getChineseShortName() : null;
    }

    /**
     * 获取国籍英文名称
     *
     * @return 国籍英文名称，如果国籍信息为空则返回null
     */
    public String getNationalityEnglishName() {
        return internationalRegionInfo != null ? internationalRegionInfo.getEnglishShortName() : null;
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
                "nationality='" + getNationalityChineseName() + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                ", age=" + getAge() +
                '}';
    }
}
