/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.DomesticPersonCredentialInfo;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import static com.xiahaimoyu.credentialkit.processor.ValidationResult.validIf;

/**
 * 港澳台居民居住证处理器基类
 * <p>
 * 居住证号码结构与18位居民身份证一致（6位地区码 + 8位生日 + 3位顺序码 + 1位校验位），
 * 仅地区码前缀不同，校验与解析链在此统一实现。子类提供各自的基础格式正则和
 * 地区码前缀到地区信息的映射。包内可见，不属于公共API。
 * </p>
 *
 * @author Howard.Li
 */
abstract class AbstractResidencePermitProcessor<T extends DomesticPersonCredentialInfo> extends CredentialProcessor<T> {

    /**
     * 构造器
     *
     * @param pattern       基础校验正则（约束18位长度、地区码前缀和字符集）
     * @param prefixRegions 前6位地区码到地区信息的映射
     */
    AbstractResidencePermitProcessor(Pattern pattern, Map<String, DomesticRegionInfo> prefixRegions) {
        super(
                Arrays.asList(
                        // 基本格式校验（null规格化后为空字符串，长度校验必然失败）
                        credential -> validIf(
                                credential.length() == 18 && pattern.matcher(credential).matches(),
                                ErrorCode.BASIC_FORMAT_ERROR),
                        // 校验生日
                        credential -> validIf(
                                DateUtil.validDateBeforeNow(credential.substring(6, 14)),
                                ErrorCode.BIRTH_DATE_ERROR),
                        // 校验校验位
                        credential -> validIf(
                                CheckDigitUtil.getIdCardCheckDigit(credential.substring(0, 17)) == credential.charAt(17),
                                ErrorCode.CHECK_DIGIT_ERROR)
                ),
                Arrays.asList(
                        // 解析地区（前6位地区码查映射，正则已保证前缀在映射内）
                        (credential, info) -> info.setRegion(prefixRegions.get(credential.substring(0, 6))),
                        // 解析生日
                        (credential, info) -> info.setBirthDate(credential.substring(6, 14)),
                        // 解析性别（顺序码末位，偶数为女，奇数为男）
                        (credential, info) -> info.setGender(Gender.fromDigit(credential.charAt(16) - '0'))
                )
        );
    }
}
