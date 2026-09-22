/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.OrgCategory;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.UnifiedSocialCreditInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.xiahaimoyu.credentialkit.processor.ValidationResult.validIf;

/**
 * 统一社会信用代码处理器
 *
 * @author Howard.Li
 */
public class UnifiedSocialCreditProcessor extends CredentialProcessor<UnifiedSocialCreditInfo> {

    /**
     * 基础校验正则
     * <p>
     * 统一社会信用代码字符集为0-9和除I、O、S、V、Z外的大写字母共31个字符（GB/T 32100-2015），
     * 字符集之外的输入在此处直接判为基本格式错误，避免后续校验位计算因非法字符抛出异常。
     * 前2位为登记管理部门和机构类别代码，第3-8位为地区代码（必须为数字），
     * 第9-17位为组织机构代码（末位为数字或X的校验位），第18位为统一社会信用代码校验位。
     * </p>
     */
    private static final Pattern PATTERN = Pattern.compile("^[0-9ABCDEFGHJKLMNPQRTUWXY]{2}\\d{6}[0-9ABCDEFGHJKLMNPQRTUWXY]{8}[0-9X][0-9ABCDEFGHJKLMNPQRTUWXY]$");

    /**
     * 构造器
     */
    public UnifiedSocialCreditProcessor() {
        super(
                Arrays.asList(
                        // 基本格式校验（null规格化后为空字符串，长度校验必然失败）
                        credential -> validIf(
                                credential.length() == 18 && PATTERN.matcher(credential).matches(),
                                ErrorCode.BASIC_FORMAT_ERROR),
                        // 校验机构类型
                        credential -> validIf(
                                OrgCategory.getByCode(credential.substring(0, 2)) != null,
                                ErrorCode.ORG_CATEGORY_ERROR),
                        // 校验首次签发地区
                        credential -> validIf(
                                RegionUtil.getDomesticRegionInfoByCode(credential.substring(2, 8)) != null,
                                ErrorCode.REGION_ERROR),
                        // 校验组织机构代码校验位
                        credential -> validIf(
                                CheckDigitUtil.getOrganizationCodeCheckDigit(credential.substring(8, 16)) == credential.charAt(16),
                                ErrorCode.CHECK_DIGIT_ERROR),
                        // 校验统一社会信用代码校验位
                        credential -> validIf(
                                CheckDigitUtil.getUnifiedSocialCreditCodeCheckDigit(credential.substring(0, 17)) == credential.charAt(17),
                                ErrorCode.CHECK_DIGIT_ERROR)
                ),
                Arrays.asList(
                        // 解析机构类型
                        (credential, info) -> {
                            info.setOrgCategory(OrgCategory.getByCode(credential.substring(0, 2)));
                        },
                        // 解析首次签发地区
                        (credential, info) -> {
                            DomesticRegionInfo region = RegionUtil.getDomesticRegionInfoByCode(credential.substring(2, 8));
                            info.setRegion(region);
                        },
                        // 解析组织机构代码
                        (credential, info) -> {
                            info.setOrganizationCode(credential.substring(8, 17));
                        }
                )
        );
    }

    /**
     * 获取统一社会信用代码信息
     *
     * @return 统一社会信用代码信息
     */
    @Override
    protected UnifiedSocialCreditInfo createInfo() {
        return new UnifiedSocialCreditInfo();
    }
}
