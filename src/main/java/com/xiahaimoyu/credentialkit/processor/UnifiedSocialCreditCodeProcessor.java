/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.OrgCategory;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.UnifiedSocialCreditCodeInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 统一社会信用代码处理器
 *
 * @author Howard.Li
 */
public class UnifiedSocialCreditCodeProcessor extends CredentialProcessor<UnifiedSocialCreditCodeInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^\\d{8}[0-9A-Z]{8}[0-9X][0-9A-Z]$");

    /**
     * 构造校验器列表
     *
     * @return 校验器列表
     */
    @Override
    protected List<CredentialValidator> buildValidators() {
        return Arrays.asList(
                //基本格式校验
                credential -> {
                    if (credential == null || credential.length() != 18 || !PATTERN.matcher(credential).matches()) {
                        throw CredentialException.of(ErrorCode.BASIC_FORMAT_ERROR, "基本格式校验失败：{0}", credential);
                    }
                },
                //校验机构类型
                credential -> {
                    String orgCategoryCode = credential.substring(0, 2);
                    if (OrgCategory.getByCode(orgCategoryCode) == null) {
                        throw CredentialException.of(ErrorCode.ORG_CATEGORY_ERROR, "机构类型校验失败：{0}", orgCategoryCode);
                    }
                },
                //校验首次签发地区
                credential -> {
                    String regionCode = credential.substring(2, 8);
                    if (RegionUtil.getDomesticRegionInfoByCode(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "签发地区不对：{0}", regionCode);
                    }
                },
                //校验组织机构代码校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getOrganizationCodeCheckDigit(credential.substring(8, 16));
                    if (checkDigit != credential.charAt(16)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(16));
                    }
                },
                //校验统一社会信用代码校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getUnifiedSocialCreditCodeCheckDigit(credential.substring(0, 17));
                    if (checkDigit != credential.charAt(17)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(17));
                    }
                }
        );
    }

    /**
     * 构造解析器列表
     *
     * @return 解析器列表
     */
    @Override
    protected List<CredentialParser<UnifiedSocialCreditCodeInfo>> buildParsers() {
        return Arrays.asList(
                //解析机构类型
                (credential, info) -> {
                    info.setOrgCategory(OrgCategory.getByCode(credential.substring(0, 2)));
                },
                //解析首次签发地区
                (credential, info) -> {
                    DomesticRegionInfo region = RegionUtil.getDomesticRegionInfoByCode(credential.substring(2, 8));
                    info.setRegion(region);
                },
                //解析组织机构代码
                (credential, info) -> {
                    info.setOrganizationCode(credential.substring(8, 17));
                }
        );
    }

    /**
     * 获取统一社会信用代码信息
     *
     * @return 统一社会信用代码信息
     */
    @Override
    protected UnifiedSocialCreditCodeInfo getInfo() {
        return new UnifiedSocialCreditCodeInfo();
    }
}
