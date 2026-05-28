/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MachineReadablePassportProcessorTest {

    private MachineReadablePassportProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new MachineReadablePassportProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateSuccess() {
        assertThat(processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16")).isTrue();
    }

    @Test
    void validateGermanySuccess() {
        assertThat(processor.valid("POD<<ZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16")).isTrue();
    }

    @Test
    void validateFormatError() {
        assertThat(processor.validate("PCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateIssuingRegionError() {
        assertThat(processor.validate("POCXXZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validateNameError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<S<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.NAME_ERROR);
    }

    @Test
    void validatePassportNumberCheckDigitError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476465CHN7304279M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateRegionError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CXX7304279M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validateBirthDateError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7302319M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validateBirthDateCheckDigitError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304278M210126619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateExpirationDateError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210231619203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.EXPIRATION_DATE_ERROR);
    }

    @Test
    void validateExpirationDateCheckDigitError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126719203301<<<<<<16").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validatePersonalNumberCheckDigitError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<26").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateCheckDigitError() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<18").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        Optional<MachineReadablePassportInfo> infoOpt = processor.parse("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16");
        assertThat(infoOpt).isPresent();
        MachineReadablePassportInfo info = infoOpt.get();
        assertThat(info.getIssuingRegion().getChineseShortName()).isEqualTo("中国");
        assertThat(info.getSurname()).isEqualTo("ZHANG");
        assertThat(info.getGivenName()).isEqualTo("SAN");
        assertThat(info.getPassportNumber()).isEqualTo("G48947646");
        assertThat(info.getRegion().getChineseShortName()).isEqualTo("中国");
        assertThat(info.getBirthDate()).isEqualTo("730427");
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        assertThat(info.getExpirationDate()).isEqualTo("210126");
        assertThat(info.getPersonalNumber()).isEqualTo("19203301");
    }

    @Test
    void parseGermanySuccess() {
        Optional<MachineReadablePassportInfo> infoOpt = processor.parse("POD<<ZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16");
        assertThat(infoOpt).isPresent();
        MachineReadablePassportInfo info = infoOpt.get();
        assertThat(info.getIssuingRegion().getChineseShortName()).isEqualTo("德国");
        assertThat(info.getSurname()).isEqualTo("ZHANG");
        assertThat(info.getGivenName()).isEqualTo("SAN");
        assertThat(info.getPassportNumber()).isEqualTo("G48947646");
        assertThat(info.getRegion().getChineseShortName()).isEqualTo("中国");
        assertThat(info.getBirthDate()).isEqualTo("730427");
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        assertThat(info.getExpirationDate()).isEqualTo("210126");
        assertThat(info.getPersonalNumber()).isEqualTo("19203301");
    }

    @Test
    void parseError() {
        assertThat(processor.parse("AOCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16")).isEmpty();
    }
}
