/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.Gender;

import java.util.Objects;

/**
 * 自然人证件信息基类
 * <p>
 * 持有生日和性别两个自然人公共属性，供身份证、居住证、永居证、护照等
 * 面向自然人的证件信息类继承。equals按运行时类和本类字段比较，
 * 子类相等性在本类基础上叠加各自字段。
 * </p>
 *
 * @author Howard.Li
 */
public abstract class PersonCredentialInfo extends CredentialInfo {

    /**
     * 生日（YYYYMMDD格式）
     */
    private String birthDate;

    /**
     * 性别
     */
    private Gender gender;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonCredentialInfo that = (PersonCredentialInfo) o;
        return Objects.equals(birthDate, that.birthDate)
                && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthDate, gender);
    }
}
