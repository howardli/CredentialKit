/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.spi;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialParser;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.CredentialValidator;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;

import java.util.Collections;
import java.util.Map;

/**
 * 测试用SPI提供者（格式：SPI + 6位数字）
 * <p>
 * 通过{@code META-INF/services/com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider}
 * 注册，用于验证{@link CredentialRegistry#create()}的自动发现机制。
 * </p>
 */
public final class TestSpiCredentialProvider implements CredentialProcessorProvider {

    /**
     * 测试用证件类型（SPI + 6位数字）
     */
    public enum TestSpiCredentialType implements CredentialType {

        SPI_TEST_ID("SPI测试证件", "SPI Test Credential");

        private final String chineseName;
        private final String englishName;

        TestSpiCredentialType(String chineseName, String englishName) {
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

    /**
     * 测试用证件信息
     */
    static final class TestSpiCredentialInfo extends CredentialInfo {
    }

    /**
     * 测试用证件处理器
     */
    static final class TestSpiCredentialProcessor extends CredentialProcessor<TestSpiCredentialInfo> {

        TestSpiCredentialProcessor() {
            super(
                    Collections.singletonList(
                            (CredentialValidator) credential ->
                                    credential.matches("SPI\\d{6}")
                                            ? ValidationResult.success()
                                            : ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR)
                    ),
                    Collections.<CredentialParser<TestSpiCredentialInfo>>emptyList()
            );
        }

        @Override
        protected TestSpiCredentialInfo createInfo() {
            return new TestSpiCredentialInfo();
        }
    }

    @Override
    public Map<CredentialType, CredentialProcessor<? extends CredentialInfo>> getProcessors() {
        return Collections.singletonMap(TestSpiCredentialType.SPI_TEST_ID, new TestSpiCredentialProcessor());
    }
}
