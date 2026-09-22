/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.ForeignerPermanentResidenceIdInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.xiahaimoyu.credentialkit.processor.ValidationResult.validIf;

/**
 * 外国人永久居留身份证处理器
 * <p>
 * 支持两种版本：
 * - 15位版本：3位国籍码 + 4位地区码 + 6位生日 + 1位性别 + 1位校验位
 * - 18位版本：1位类型码(9) + 2位地区码 + 3位国籍数字码 + 8位生日 + 3位顺序码 + 1位校验位
 * </p>
 *
 * @author Howard.Li
 */
public class ForeignerPermanentResidenceIdProcessor extends CredentialProcessor<ForeignerPermanentResidenceIdInfo> {

    /**
     * 15位格式正则：3位字母 + 12位数字
     */
    private static final Pattern PATTERN_15 = Pattern.compile("^[A-Z]{3}\\d{12}$");

    /**
     * 18位格式正则：9开头 + 16位数字/字母 + 校验位
     */
    private static final Pattern PATTERN_18 = Pattern.compile("^9\\d{16}[0-9X]$");

    /**
     * 构造器
     */
    public ForeignerPermanentResidenceIdProcessor() {
        super(
                Arrays.asList(
                        // 基本格式校验（null规格化后为空字符串，两个格式必然都不匹配）
                        credential -> validIf(
                                (credential.length() == 15 && PATTERN_15.matcher(credential).matches())
                                        || (credential.length() == 18 && PATTERN_18.matcher(credential).matches()),
                                ErrorCode.BASIC_FORMAT_ERROR),
                        // 版本特定校验（基本格式校验已保证长度为15或18）
                        credential -> credential.length() == 15
                                ? validate15Bit(credential)
                                : validate18Bit(credential)
                ),
                Collections.singletonList(
                        (credential, info) -> {
                            if (credential.length() == 15) {
                                parse15Bit(credential, info);
                            } else {
                                parse18Bit(credential, info);
                            }
                        }
                )
        );
    }

    /**
     * 创建证件信息对象
     *
     * @return 外国人永久居留身份证信息对象
     */
    @Override
    protected ForeignerPermanentResidenceIdInfo createInfo() {
        return new ForeignerPermanentResidenceIdInfo();
    }

    // ==================== 15位版本校验与解析 ====================

    /**
     * 校验15位版本
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate15Bit(String credential) {
        ValidationResult result = validate15BitNationality(credential);
        if (result.isValid()) {
            result = validate15BitRegion(credential);
        }
        if (result.isValid()) {
            result = validate15BitBirthDate(credential);
        }
        if (result.isValid()) {
            result = validate15BitCheckDigit(credential);
        }
        return result;
    }

    /**
     * 校验15位版本的国籍码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate15BitNationality(String credential) {
        return validIf(RegionUtil.getInternationalRegionInfoByAlpha3(credential.substring(0, 3)) != null,
                ErrorCode.INTERNATIONAL_REGION_ERROR);
    }

    /**
     * 校验15位版本的地区码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate15BitRegion(String credential) {
        return validIf(RegionUtil.getDomesticRegionInfoByCode(credential.substring(3, 7) + "00") != null,
                ErrorCode.REGION_ERROR);
    }

    /**
     * 校验15位版本的生日
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate15BitBirthDate(String credential) {
        String birthDate = credential.substring(7, 13);
        return validIf(DateUtil.validDateBeforeNow("19" + birthDate) || DateUtil.validDateBeforeNow("20" + birthDate),
                ErrorCode.BIRTH_DATE_ERROR);
    }

    /**
     * 校验15位版本的校验位
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate15BitCheckDigit(String credential) {
        return validIf(CheckDigitUtil.getMachineReadablePassportCheckDigit(credential.substring(0, 14)) == credential.charAt(14),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    /**
     * 解析15位版本
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parse15Bit(String credential, ForeignerPermanentResidenceIdInfo info) {
        info.setInternationalRegionInfo(RegionUtil.getInternationalRegionInfoByAlpha3(credential.substring(0, 3)));
        info.setDomesticRegionInfo(RegionUtil.getDomesticRegionInfoByCode(credential.substring(3, 7) + "00"));
        String yyBirthDate = credential.substring(7, 13);
        info.setBirthDate(DateUtil.toFullYearDate(yyBirthDate));
        info.setGender(Gender.fromDigit(credential.charAt(13) - '0'));
    }

    // ==================== 18位版本校验与解析 ====================

    /**
     * 校验18位版本
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate18Bit(String credential) {
        ValidationResult result = validate18BitRegion(credential);
        if (result.isValid()) {
            result = validate18BitNationality(credential);
        }
        if (result.isValid()) {
            result = validate18BitBirthDate(credential);
        }
        if (result.isValid()) {
            result = validate18BitCheckDigit(credential);
        }
        return result;
    }

    /**
     * 校验18位版本的地区码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate18BitRegion(String credential) {
        return validIf(RegionUtil.getDomesticRegionInfoByCode(credential.substring(1, 3) + "0000") != null,
                ErrorCode.REGION_ERROR);
    }

    /**
     * 校验18位版本的国籍数字码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate18BitNationality(String credential) {
        return validIf(RegionUtil.getInternationalRegionInfoByNumeric(credential.substring(3, 6)) != null,
                ErrorCode.INTERNATIONAL_REGION_ERROR);
    }

    /**
     * 校验18位版本的生日
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate18BitBirthDate(String credential) {
        return validIf(DateUtil.validDateBeforeNow(credential.substring(6, 14)), ErrorCode.BIRTH_DATE_ERROR);
    }

    /**
     * 校验18位版本的校验位
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validate18BitCheckDigit(String credential) {
        return validIf(CheckDigitUtil.getIdCardCheckDigit(credential.substring(0, 17)) == credential.charAt(17),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    /**
     * 解析18位版本
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parse18Bit(String credential, ForeignerPermanentResidenceIdInfo info) {
        info.setDomesticRegionInfo(RegionUtil.getDomesticRegionInfoByCode(credential.substring(1, 3) + "0000"));
        info.setInternationalRegionInfo(RegionUtil.getInternationalRegionInfoByNumeric(credential.substring(3, 6)));
        info.setBirthDate(credential.substring(6, 14));
        info.setGender(Gender.fromDigit(credential.charAt(16) - '0'));
    }
}
