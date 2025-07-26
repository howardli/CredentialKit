/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */

package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.Gender;

/**
 * 可机读护照编码信息
 *
 * @author Howard.Li
 */
public class MachineReadablePassportCodeInfo extends CredentialInfo {

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
    private String birthdate;

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
     */
    public void setIssuingRegion(InternationalRegionInfo issuingRegion) {
        this.issuingRegion = issuingRegion;
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
     */
    public void setSurname(String surname) {
        this.surname = surname;
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
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
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
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
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
     */
    public void setRegion(InternationalRegionInfo region) {
        this.region = region;
    }

    /**
     * 获取生日（YYMMDD格式）
     *
     * @return 生日（YYMMDD格式）
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * 设置生日（YYMMDD格式）
     *
     * @param birthdate 生日（YYMMDD格式）
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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
     */
    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }
}
