/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.HkMacaoTravelPermitInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.xiahaimoyu.credentialkit.processor.ValidationResult.validIf;

/**
 * 港澳居民来往内地通行证处理器
 *
 * @author Howard.Li
 */
public class HkMacaoTravelPermitProcessor extends CredentialProcessor<HkMacaoTravelPermitInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^[HM](\\d{8}|\\d{10})$");

    /**
     * 构造器
     */
    public HkMacaoTravelPermitProcessor() {
        super(
                Collections.singletonList(
                        // 基本格式校验（null规格化后为空字符串，长度校验必然失败）
                        credential -> validIf(
                                (credential.length() == 9 || credential.length() == 11) && PATTERN.matcher(credential).matches(),
                                ErrorCode.BASIC_FORMAT_ERROR)
                ),
                Arrays.asList(
                        // 解析地区
                        (credential, info) -> {
                            String regionCode = credential.substring(0, 1);
                            if (regionCode.equals("H")) {
                                info.setRegion(CredentialRegionInfos.HONG_KONG_TRAVEL_PERMIT);
                            } else if (regionCode.equals("M")) {
                                info.setRegion(CredentialRegionInfos.MACAO_TRAVEL_PERMIT);
                            }
                        },
                        // 解析换证次数
                        (credential, info) -> {
                            if (credential.length() == 11) {
                                info.setReplacementTime(Integer.parseInt(credential.substring(9, 11)));
                            }
                        }
                )
        );
    }

    /**
     * 获取港澳居民来往内地通行证信息
     *
     * @return 港澳居民来往内地通行证信息
     */
    @Override
    protected HkMacaoTravelPermitInfo createInfo() {
        return new HkMacaoTravelPermitInfo();
    }
}
