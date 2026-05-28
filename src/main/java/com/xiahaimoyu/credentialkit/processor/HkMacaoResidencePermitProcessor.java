/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.constant.RegionConstant;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.HkMacaoResidencePermitInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 港澳居民居住证处理器
 *
 * @author Howard.Li
 */
public class HkMacaoResidencePermitProcessor extends CredentialProcessor<HkMacaoResidencePermitInfo> {

    /**
     * 香港地区信息常量（避免重复创建）
     */
    private static final DomesticRegionInfo HONG_KONG_REGION =
        new DomesticRegionInfo("810000", RegionConstant.HONG_KONG, null, null);

    /**
     * 澳门地区信息常量（避免重复创建）
     */
    private static final DomesticRegionInfo MACAO_REGION =
        new DomesticRegionInfo("820000", RegionConstant.MACAO, null, null);

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^8[12]0000\\d{11}[0-9X]$");

    /**
     * 构造校验器列表
     *
     * @return 校验器列表
     */
    @Override
    protected List<CredentialValidator> buildValidators() {
        return Arrays.asList(
                // 基本格式校验
                credential -> {
                    if (credential == null || credential.length() != 18 || !PATTERN.matcher(credential).matches()) {
                        return ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR);
                    }
                    return ValidationResult.success();
                },
                // 校验生日
                credential -> {
                    String birthDate = credential.substring(6, 14);
                    if (!DateUtil.validDateBeforeNow(birthDate)) {
                        return ValidationResult.failure(ErrorCode.BIRTH_DATE_ERROR);
                    }
                    return ValidationResult.success();
                },
                // 校验校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getIdCardCheckDigit(credential.substring(0, 17));
                    if (checkDigit != credential.charAt(17)) {
                        return ValidationResult.failure(ErrorCode.CHECK_DIGIT_ERROR);
                    }
                    return ValidationResult.success();
                }
        );
    }

    /**
     * 构造解析器列表
     *
     * @return 解析器列表
     */
    @Override
    protected List<CredentialParser<HkMacaoResidencePermitInfo>> buildParsers() {
        return Arrays.asList(
                // 解析地区
                (credential, info) -> {
                    String regionCode = credential.substring(0, 6);
                    if (regionCode.equals("810000")) {
                        info.setRegion(HONG_KONG_REGION);
                    } else if (regionCode.equals("820000")) {
                        info.setRegion(MACAO_REGION);
                    }
                },
                // 解析生日
                (credential, info) -> {
                    String birthday = credential.substring(6, 14);
                    info.setBirthDate(birthday);
                },
                // 解析性别
                (credential, info) -> {
                    int genderDigit = credential.charAt(16) - '0';
                    info.setGender(Gender.fromDigit(genderDigit));
                }
        );
    }

    /**
     * 获取港澳居民居住证信息
     *
     * @return 港澳居民居住证信息
     */
    @Override
    protected HkMacaoResidencePermitInfo createInfo() {
        return new HkMacaoResidencePermitInfo();
    }
}
