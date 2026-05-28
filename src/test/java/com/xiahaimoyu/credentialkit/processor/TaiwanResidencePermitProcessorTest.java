/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.TaiwanResidencePermitInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaiwanResidencePermitProcessorTest {

    private TaiwanResidencePermitProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TaiwanResidencePermitProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateSuccess() {
        assertThat(processor.valid("830000199201300022")).isTrue();
    }

    @Test
    void validateBirthDateError() {
        assertThat(processor.validate("830000199202310022").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validateCheckDigitError() {
        assertThat(processor.validate("830000199201300021").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        Optional<TaiwanResidencePermitInfo> infoOpt = processor.parse("830000199201300022");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getBirthDate()).isEqualTo("19920130");
        assertThat(infoOpt.get().getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("830000199201300021")).isEmpty();
    }
}
