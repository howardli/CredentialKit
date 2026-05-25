/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.HkMoResidencePermitNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(processor.valid("810000199408230021")).isTrue();
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
        Optional<HkMoResidencePermitNumberInfo> infoOpt = processor.parse("810000199408230021");
        assertThat(infoOpt).isPresent();
        HkMoResidencePermitNumberInfo info = infoOpt.get();
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("19940823");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseMoSuccess() {
        Optional<HkMoResidencePermitNumberInfo> infoOpt = processor.parse("820000199408230023");
        assertThat(infoOpt).isPresent();
        HkMoResidencePermitNumberInfo info = infoOpt.get();
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
