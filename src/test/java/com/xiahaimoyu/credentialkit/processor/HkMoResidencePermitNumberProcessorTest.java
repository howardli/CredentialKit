/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.HkMoResidencePermitNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HkMoResidencePermitNumberProcessorTest {

    private HkMoResidencePermitNumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new HkMoResidencePermitNumberProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateSuccess() {
        assertThatCode(() -> processor.valid("810000199408230021"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateBirthDateError() {
        assertThatThrownBy(() -> processor.valid("810000199402310021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validateCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("810000199408230022"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseHkSuccess() {
        HkMoResidencePermitNumberInfo info = processor.parse("810000199408230021");
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("19940823");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseMoSuccess() {
        HkMoResidencePermitNumberInfo info = processor.parse("820000199408230023");
        assertThat(info.getRegion().getProvince()).isEqualTo("澳门特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("19940823");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("820000199408230022"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }
}