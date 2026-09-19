/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialProcessorTest {

    @Test
    void validatorExceptionTreatedAsFormatError() {
        // 校验器抛出运行时异常时视为基本格式错误，validate/parse不以异常形式抛出
        CredentialProcessor<CredentialInfo> processor = throwingValidatorProcessor();
        assertThat(processor.validate("anything").isValid()).isFalse();
        assertThat(processor.validate("anything").getErrorCode()).hasValue(ErrorCode.BASIC_FORMAT_ERROR);
        assertThat(processor.parse("anything")).isEqualTo(Optional.empty());
    }

    /**
     * 创建校验时抛异常的处理器
     *
     * @return 处理器
     */
    private CredentialProcessor<CredentialInfo> throwingValidatorProcessor() {
        return new CredentialProcessor<CredentialInfo>(
                Collections.singletonList(credential -> {
                    throw new IllegalArgumentException("模拟校验器异常");
                }),
                Collections.emptyList()) {
            @Override
            protected CredentialInfo createInfo() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
