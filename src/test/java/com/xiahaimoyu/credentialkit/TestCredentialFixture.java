/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialParser;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.CredentialValidator;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;

import java.util.Collections;

/**
 * 测试用自定义证件类型和处理器（8位数字）
 */
enum TestCredentialType implements CredentialType {

    TEST_ID("测试证件", "Test Credential");

    private final String chineseName;
    private final String englishName;

    TestCredentialType(String chineseName, String englishName) {
        this.chineseName = chineseName;
        this.englishName = englishName;
    }

    @Override
    public String getChineseName() {
        return chineseName;
    }

    @Override
    public String getEnglishName() {
        return englishName;
    }
}

class TestCredentialInfo extends CredentialInfo {
}

class TestCredentialProcessor extends CredentialProcessor<TestCredentialInfo> {

    TestCredentialProcessor() {
        super(
                Collections.singletonList(
                        (CredentialValidator) credential ->
                                credential.matches("\\d{8}")
                                        ? ValidationResult.success()
                                        : ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR)
                ),
                Collections.<CredentialParser<TestCredentialInfo>>emptyList()
        );
    }

    @Override
    protected TestCredentialInfo createInfo() {
        return new TestCredentialInfo();
    }
}
