/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdNumberInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 中华人民共和国居民身份证号码处理器
 *
 * @author Howard.Li
 */
public class MainlandResidentIdNumberProcessor extends CredentialProcessor<MainlandResidentIdNumberInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^(\\d{17}[0-9X])|(\\d{15})$");

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
                    if (credential == null || (credential.length() != 18 && credential.length() != 15) || !PATTERN.matcher(credential).matches()) {
                        throw CredentialException.of(ErrorCode.BASIC_FORMAT_ERROR, "基本格式校验失败：{0}", credential);
                    }
                },
                //校验首次签发地区
                credential -> {
                    String regionCode = credential.substring(0, 6);
                    if (RegionUtil.getDomesticRegionInfoByCode(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "签发地区不对：{0}", regionCode);
                    }
                },
                //校验生日
                credential -> {
                    String birthDate = null;
                    try {
                        if (isNew(credential)) {
                            birthDate = credential.substring(6, 14);
                        } else {
                            birthDate = "19" + credential.substring(6, 12);
                        }
                        if (!DateUtil.validDateBeforeNow(birthDate)) {
                            throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                        }
                    } catch (DateTimeParseException e) {
                        throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                    }
                },
                //校验校验位
                credential -> {
                    if (!isNew(credential)) {
                        return;
                    }
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
    protected List<CredentialParser<MainlandResidentIdNumberInfo>> buildParsers() {
        return Arrays.asList(
                //解析首次签发地区
                (credential, info) -> {
                    DomesticRegionInfo region = RegionUtil.getDomesticRegionInfoByCode(credential.substring(0, 6));
                    info.setRegion(region);
                },
                //解析生日
                (credential, info) -> {
                    String birthDate = null;
                    if (isNew(credential)) {
                        birthDate = credential.substring(6, 14);
                    } else {
                        birthDate = "19" + credential.substring(6, 12);
                    }
                    info.setBirthDate(birthDate);
                },
                //解析性别
                (credential, info) -> {
                    int genderDigit = -1;
                    if (isNew(credential)) {
                        genderDigit = credential.charAt(16) - '0';
                    } else {
                        genderDigit = credential.charAt(14) - '0';
                    }
                    Gender gender = (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
                    info.setGender(gender);
                }
        );
    }

    /**
     * 获取中华人民共和国居民身份证号码信息
     *
     * @return 中华人民共和国居民身份证号码信息
     */
    @Override
    protected MainlandResidentIdNumberInfo getInfo() {
        return new MainlandResidentIdNumberInfo();
    }

    /**
     * 是否是18位新身份证号码
     *
     * @param credential 证件
     * @return 是否是18位新身份证号码
     */
    private boolean isNew(String credential) {
        return credential.length() == 18;
    }
}
