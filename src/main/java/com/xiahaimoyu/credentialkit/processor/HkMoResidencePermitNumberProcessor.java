/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.constant.RegionConstant;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.HkMoResidencePermitNumberInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 港澳居民居住证号码处理器
 *
 * @author Howard.Li
 */
public class HkMoResidencePermitNumberProcessor extends CredentialProcessor<HkMoResidencePermitNumberInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^8[1|2]0000\\d{11}[0-9X]$");

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
                //校验生日
                credential -> {
                    String birthDate = credential.substring(6, 14);
                    if (!DateUtil.validDateBeforeNow(birthDate)) {
                        throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                    }
                },
                //校验校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getIdCardCheckDigit(credential.substring(0, 17));
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
    protected List<CredentialParser<HkMoResidencePermitNumberInfo>> buildParsers() {
        return Arrays.asList(
                //解析地区
                (credential, info) -> {
                    String regionCode = credential.substring(0, 6);
                    if (regionCode.equals("810000")) {
                        info.setRegion(new DomesticRegionInfo(regionCode, RegionConstant.HONG_KONG, null, null));
                    } else if (regionCode.equals("820000")) {
                        info.setRegion(new DomesticRegionInfo(regionCode, RegionConstant.MACAO, null, null));
                    }
                },
                //解析生日
                (credential, info) -> {
                    String birthday = credential.substring(6, 14);
                    info.setBirthDate(birthday);
                },
                //解析性别
                (credential, info) -> {
                    int genderDigit = -1;
                    genderDigit = credential.charAt(16) - '0';
                    Gender gender = (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
                    info.setGender(gender);
                }
        );
    }

    /**
     * 获取港澳居民居住证号码信息
     *
     * @return 港澳居民居住证号码信息
     */
    @Override
    protected HkMoResidencePermitNumberInfo getInfo() {
        return new HkMoResidencePermitNumberInfo();
    }
}
