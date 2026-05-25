/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.TwTravelPermitNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(processor.valid("12345678")).isTrue();
    }

    @Test
    void validate10Success() {
        assertThat(processor.valid("1234567890")).isTrue();
    }

    @Test
    void validateFormatError() {
        assertThat(processor.validate("123456789").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parse8Success() {
        Optional<TwTravelPermitNumberInfo> infoOpt = processor.parse("12345678");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parse10Success() {
        Optional<TwTravelPermitNumberInfo> infoOpt = processor.parse("1234567890");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getReplacementTime()).isEqualTo(90);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("123456789")).isEmpty();
    }
}
