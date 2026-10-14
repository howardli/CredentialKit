/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.HkMacaoTravelPermitNonChineseInfo;

import java.util.Collections;
import java.util.regex.Pattern;

import static com.xiahaimoyu.credentialkit.processor.ValidationResult.validIf;

/**
 * 港澳居民来往内地通行证（非中国籍）处理器
 * <p>
 * 号码共9位，一人一号、终身不变：HA/MA字头（HA=香港、MA=澳门）+ 7位数字。
 * 换证次数是卡面独立登记项目，不属于号码的一部分，故不做解析。
 * </p>
 *
 * @author Howard.Li
 */
public class HkMacaoTravelPermitNonChineseProcessor extends CredentialProcessor<HkMacaoTravelPermitNonChineseInfo> {

    /**
     * 基础校验正则（HA/MA字头 + 7位数字）
     */
    private static final Pattern PATTERN = Pattern.compile("^[HM]A\\d{7}$");

    /**
     * 构造器
     */
    public HkMacaoTravelPermitNonChineseProcessor() {
        super(
                Collections.singletonList(
                        // 基本格式校验（null规格化后为空字符串，长度校验必然失败）
                        credential -> validIf(
                                credential.length() == 9 && PATTERN.matcher(credential).matches(),
                                ErrorCode.BASIC_FORMAT_ERROR)
                ),
                Collections.singletonList(
                        // 解析地区（HA=香港，MA=澳门）
                        (credential, info) -> {
                            String regionCode = credential.substring(0, 2);
                            if (regionCode.equals("HA")) {
                                info.setRegion(CredentialRegionInfos.HONG_KONG_TRAVEL_PERMIT_NON_CHINESE);
                            } else if (regionCode.equals("MA")) {
                                info.setRegion(CredentialRegionInfos.MACAO_TRAVEL_PERMIT_NON_CHINESE);
                            }
                        }
                )
        );
    }

    /**
     * 获取港澳居民来往内地通行证（非中国籍）信息
     *
     * @return 港澳居民来往内地通行证（非中国籍）信息
     */
    @Override
    protected HkMacaoTravelPermitNonChineseInfo createInfo() {
        return new HkMacaoTravelPermitNonChineseInfo();
    }
}
