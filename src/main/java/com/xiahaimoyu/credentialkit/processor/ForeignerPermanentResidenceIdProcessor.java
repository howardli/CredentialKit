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
import java.util.List;
import java.util.regex.Pattern;

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
     * 构造校验器列表
     *
     * @return 校验器列表
     */
    @Override
    protected List<CredentialValidator> buildValidators() {
        return Arrays.asList(
                // 基本格式校验
                credential -> {
                    if (credential == null) {
                        return ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR);
                    }
                    if (credential.length() == 15 && PATTERN_15.matcher(credential).matches()) {
                        return ValidationResult.success();
                    }
                    if (credential.length() == 18 && PATTERN_18.matcher(credential).matches()) {
                        return ValidationResult.success();
                    }
                    return ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR);
                },
                // 版本特定校验
                credential -> {
                    if (credential == null) {
                        return ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR);
                    }
                    if (credential.length() == 15) {
                        return validate15Bit(credential);
                    }
                    if (credential.length() == 18) {
                        return validate18Bit(credential);
                    }
                    return ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR);
                }
        );
    }

    /**
     * 构造解析器列表
     *
     * @return 解析器列表
     */
    @Override
    protected List<CredentialParser<ForeignerPermanentResidenceIdInfo>> buildParsers() {
        return Arrays.asList(
                (credential, info) -> {
                    if (credential == null) {
                        return;
                    }
                    if (credential.length() == 15) {
                        parse15Bit(credential, info);
                    } else if (credential.length() == 18) {
                        parse18Bit(credential, info);
                    }
                }
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
    private ValidationResult validate15Bit(String credential) {
        // 校验国籍
        ValidationResult result = validate15BitNationality(credential);
        if (!result.isValid()) {
            return result;
        }
        // 校验地区
        result = validate15BitRegion(credential);
        if (!result.isValid()) {
            return result;
        }
        // 校验生日
        result = validate15BitBirthDate(credential);
        if (!result.isValid()) {
            return result;
        }
        // 校验校验位
        result = validate15BitCheckDigit(credential);
        if (!result.isValid()) {
            return result;
        }
        return ValidationResult.success();
    }

    /**
     * 校验15位版本的国籍码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate15BitNationality(String credential) {
        String nationalityCode = credential.substring(0, 3);
        if (RegionUtil.getInternationalRegionInfoByAlpha3(nationalityCode) == null) {
            return ValidationResult.failure(ErrorCode.REGION_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 校验15位版本的地区码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate15BitRegion(String credential) {
        String regionCode = credential.substring(3, 7) + "00";
        if (RegionUtil.getDomesticRegionInfoByCode(regionCode) == null) {
            return ValidationResult.failure(ErrorCode.REGION_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 校验15位版本的生日
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate15BitBirthDate(String credential) {
        String birthDate = credential.substring(7, 13);
        if (!DateUtil.validDateBeforeNow("19" + birthDate) && !DateUtil.validDateBeforeNow("20" + birthDate)) {
            return ValidationResult.failure(ErrorCode.BIRTH_DATE_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 校验15位版本的校验位
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate15BitCheckDigit(String credential) {
        char expectedDigit = CheckDigitUtil.getMachineReadablePassportCheckDigit(credential.substring(0, 14));
        if (expectedDigit != credential.charAt(14)) {
            return ValidationResult.failure(ErrorCode.CHECK_DIGIT_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 解析15位版本
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private void parse15Bit(String credential, ForeignerPermanentResidenceIdInfo info) {
        info.setInternationalRegionInfo(RegionUtil.getInternationalRegionInfoByAlpha3(credential.substring(0, 3)));
        info.setDomesticRegionInfo(RegionUtil.getDomesticRegionInfoByCode(credential.substring(3, 7) + "00"));
        info.setBirthDate(credential.substring(7, 13));
        info.setGender(Gender.fromDigit(credential.charAt(13) - '0'));
    }

    // ==================== 18位版本校验与解析 ====================

    /**
     * 校验18位版本
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate18Bit(String credential) {
        // 校验地区
        ValidationResult result = validate18BitRegion(credential);
        if (!result.isValid()) {
            return result;
        }
        // 校验国籍
        result = validate18BitNationality(credential);
        if (!result.isValid()) {
            return result;
        }
        // 校验生日
        result = validate18BitBirthDate(credential);
        if (!result.isValid()) {
            return result;
        }
        // 校验校验位
        result = validate18BitCheckDigit(credential);
        if (!result.isValid()) {
            return result;
        }
        return ValidationResult.success();
    }

    /**
     * 校验18位版本的地区码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate18BitRegion(String credential) {
        String regionCode = credential.substring(1, 3) + "0000";
        if (RegionUtil.getDomesticRegionInfoByCode(regionCode) == null) {
            return ValidationResult.failure(ErrorCode.REGION_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 校验18位版本的国籍数字码
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate18BitNationality(String credential) {
        String nationalityCode = credential.substring(3, 6);
        if (RegionUtil.getInternationalRegionInfoByNumeric(nationalityCode) == null) {
            return ValidationResult.failure(ErrorCode.REGION_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 校验18位版本的生日
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate18BitBirthDate(String credential) {
        String birthDate = credential.substring(6, 14);
        if (!DateUtil.validDateBeforeNow(birthDate)) {
            return ValidationResult.failure(ErrorCode.BIRTH_DATE_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 校验18位版本的校验位
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private ValidationResult validate18BitCheckDigit(String credential) {
        char expectedDigit = CheckDigitUtil.getIdCardCheckDigit(credential.substring(0, 17));
        if (expectedDigit != credential.charAt(17)) {
            return ValidationResult.failure(ErrorCode.CHECK_DIGIT_ERROR);
        }
        return ValidationResult.success();
    }

    /**
     * 解析18位版本
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private void parse18Bit(String credential, ForeignerPermanentResidenceIdInfo info) {
        info.setDomesticRegionInfo(RegionUtil.getDomesticRegionInfoByCode(credential.substring(1, 3) + "0000"));
        info.setInternationalRegionInfo(RegionUtil.getInternationalRegionInfoByNumeric(credential.substring(3, 6)));
        info.setBirthDate(credential.substring(6, 14));
        info.setGender(Gender.fromDigit(credential.charAt(16) - '0'));
    }
}