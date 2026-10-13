/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.xiahaimoyu.credentialkit.processor.ValidationResult.validIf;

/**
 * 可机读护照处理器
 * <p>
 * 签发地区和持证人国籍/地区编码按机读码语境查找：ISO 3166优先，
 * 未命中回退ICAO 9303机读码扩展编码（见{@link RegionUtil#getMrzRegionInfoByAlpha3(String)}）。
 * </p>
 *
 * @author Howard.Li
 */
public class MachineReadablePassportProcessor extends CredentialProcessor<MachineReadablePassportInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^P[A-Z<][A-Z<]{3}[A-Z<]{39}[A-Z0-9<]{9}[0-9][A-Z<]{3}[0-9]{6}[0-9][MF<][0-9]{6}[0-9][A-Z0-9<]{14}[0-9<][0-9]$");

    /**
     * 姓名正则
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^([A-Z]+<)*[A-Z]+(<<([A-Z]+<)*[A-Z]+)?$");

    /**
     * 构造器
     * <p>
     * 校验链按MRZ字段顺序执行，解析链与校验链字段一一对应，各步骤实现见下方命名方法。
     * </p>
     */
    public MachineReadablePassportProcessor() {
        super(
                Arrays.asList(
                        MachineReadablePassportProcessor::validateFormat,
                        MachineReadablePassportProcessor::validateIssuingRegion,
                        MachineReadablePassportProcessor::validateName,
                        MachineReadablePassportProcessor::validatePassportNumberCheckDigit,
                        MachineReadablePassportProcessor::validateHolderRegion,
                        MachineReadablePassportProcessor::validateBirthDate,
                        MachineReadablePassportProcessor::validateExpirationDate,
                        MachineReadablePassportProcessor::validatePersonalNumberCheckDigit,
                        MachineReadablePassportProcessor::validateCompositeCheckDigit
                ),
                Arrays.asList(
                        MachineReadablePassportProcessor::parseIssuingRegion,
                        MachineReadablePassportProcessor::parseName,
                        MachineReadablePassportProcessor::parsePassportNumber,
                        MachineReadablePassportProcessor::parseHolderRegion,
                        MachineReadablePassportProcessor::parseBirthDate,
                        MachineReadablePassportProcessor::parseGender,
                        MachineReadablePassportProcessor::parseExpirationDate,
                        MachineReadablePassportProcessor::parsePersonalNumber
                )
        );
    }

    /**
     * 获取可机读护照信息
     *
     * @return 可机读护照信息
     */
    @Override
    protected MachineReadablePassportInfo createInfo() {
        return new MachineReadablePassportInfo();
    }

    // ==================== 校验器 ====================

    /**
     * 校验基本格式
     * <p>
     * 总长度88位且各字段字符集符合MRZ规范（null规格化后为空字符串，长度校验必然失败）。
     * </p>
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateFormat(String credential) {
        return validIf(credential.length() == 88 && PATTERN.matcher(credential).matches(), ErrorCode.BASIC_FORMAT_ERROR);
    }

    /**
     * 校验签发地区（第3-5位）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateIssuingRegion(String credential) {
        return validIf(RegionUtil.getMrzRegionInfoByAlpha3(credential.substring(2, 5)) != null, ErrorCode.REGION_ERROR);
    }

    /**
     * 校验姓名（第6-44位，去除尾部填充符后须符合姓名格式）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateName(String credential) {
        return validIf(NAME_PATTERN.matcher(rightTrim(credential.substring(5, 44))).matches(), ErrorCode.NAME_ERROR);
    }

    /**
     * 校验护照号码校验位（第45-53位计算，第54位校验）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validatePassportNumberCheckDigit(String credential) {
        return validIf(CheckDigitUtil.getMachineReadablePassportCheckDigit(credential.substring(44, 53)) == credential.charAt(53),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    /**
     * 校验持证人国籍/地区（第55-57位）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateHolderRegion(String credential) {
        return validIf(RegionUtil.getMrzRegionInfoByAlpha3(credential.substring(54, 57)) != null, ErrorCode.INTERNATIONAL_REGION_ERROR);
    }

    /**
     * 校验生日（第58-63位六位年月日 + 第64位校验位）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateBirthDate(String credential) {
        String birthDate = credential.substring(57, 63);
        if (!DateUtil.validDateBeforeNow("19" + birthDate) && !DateUtil.validDateBeforeNow("20" + birthDate)) {
            return ValidationResult.failure(ErrorCode.BIRTH_DATE_ERROR);
        }
        return validIf(CheckDigitUtil.getMachineReadablePassportCheckDigit(birthDate) == credential.charAt(63),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    /**
     * 校验有效期（第66-71位六位年月日 + 第72位校验位）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateExpirationDate(String credential) {
        String expirationDate = credential.substring(65, 71);
        if (!DateUtil.validDate("19" + expirationDate) && !DateUtil.validDate("20" + expirationDate)) {
            return ValidationResult.failure(ErrorCode.EXPIRATION_DATE_ERROR);
        }
        return validIf(CheckDigitUtil.getMachineReadablePassportCheckDigit(expirationDate) == credential.charAt(71),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    /**
     * 校验个人号码校验位（第73-86位计算，第87位校验）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validatePersonalNumberCheckDigit(String credential) {
        return validIf(CheckDigitUtil.getMachineReadablePassportCheckDigit(credential.substring(72, 86)) == credential.charAt(86),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    /**
     * 校验复合校验位（第45-54、58-64、66-87位拼接后计算，第88位校验）
     *
     * @param credential 证件号码
     * @return 校验结果
     */
    private static ValidationResult validateCompositeCheckDigit(String credential) {
        String composite = credential.substring(44, 54) + credential.substring(57, 64) + credential.substring(65, 87);
        return validIf(CheckDigitUtil.getMachineReadablePassportCheckDigit(composite) == credential.charAt(87),
                ErrorCode.CHECK_DIGIT_ERROR);
    }

    // ==================== 解析器 ====================

    /**
     * 解析签发地区（第3-5位）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parseIssuingRegion(String credential, MachineReadablePassportInfo info) {
        info.setIssuingRegion(RegionUtil.getMrzRegionInfoByAlpha3(credential.substring(2, 5)));
    }

    /**
     * 解析姓名（第6-44位，主姓与名以&lt;&lt;分隔，单词内填充符转空格）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parseName(String credential, MachineReadablePassportInfo info) {
        String name = rightTrim(credential.substring(5, 44));
        int separatorIndex = name.indexOf("<<");
        if (separatorIndex < 0) {
            info.setSurname(name.replace("<", " "));
        } else {
            info.setSurname(name.substring(0, separatorIndex).replace("<", " "));
            info.setGivenName(name.substring(separatorIndex + 2).replace("<", " "));
        }
    }

    /**
     * 解析护照号（第45-53位，不足9位以&lt;填充，去除尾部填充符）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parsePassportNumber(String credential, MachineReadablePassportInfo info) {
        info.setPassportNumber(rightTrim(credential.substring(44, 53)));
    }

    /**
     * 解析归属地（第55-57位）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parseHolderRegion(String credential, MachineReadablePassportInfo info) {
        info.setRegion(RegionUtil.getMrzRegionInfoByAlpha3(credential.substring(54, 57)));
    }

    /**
     * 解析生日（第58-63位）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parseBirthDate(String credential, MachineReadablePassportInfo info) {
        info.setBirthDate(DateUtil.toFullYearDate(credential.substring(57, 63)));
    }

    /**
     * 解析性别（第65位，M/F/&lt;）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parseGender(String credential, MachineReadablePassportInfo info) {
        char gender = credential.charAt(64);
        if (gender == 'M') {
            info.setGender(Gender.MALE);
        } else if (gender == 'F') {
            info.setGender(Gender.FEMALE);
        } else if (gender == '<') {
            info.setGender(Gender.UNKNOWN);
        }
    }

    /**
     * 解析有效期（第66-71位）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parseExpirationDate(String credential, MachineReadablePassportInfo info) {
        info.setExpirationDate(DateUtil.toFullYearExpirationDate(credential.substring(65, 71)));
    }

    /**
     * 解析个人号码（第73-86位，去除尾部填充符，填充符转空格）
     *
     * @param credential 证件号码
     * @param info       信息对象
     */
    private static void parsePersonalNumber(String credential, MachineReadablePassportInfo info) {
        info.setPersonalNumber(rightTrim(credential.substring(72, 86)).replace("<", " "));
    }

    // ==================== 工具方法 ====================

    /**
     * 去掉结尾的<
     *
     * @param str 字符串
     * @return 去掉结尾的<后的字符串
     */
    private static String rightTrim(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) == '<') {
            end--;
        }
        return end == str.length() ? str : str.substring(0, end);
    }
}
