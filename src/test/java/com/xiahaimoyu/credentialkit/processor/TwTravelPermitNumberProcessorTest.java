/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.TwTravelPermitNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TwTravelPermitNumberProcessorTest {

    private TwTravelPermitNumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TwTravelPermitNumberProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validate8Success() {
        assertThatCode(() -> processor.valid("12345678"))
                .doesNotThrowAnyException();
    }

    @Test
    void validate10Success() {
        assertThatCode(() -> processor.valid("1234567890"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateFormatError() {
        assertThatThrownBy(() -> processor.valid("123456789"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parse8Success() {
        TwTravelPermitNumberInfo info = processor.parse("12345678");
        assertThat(info.getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parse10Success() {
        TwTravelPermitNumberInfo info = processor.parse("1234567890");
        assertThat(info.getReplacementTime()).isEqualTo(90);
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("123456789"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }
}