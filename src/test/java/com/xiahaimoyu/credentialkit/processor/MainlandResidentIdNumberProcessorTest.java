/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MainlandResidentIdNumberProcessorTest {

    private MainlandResidentIdNumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new MainlandResidentIdNumberProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validate18Success() {
        assertThatCode(() -> processor.valid("110101197810270029"))
                .doesNotThrowAnyException();
    }

    @Test
    void validate15Success() {
        assertThatCode(() -> processor.valid("110101781027002"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateFormatError() {
        assertThatThrownBy(() -> processor.valid("1101011978102700291"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateRegionError() {
        assertThatThrownBy(() -> processor.valid("880101197810270029"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate18BirthDateError() {
        assertThatThrownBy(() -> processor.valid("110101197802310029"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate15BirthDateError() {
        assertThatThrownBy(() -> processor.valid("110101780231002"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate18CheckDigitError() {
        assertThatThrownBy(() -> processor.valid("110101197810270021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parse15Success() {
        MainlandResidentIdNumberInfo info = processor.parse("330105781027002");
        assertThat(info).isNotNull();
        assertThat(info.getRegion().getCode()).isEqualTo("330105");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("拱墅区");
        assertThat(info.getBirthDate()).isEqualTo("19781027");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parse18Success() {
        MainlandResidentIdNumberInfo info = processor.parse("330105197810270025");
        assertThat(info).isNotNull();
        assertThat(info.getRegion().getCode()).isEqualTo("330105");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("拱墅区");
        assertThat(info.getBirthDate()).isEqualTo("19781027");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("3301051978102700251"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }
}