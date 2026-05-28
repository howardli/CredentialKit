/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.HkMacaoTravelPermitInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HkMacaoTravelPermitProcessorTest {

    private HkMacaoTravelPermitProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new HkMacaoTravelPermitProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validate8Success() {
        assertThat(processor.valid("H12345678")).isTrue();
    }

    @Test
    void validate10Success() {
        assertThat(processor.valid("M1234567801")).isTrue();
    }

    @Test
    void validateFormatError() {
        assertThat(processor.validate("A1234567801").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parseHk8Success() {
        Optional<HkMacaoTravelPermitInfo> infoOpt = processor.parse("H12345678");
        assertThat(infoOpt).isPresent();
        HkMacaoTravelPermitInfo info = infoOpt.get();
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parseMo8Success() {
        Optional<HkMacaoTravelPermitInfo> infoOpt = processor.parse("M12345678");
        assertThat(infoOpt).isPresent();
        HkMacaoTravelPermitInfo info = infoOpt.get();
        assertThat(info.getRegion().getProvince()).isEqualTo("澳门特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getReplacementTime()).isEqualTo(-1);
    }

    @Test
    void parseHk10Success() {
        Optional<HkMacaoTravelPermitInfo> infoOpt = processor.parse("H1234567802");
        assertThat(infoOpt).isPresent();
        HkMacaoTravelPermitInfo info = infoOpt.get();
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getReplacementTime()).isEqualTo(2);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("K1234567802")).isEmpty();
    }
}
