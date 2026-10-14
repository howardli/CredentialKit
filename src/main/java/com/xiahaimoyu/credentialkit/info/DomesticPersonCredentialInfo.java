/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import java.util.Objects;

/**
 * 含国内地区的自然人证件信息基类
 * <p>
 * 在{@link PersonCredentialInfo}的生日、性别之上增加签发/居住地区，
 * 供身份证和港澳台居民居住证等使用国内地区编码的证件信息类继承。
 * equals/hashCode/toString已覆盖全部字段，具体子类通常只需声明类型与语义。
 * </p>
 *
 * @author Howard.Li
 */
public abstract class DomesticPersonCredentialInfo extends PersonCredentialInfo {

    /**
     * 地区
     */
    private DomesticRegionInfo region;

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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        return Objects.equals(region, ((DomesticPersonCredentialInfo) o).region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, getBirthDate(), getGender());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{region=" + region
                + ", birthDate='" + getBirthDate() + '\''
                + ", gender=" + getGender() + '}';
    }
}
