/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.TaiwanTravelPermitInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaiwanTravelPermitProcessorTest {

    private TaiwanTravelPermitProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TaiwanTravelPermitProcessor();
    }

    @Test
    void validate8Success() {
        assertThat(processor.validate("12345678").isValid()).isTrue();
    }

    @Test
    void validate10Success() {
        assertThat(processor.validate("1234567890").isValid()).isTrue();
    }

    @Test
    void validateFormatError() {
        assertThat(processor.validate("123456789").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parse8Success() {
        Optional<TaiwanTravelPermitInfo> infoOpt = processor.parse("12345678");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parse10Success() {
        Optional<TaiwanTravelPermitInfo> infoOpt = processor.parse("1234567890");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getReplacementTime()).isEqualTo(90);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("123456789")).isEmpty();
    }
}
