/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportCodeInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MachineReadablePassportCodeProcessorTest {

    private MachineReadablePassportCodeProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new MachineReadablePassportCodeProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateSuccess() {
        assertThatCode(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateGermanySuccess() {
        assertThatCode(() -> processor.valid("POD<<ZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateFormatError() {
        assertThatThrownBy(() -> processor.valid("PCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateIssuingRegionError() {
        assertThatThrownBy(() -> processor.valid("POCXXZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validateNameError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<S<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.NAME_ERROR);
    }

    @Test
    void validatePassportNumberCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476465CHN7304279M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateRegionError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CXX7304279M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validateBirthDateError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7302319M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validateBirthDateCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304278M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateExpirationDateError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210231619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.EXPIRATION_DATE_ERROR);
    }

    @Test
    void validateExpirationDateCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126719203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validatePersonalNumberCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<26"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<18"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        MachineReadablePassportCodeInfo info = processor.parse("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16");
        assertThat(info).isNotNull();
        assertThat(info.getIssuingRegion().getZhShortName()).isEqualTo("中国");
        assertThat(info.getSurname()).isEqualTo("ZHANG");
        assertThat(info.getGivenName()).isEqualTo("SAN");
        assertThat(info.getPassportNumber()).isEqualTo("G48947646");
        assertThat(info.getRegion().getZhShortName()).isEqualTo("中国");
        assertThat(info.getBirthdate()).isEqualTo("730427");
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        assertThat(info.getExpirationDate()).isEqualTo("210126");
        assertThat(info.getPersonalNumber()).isEqualTo("19203301");
    }

    @Test
    void parseGermanySuccess() {
        MachineReadablePassportCodeInfo info = processor.parse("POD<<ZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16");
        assertThat(info).isNotNull();
        assertThat(info.getIssuingRegion().getZhShortName()).isEqualTo("德国");
        assertThat(info.getSurname()).isEqualTo("ZHANG");
        assertThat(info.getGivenName()).isEqualTo("SAN");
        assertThat(info.getPassportNumber()).isEqualTo("G48947646");
        assertThat(info.getRegion().getZhShortName()).isEqualTo("中国");
        assertThat(info.getBirthdate()).isEqualTo("730427");
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        assertThat(info.getExpirationDate()).isEqualTo("210126");
        assertThat(info.getPersonalNumber()).isEqualTo("19203301");
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("AOCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }
}