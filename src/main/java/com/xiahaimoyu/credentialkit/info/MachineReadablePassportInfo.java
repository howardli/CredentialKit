/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */

package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.util.DateUtil;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 可机读护照信息
 *
 * @author Howard.Li
 */
public final class MachineReadablePassportInfo extends CredentialInfo {

    /**
     * 签发地区
     */
    private InternationalRegionInfo issuingRegion;

    /**
     * 姓（名字的主要部分）
     */
    private String surname;

    /**
     * 名（名字的次要部分）
     */
    private String givenName;

    /**
     * 护照号码
     */
    private String passportNumber;

    /**
     * 地区
     */
    private InternationalRegionInfo region;

    /**
     * 生日（YYMMDD格式）
     */
    private String birthDate;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 过期时间（YYMMDD格式）
     */
    private String expirationDate;

    /**
     * 个人号码
     */
    private String personalNumber;

    /**
     * 获取签发地区
     *
     * @return 签发地区
     */
    public InternationalRegionInfo getIssuingRegion() {
        return issuingRegion;
    }

    /**
     * 设置签发地区
     *
     * @param issuingRegion 签发地区
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setIssuingRegion(InternationalRegionInfo issuingRegion) {
        this.issuingRegion = issuingRegion;
        return this;
    }

    /**
     * 获取姓（名字的主要部分）
     *
     * @return 姓（名字的主要部分）
     */
    public String getSurname() {
        return surname;
    }

    /**
     * 设置姓（名字的主要部分）
     *
     * @param surname 姓（名字的主要部分）
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    /**
     * 获取名（名字的次要部分）
     *
     * @return 名（名字的次要部分）
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * 设置名（名字的次要部分）
     *
     * @param givenName 名（名字的次要部分）
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    /**
     * 获取护照号码
     *
     * @return 护照号码
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * 设置护照号码
     *
     * @param passportNumber 护照号码
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    /**
     * 获取地区
     *
     * @return 地区
     */
    public InternationalRegionInfo getRegion() {
        return region;
    }

    /**
     * 设置地区
     *
     * @param region 地区
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setRegion(InternationalRegionInfo region) {
        this.region = region;
        return this;
    }

    /**
     * 获取生日（YYMMDD格式）
     *
     * @return 生日（YYMMDD格式）
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * 设置生日（YYMMDD格式）
     *
     * @param birthDate 生日（YYMMDD格式）
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setBirthDate(String birthDate) {
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
    public MachineReadablePassportInfo setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    /**
     * 获取过期时间（YYMMDD格式）
     *
     * @return 过期时间（YYMMDD格式）
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * 设置过期时间（YYMMDD格式）
     *
     * @param expirationDate 过期时间（YYMMDD格式）
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    /**
     * 获取个人号码
     *
     * @return 个人号码
     */
    public String getPersonalNumber() {
        return personalNumber;
    }

    /**
     * 设置个人号码
     *
     * @param personalNumber 个人号码
     * @return this（链式调用）
     */
    public MachineReadablePassportInfo setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
        return this;
    }

    /**
     * 获取年龄（基于YYMMDD格式生日）
     *
     * @return 年龄，如果生日无效则返回-1
     */
    public int getAge() {
        LocalDate birth = DateUtil.parseYYMMDD(birthDate);
        return DateUtil.calculateAge(birth);
    }

    /**
     * 判断是否成年（18岁以上）
     *
     * @return 是否成年
     */
    public boolean isAdult() {
        LocalDate birth = DateUtil.parseYYMMDD(birthDate);
        return DateUtil.isAdult(birth);
    }

    /**
     * 获取生日（LocalDate格式）
     *
     * @return 生日，如果解析失败则返回null
     */
    public LocalDate getBirthDateAsLocalDate() {
        return DateUtil.parseYYMMDD(birthDate);
    }

    /**
     * 获取生日（YYYY-MM-DD格式）
     *
     * @return 生日字符串，如果无效则返回null
     */
    public String getBirthDateFormatted() {
        return DateUtil.formatYYMMDDToIsoDate(birthDate);
    }

    /**
     * 判断护照是否过期
     *
     * @return 是否过期
     */
    public boolean isExpired() {
        LocalDate expiration = DateUtil.parseYYMMDD(expirationDate);
        if (expiration == null) {
            return false;
        }
        return expiration.isBefore(LocalDate.now());
    }

    /**
     * 获取过期日期（LocalDate格式）
     *
     * @return 过期日期，如果解析失败则返回null
     */
    public LocalDate getExpirationDateAsLocalDate() {
        return DateUtil.parseYYMMDD(expirationDate);
    }

    /**
     * 获取过期日期（YYYY-MM-DD格式）
     *
     * @return 过期日期字符串，如果无效则返回null
     */
    public String getExpirationDateFormatted() {
        return DateUtil.formatYYMMDDToIsoDate(expirationDate);
    }

    /**
     * 获取持有人全名
     *
     * @return 全名（姓 + 名）
     */
    public String getFullName() {
        if (surname == null) {
            return givenName;
        }
        if (givenName == null) {
            return surname;
        }
        return surname + " " + givenName;
    }

    /**
     * 获取签发地区中文名称
     *
     * @return 签发地区中文名称
     */
    public String getIssuingRegionChineseName() {
        return issuingRegion != null ? issuingRegion.getChineseShortName() : null;
    }

    /**
     * 获取签发地区英文名称
     *
     * @return 签发地区英文名称
     */
    public String getIssuingRegionEnglishName() {
        return issuingRegion != null ? issuingRegion.getEnglishShortName() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineReadablePassportInfo that = (MachineReadablePassportInfo) o;
        return Objects.equals(issuingRegion, that.issuingRegion)
                && Objects.equals(surname, that.surname)
                && Objects.equals(givenName, that.givenName)
                && Objects.equals(passportNumber, that.passportNumber)
                && Objects.equals(region, that.region)
                && Objects.equals(birthDate, that.birthDate)
                && gender == that.gender
                && Objects.equals(expirationDate, that.expirationDate)
                && Objects.equals(personalNumber, that.personalNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuingRegion, surname, givenName, passportNumber,
                region, birthDate, gender, expirationDate, personalNumber);
    }

    @Override
    public String toString() {
        return "MachineReadablePassportInfo{" +
                "passportNumber='" + passportNumber + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", issuingRegion='" + getIssuingRegionEnglishName() + '\'' +
                ", age=" + getAge() +
                ", expired=" + isExpired() +
                '}';
    }
}
