/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationResultTest {

    @Test
    void success() {
        ValidationResult result = ValidationResult.success();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorCode()).isEmpty();
        assertThat(result.getErrorDescription()).isEmpty();
        assertThat(result.toString()).contains("valid=true");
    }

    @Test
    void failureIsCachedPerErrorCode() {
        ValidationResult a = ValidationResult.failure(ErrorCode.CHECK_DIGIT_ERROR);
        ValidationResult b = ValidationResult.failure(ErrorCode.CHECK_DIGIT_ERROR);
        assertThat(a.isValid()).isFalse();
        assertThat(a).isSameAs(b);
        assertThat(a.getErrorCode()).hasValue(ErrorCode.CHECK_DIGIT_ERROR);
        assertThat(a.getErrorDescription()).isEqualTo("[CHECK_DIGIT_ERROR] 校验位错误");
        assertThat(a.toString()).contains("CHECK_DIGIT_ERROR");
    }

    @Test
    void failureRejectsNullErrorCode() {
        assertThatThrownBy(() -> ValidationResult.failure(null))
                .isInstanceOf(NullPointerException.class);
    }
}
