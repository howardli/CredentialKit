/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.constant.RegionConstant;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.HkMoTravelPermitNumberInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 港澳居民来往内地通行证号码处理器
 *
 * @author Howard.Li
 */
public class HkMoTravelPermitNumberProcessor extends CredentialProcessor<HkMoTravelPermitNumberInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^[HM](\\d{8}|\\d{10})$");

    /**
     * 构造校验器列表
     *
     * @return 校验器列表
     */
    @Override
    protected List<CredentialValidator> buildValidators() {
        return Collections.singletonList(
                //基本格式校验
                credential -> {
                    if (credential == null || (credential.length() != 9 && credential.length() != 11) || !PATTERN.matcher(credential).matches()) {
                        throw CredentialException.of(ErrorCode.BASIC_FORMAT_ERROR, "基本格式校验失败：{0}", credential);
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
    protected List<CredentialParser<HkMoTravelPermitNumberInfo>> buildParsers() {
        return Arrays.asList(
                //解析地区
                (credential, info) -> {
                    String regionCode = credential.substring(0, 1);
                    if (regionCode.equals("H")) {
                        info.setRegion(new DomesticRegionInfo(regionCode, RegionConstant.HONG_KONG, null, null));
                    } else if (regionCode.equals("M")) {
                        info.setRegion(new DomesticRegionInfo(regionCode, RegionConstant.MACAO, null, null));
                    }
                },
                //解析换证次数
                (credential, info) -> {
                    if (credential.length() == 11) {
                        info.setReplacementTime(Integer.parseInt(credential.substring(9, 11)));
                    }
                }
        );
    }

    /**
     * 获取港澳居民来往内地通行证号码信息
     *
     * @return 港澳居民来往内地通行证号码信息
     */
    @Override
    protected HkMoTravelPermitNumberInfo getInfo() {
        return new HkMoTravelPermitNumberInfo();
    }
}
