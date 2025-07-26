/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportCodeInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 可机读护照编码处理器
 *
 * @author Howard.Li
 */
public class MachineReadablePassportCodeProcessor extends CredentialProcessor<MachineReadablePassportCodeInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^P[A-Z<][A-Z<]{3}[A-Z<]{39}[A-Z0-9<]{9}[0-9][A-Z<]{3}[0-9]{6}[0-9][MF<][0-9]{6}[0-9][A-Z0-9<]{14}[0-9<][0-9]$");

    /**
     * 姓名正则
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^([A-Z]+<)*[A-Z]+(<<([A-Z]+<)*[A-Z]+)?$");

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
                    if (credential == null || credential.length() != 88 || !PATTERN.matcher(credential).matches()) {
                        throw CredentialException.of(ErrorCode.BASIC_FORMAT_ERROR, "基本格式校验失败：{0}", credential);
                    }
                },
                //校验签发地区
                credential -> {
                    String regionCode = credential.substring(2, 5);
                    if (RegionUtil.getInternationalRegionInfoByAlpha3(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "签发地区不对：{0}", regionCode);
                    }
                },
                //校验名字
                credential -> {
                    String name = rightTrim(credential.substring(5, 44));
                    if (!NAME_PATTERN.matcher(name).matches()) {
                        throw CredentialException.of(ErrorCode.NAME_ERROR, "名字不对：{0}", name);
                    }
                },
                //校验护照号码校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(44, 53));
                    if (checkDigit != credential.charAt(53)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(53));
                    }
                },
                //校验归属地
                credential -> {
                    String regionCode = credential.substring(54, 57);
                    if (RegionUtil.getInternationalRegionInfoByAlpha3(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "地区不对：{0}", regionCode);
                    }
                },
                //校验生日
                credential -> {
                    String birthDate = credential.substring(57, 63);
                    if (!DateUtil.validDateBeforeNow("19" + birthDate) && !DateUtil.validDateBeforeNow("20" + birthDate)) {
                        throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                    }
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(birthDate);
                    if (checkDigit != credential.charAt(63)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(63));
                    }
                },
                //校验有效期
                credential -> {
                    String expirationDate = credential.substring(65, 71);
                    if (!DateUtil.validDate("19" + expirationDate) && !DateUtil.validDate("20" + expirationDate)) {
                        throw CredentialException.of(ErrorCode.EXPIRATION_DATE_ERROR, "有效期不对：{0}", expirationDate);
                    }
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(expirationDate);
                    if (checkDigit != credential.charAt(71)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(71));
                    }
                },
                //校验个人号码校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(72, 86));
                    if (checkDigit != credential.charAt(86)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(86));
                    }
                },
                //校验护照校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(44, 54) + credential.substring(57, 64) + credential.substring(65, 87));
                    if (checkDigit != credential.charAt(87)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(87));
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
    protected List<CredentialParser<MachineReadablePassportCodeInfo>> buildParsers() {
        return Arrays.asList(
                //解析签发地区
                (credential, info) -> {
                    String regionCode = credential.substring(2, 5);
                    info.setIssuingRegion(RegionUtil.getInternationalRegionInfoByAlpha3(regionCode));
                },
                //解析名字
                (credential, info) -> {
                    String name = rightTrim(credential.substring(5, 44));
                    String[] names = name.split("<<");
                    info.setSurname(names[0].replace("<", " "));
                    if (names.length > 1) {
                        info.setGivenName(names[1].replace("<", " "));
                    }
                },
                //解析护照号
                (credential, info) -> {
                    info.setPassportNumber(credential.substring(44, 53));
                },
                //解析归属地
                (credential, info) -> {
                    String regionCode = credential.substring(54, 57);
                    info.setRegion(RegionUtil.getInternationalRegionInfoByAlpha3(regionCode));
                },
                //解析生日
                (credential, info) -> {
                    info.setBirthdate(credential.substring(57, 63));
                },
                //解析性别
                (credential, info) -> {
                    char gender = credential.charAt(64);
                    if (gender == 'M') {
                        info.setGender(Gender.MALE);
                    } else if (gender == 'F') {
                        info.setGender(Gender.FEMALE);
                    } else if (gender == '<') {
                        info.setGender(Gender.UNKNOWN);
                    }
                },
                //解析有效期
                (credential, info) -> {
                    info.setExpirationDate(credential.substring(65, 71));
                },
                //解析个人号码
                (credential, info) -> {
                    String personalNumber = rightTrim(credential.substring(72, 86)).replace("<", " ");
                    info.setPersonalNumber(personalNumber);
                }
        );
    }

    /**
     * 获取可机读护照编码信息
     *
     * @return 可机读护照编码信息
     */
    @Override
    protected MachineReadablePassportCodeInfo getInfo() {
        return new MachineReadablePassportCodeInfo();
    }

    /**
     * 去掉结尾的<
     *
     * @param str 字符串
     * @return 去掉结尾的<后的字符串
     */
    private String rightTrim(String str) {
        return str.replaceAll("<+$", "");
    }
}
