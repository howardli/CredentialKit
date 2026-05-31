/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
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
     */
    public void setOrgCategory(OrgCategory orgCategory) {
        this.orgCategory = orgCategory;
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
     */
    public void setRegion(DomesticRegionInfo region) {
        this.region = region;
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
     */
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    /**
     * 获取证件类型
     *
     * @return 统一社会信用代码类型
     */
    @Override
    public CredentialType getType() {
        return DefaultCredentialType.UNIFIED_SOCIAL_CREDIT;
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
                "orgCategory=" + orgCategory +
                ", region=" + region +
                ", organizationCode='" + organizationCode + '\'' +
                '}';
    }
}