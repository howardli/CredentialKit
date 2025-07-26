/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.HkMoTravelPermitNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HkMoTravelPermitNumberProcessorTest {

    private HkMoTravelPermitNumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new HkMoTravelPermitNumberProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validate8Success() {
        assertThatCode(() -> processor.valid("H12345678"))
                .doesNotThrowAnyException();
    }

    @Test
    void validate10Success() {
        assertThatCode(() -> processor.valid("M1234567801"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateFormatError() {
        assertThatThrownBy(() -> processor.valid("A1234567801"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parseHk8Success() {
        HkMoTravelPermitNumberInfo info = processor.parse("H12345678");
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parseMo8Success() {
        HkMoTravelPermitNumberInfo info = processor.parse("M12345678");
        assertThat(info.getRegion().getProvince()).isEqualTo("澳门特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parseHk10Success() {
        HkMoTravelPermitNumberInfo info = processor.parse("H1234567802");
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getReplacementTime()).isEqualTo(2);
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("K1234567802"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }
}