/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.util.DateUtil;

import java.time.LocalDate;
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
     * @return this（链式调用）
     */
    public TaiwanResidencePermitInfo setRegion(DomesticRegionInfo region) {
        this.region = region;
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
    public TaiwanResidencePermitInfo setBirthDate(String birthDate) {
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
    public TaiwanResidencePermitInfo setGender(Gender gender) {
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
                ", age=" + getAge() +
                '}';
    }
}
