/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.TaiwanTravelPermitInfo;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 台湾居民来往大陆通行证处理器
 *
 * @author Howard.Li
 */
public class TaiwanTravelPermitProcessor extends CredentialProcessor<TaiwanTravelPermitInfo> {

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
                // 基本格式校验
                credential -> {
                    if (credential == null || (credential.length() != 8 && credential.length() != 10) || !PATTERN.matcher(credential).matches()) {
                        return ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR);
                    }
                    return ValidationResult.success();
                }
        );
    }

    /**
     * 构造解析器列表
     *
     * @return 解析器列表
     */
    @Override
    protected List<CredentialParser<TaiwanTravelPermitInfo>> buildParsers() {
        return Collections.singletonList(
                // 解析换证次数
                (credential, info) -> {
                    if (credential.length() == 10) {
                        info.setReplacementTime(Integer.parseInt(credential.substring(8, 10)));
                    }
                }
        );
    }

    /**
     * 获取台湾居民来往大陆通行证信息
     *
     * @return 台湾居民来往大陆通行证信息
     */
    @Override
    protected TaiwanTravelPermitInfo createInfo() {
        return new TaiwanTravelPermitInfo();
    }
}
