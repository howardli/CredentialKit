/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.HkMacaoResidencePermitInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HkMacaoResidencePermitProcessorTest {

    private HkMacaoResidencePermitProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new HkMacaoResidencePermitProcessor();
    }

    @Test
    void validateSuccess() {
        assertThat(processor.validate("810000199408230021").isValid()).isTrue();
    }

    @Test
    void validateBirthDateError() {
        assertThat(processor.validate("810000199402310021").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validateCheckDigitError() {
        assertThat(processor.validate("810000199408230022").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseHkSuccess() {
        Optional<HkMacaoResidencePermitInfo> infoOpt = processor.parse("810000199408230021");
        assertThat(infoOpt).isPresent();
        HkMacaoResidencePermitInfo info = infoOpt.get();
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("19940823");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseMoSuccess() {
        Optional<HkMacaoResidencePermitInfo> infoOpt = processor.parse("820000199408230023");
        assertThat(infoOpt).isPresent();
        HkMacaoResidencePermitInfo info = infoOpt.get();
        assertThat(info.getRegion().getProvince()).isEqualTo("澳门特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("19940823");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("820000199408230022")).isEmpty();
    }
}
