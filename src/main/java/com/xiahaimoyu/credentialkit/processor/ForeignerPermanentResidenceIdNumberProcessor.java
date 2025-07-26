/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.ForeignerPermanentResidenceIdNumberInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 外国人永久居留身份证号码处理器
 *
 * @author Howard.Li
 */
public class ForeignerPermanentResidenceIdNumberProcessor extends CredentialProcessor<ForeignerPermanentResidenceIdNumberInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^([A-Z]{3}\\d{12})|(9\\d{16})[0-9X]$");

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
                    if (credential == null || (credential.length() != 15 && credential.length() != 18) || !PATTERN.matcher(credential).matches()) {
                        throw CredentialException.of(ErrorCode.BASIC_FORMAT_ERROR, "基本格式校验失败：{0}", credential);
                    }
                },
                //15位号校验首次签发国籍
                credential -> {
                    if (isNew(credential)) {
                        return;
                    }
                    String regionCode = credential.substring(0, 3);
                    if (RegionUtil.getInternationalRegionInfoByAlpha3(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "国籍不对：{0}", regionCode);
                    }
                },
                //15位号校验签发地区
                credential -> {
                    if (isNew(credential)) {
                        return;
                    }
                    String regionCode = credential.substring(3, 7);
                    if (RegionUtil.getDomesticRegionInfoByCode(regionCode + "00") == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "签发地区不对：{0}", regionCode);
                    }
                },
                //15位号校验生日
                credential -> {
                    if (isNew(credential)) {
                        return;
                    }
                    String birthDate = credential.substring(7, 13);
                    if (!DateUtil.validDateBeforeNow("19" + birthDate) && !DateUtil.validDateBeforeNow("20" + birthDate)) {
                        throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                    }
                },
                //15位号校验校验位
                credential -> {
                    if (isNew(credential)) {
                        return;
                    }
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(0, 14));
                    if (checkDigit != credential.charAt(14)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(14));
                    }
                },
                //18位号校验签发地区
                credential -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    String regionCode = credential.substring(1, 3);
                    if (RegionUtil.getDomesticRegionInfoByCode(regionCode + "0000") == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "签发地区不对：{0}", regionCode);
                    }
                },
                //18位号校验首次签发国籍
                credential -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    String regionCode = credential.substring(3, 6);
                    if (RegionUtil.getInternationalRegionInfoByNumeric(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "国籍不对：{0}", regionCode);
                    }
                },
                //18位号校验生日
                credential -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    String birthDate = credential.substring(6, 14);
                    if (!DateUtil.validDateBeforeNow(birthDate)) {
                        throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                    }
                },
                //18位号校验校验位
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
    @Override
    protected List<CredentialParser<ForeignerPermanentResidenceIdNumberInfo>> buildParsers() {
        return Arrays.asList(
                //15位号解析首次签发国籍
                (credential, info) -> {
                    if (isNew(credential)) {
                        return;
                    }
                    String regionCode = credential.substring(0, 3);
                    info.setInternationalRegionInfo(RegionUtil.getInternationalRegionInfoByAlpha3(regionCode));
                },
                //15位号解析首次签发地区
                (credential, info) -> {
                    if (isNew(credential)) {
                        return;
                    }
                    info.setDomesticRegionInfo(RegionUtil.getDomesticRegionInfoByCode(credential.substring(3, 7) + "00"));
                },
                //15位号解析生日
                (credential, info) -> {
                    if (isNew(credential)) {
                        return;
                    }
                    info.setBirthDate(credential.substring(7, 13));
                },
                //15位号解析性别
                (credential, info) -> {
                    if (isNew(credential)) {
                        return;
                    }
                    int genderDigit = credential.charAt(13) - '0';
                    Gender gender = (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
                    info.setGender(gender);
                },
                //18位号解析首次签发地区
                (credential, info) -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    info.setDomesticRegionInfo(RegionUtil.getDomesticRegionInfoByCode(credential.substring(1, 3) + "0000"));
                },
                //18位号解析首次签发国籍
                (credential, info) -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    info.setInternationalRegionInfo(RegionUtil.getInternationalRegionInfoByNumeric(credential.substring(3, 6)));
                },
                //18位号解析生日
                (credential, info) -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    info.setBirthDate(credential.substring(6, 14));
                },
                //18位号解析性别
                (credential, info) -> {
                    if (!isNew(credential)) {
                        return;
                    }
                    int genderDigit = credential.charAt(16) - '0';
                    Gender gender = (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
                    info.setGender(gender);
                }
        );
    }

    /**
     * 获取外国人永久居留身份证号码信息
     *
     * @return 外国人永久居留身份证号码信息
     */
    @Override
    protected ForeignerPermanentResidenceIdNumberInfo getInfo() {
        return new ForeignerPermanentResidenceIdNumberInfo();
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
