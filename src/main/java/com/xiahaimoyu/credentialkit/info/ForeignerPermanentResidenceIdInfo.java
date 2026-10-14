/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import java.util.Objects;

/**
 * 外国人永久居留身份证信息
 * <p>
 * 生日与性别继承自{@link PersonCredentialInfo}。
 * </p>
 *
 * @author Howard.Li
 */
public final class ForeignerPermanentResidenceIdInfo extends PersonCredentialInfo {

    /**
     * 首次签发的国籍
     */
    private InternationalRegionInfo internationalRegionInfo;

    /**
     * 首次签发的地区
     */
    private DomesticRegionInfo domesticRegionInfo;

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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        ForeignerPermanentResidenceIdInfo that = (ForeignerPermanentResidenceIdInfo) o;
        return Objects.equals(internationalRegionInfo, that.internationalRegionInfo)
                && Objects.equals(domesticRegionInfo, that.domesticRegionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internationalRegionInfo, domesticRegionInfo, getBirthDate(), getGender());
    }

    @Override
    public String toString() {
        return "ForeignerPermanentResidenceIdInfo{" +
                "internationalRegionInfo=" + internationalRegionInfo +
                ", domesticRegionInfo=" + domesticRegionInfo +
                ", birthDate='" + getBirthDate() + '\'' +
                ", gender=" + getGender() +
                '}';
    }
}
