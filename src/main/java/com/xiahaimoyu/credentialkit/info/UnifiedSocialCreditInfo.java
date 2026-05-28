/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.OrgCategory;

import java.util.Objects;

/**
 * 统一社会信用代码信息
 *
 * @author Howard.Li
 */
public final class UnifiedSocialCreditInfo extends CredentialInfo {

    /**
     * 机构类别
     */
    private OrgCategory orgCategory;

    /**
     * 首次签发地区
     */
    private DomesticRegionInfo region;

    /**
     * 组织机构代码
     */
    private String organizationCode;

    /**
     * 获取机构类别
     *
     * @return 机构类别
     */
    public OrgCategory getOrgCategory() {
        return orgCategory;
    }

    /**
     * 设置机构类别
     *
     * @param orgCategory 机构类别
     * @return this（链式调用）
     */
    public UnifiedSocialCreditInfo setOrgCategory(OrgCategory orgCategory) {
        this.orgCategory = orgCategory;
        return this;
    }

    /**
     * 获取首次签发地区
     *
     * @return 首次签发地区
     */
    public DomesticRegionInfo getRegion() {
        return region;
    }

    /**
     * 设置首次签发地区
     *
     * @param region 首次签发地区
     * @return this（链式调用）
     */
    public UnifiedSocialCreditInfo setRegion(DomesticRegionInfo region) {
        this.region = region;
        return this;
    }

    /**
     * 获取组织机构代码
     *
     * @return 组织机构代码
     */
    public String getOrganizationCode() {
        return organizationCode;
    }

    /**
     * 设置组织机构代码
     *
     * @param organizationCode 组织机构代码
     * @return this（链式调用）
     */
    public UnifiedSocialCreditInfo setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
        return this;
    }

    /**
     * 获取机构类别中文名称
     *
     * @return 机构类别中文名称，如果机构类别为空则返回null
     */
    public String getOrgCategoryChineseName() {
        return orgCategory != null ? orgCategory.getDesc() : null;
    }

    /**
     * 获取省份名称
     *
     * @return 省份名称，如果地区信息为空则返回null
     */
    public String getProvinceName() {
        return region != null ? region.getProvince() : null;
    }

    /**
     * 获取城市名称
     *
     * @return 埂市名称，如果地区信息为空则返回null
     */
    public String getCityName() {
        return region != null ? region.getCity() : null;
    }

    /**
     * 获取区县名称
     *
     * @return 区县名称，如果地区信息为空则返回null
     */
    public String getCountyName() {
        return region != null ? region.getCounty() : null;
    }

    /**
     * 判断是否为企业
     *
     * @return 是否为企业（机构类别代码以91开头）
     */
    public boolean isEnterprise() {
        return orgCategory != null && orgCategory.getCode().startsWith("9");
    }

    /**
     * 判断是否为事业单位
     *
     * @return 是否为事业单位（机构类别代码以1开头）
     */
    public boolean isPublicInstitution() {
        return orgCategory != null && orgCategory.getCode().startsWith("1");
    }

    /**
     * 判断是否为社会团体
     *
     * @return 是否为社会团体（机构类别代码以5开头）
     */
    public boolean isSocialOrganization() {
        return orgCategory != null && orgCategory.getCode().startsWith("5");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnifiedSocialCreditInfo that = (UnifiedSocialCreditInfo) o;
        return orgCategory == that.orgCategory
                && Objects.equals(region, that.region)
                && Objects.equals(organizationCode, that.organizationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgCategory, region, organizationCode);
    }

    @Override
    public String toString() {
        return "UnifiedSocialCreditInfo{" +
                "orgCategory='" + getOrgCategoryChineseName() + '\'' +
                ", province='" + getProvinceName() + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                '}';
    }
}
