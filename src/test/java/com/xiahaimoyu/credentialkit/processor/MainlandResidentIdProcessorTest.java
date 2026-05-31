/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MainlandResidentIdProcessorTest {

    private MainlandResidentIdProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new MainlandResidentIdProcessor();
    }

    @Test
    void validate18Success() {
        assertThat(processor.validate("110101197810270029").isValid()).isTrue();
    }

    @Test
    void validate15Success() {
        assertThat(processor.validate("110101781027002").isValid()).isTrue();
    }

    @Test
    void validateFormatError() {
        assertThat(processor.validate("1101011978102700291").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateRegionError() {
        assertThat(processor.validate("880101197810270029").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate18BirthDateError() {
        assertThat(processor.validate("110101197802310029").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate15BirthDateError() {
        assertThat(processor.validate("110101780231002").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate18CheckDigitError() {
        assertThat(processor.validate("110101197810270021").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parse15Success() {
        Optional<MainlandResidentIdInfo> infoOpt = processor.parse("330105781027002");
        assertThat(infoOpt).isPresent();
        MainlandResidentIdInfo info = infoOpt.get();
        assertThat(info.getRegion().getCode()).isEqualTo("330105");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("拱墅区");
        assertThat(info.getBirthDate()).isEqualTo("19781027");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parse18Success() {
        Optional<MainlandResidentIdInfo> infoOpt = processor.parse("330105197810270025");
        assertThat(infoOpt).isPresent();
        MainlandResidentIdInfo info = infoOpt.get();
        assertThat(info.getRegion().getCode()).isEqualTo("330105");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("拱墅区");
        assertThat(info.getBirthDate()).isEqualTo("19781027");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("3301051978102700251")).isEmpty();
    }
}
