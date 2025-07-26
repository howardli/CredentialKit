/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.TwResidencePermitNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TwResidencePermitNumberProcessorTest {

    private TwResidencePermitNumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TwResidencePermitNumberProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateSuccess() {
        assertThatCode(() -> processor.valid("830000199201300022"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateBirthDateError() {
        assertThatThrownBy(() -> processor.valid("830000199202310022"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validateCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("830000199201300021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        TwResidencePermitNumberInfo info = processor.parse("830000199201300022");
        assertThat(info.getBirthDate()).isEqualTo("19920130");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("830000199201300021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }
}