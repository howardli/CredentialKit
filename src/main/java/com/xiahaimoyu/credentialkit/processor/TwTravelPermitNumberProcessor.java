/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.TwTravelPermitNumberInfo;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 台湾居民来往大陆通行证号码处理器
 *
 * @author Howard.Li
 */
public class TwTravelPermitNumberProcessor extends CredentialProcessor<TwTravelPermitNumberInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^(\\d{8}|\\d{10})$");

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
                    if (credential == null || (credential.length() != 8 && credential.length() != 10) || !PATTERN.matcher(credential).matches()) {
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
    protected List<CredentialParser<TwTravelPermitNumberInfo>> buildParsers() {
        return Collections.singletonList(
                //解析换证次数
                (credential, info) -> {
                    if (credential.length() == 10) {
                        info.setReplacementTime(Integer.parseInt(credential.substring(8, 10)));
                    }
                }
        );
    }

    /**
     * 获取台湾居民来往大陆通行证号码信息
     *
     * @return 台湾居民来往大陆通行证号码信息
     */
    @Override
    protected TwTravelPermitNumberInfo getInfo() {
        return new TwTravelPermitNumberInfo();
    }
}
