/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.ForeignerPermanentResidenceIdNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ForeignerPermanentResidenceIdNumberProcessorTest {

    private ForeignerPermanentResidenceIdNumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ForeignerPermanentResidenceIdNumberProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validate15Success() {
        assertThatCode(() -> processor.valid("KAZ110090123105"))
                .doesNotThrowAnyException();
    }

    @Test
    void validate15InternationalRegionError() {
        assertThatThrownBy(() -> processor.valid("KAA110090123105"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate15DomesticRegionError() {
        assertThatThrownBy(() -> processor.valid("KAZ990090123105"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate15BirthDateError() {
        assertThatThrownBy(() -> processor.valid("KAZ110090023105"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate15CheckDigitError() {
        assertThatThrownBy(() -> processor.valid("KAZ110090123107"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validate18Success() {
        assertThatCode(() -> processor.valid("911398199012310021"))
                .doesNotThrowAnyException();
    }

    @Test
    void validate18DomesticRegionError() {
        assertThatThrownBy(() -> processor.valid("999398199012310021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }


    @Test
    void validate18InternationalRegionError() {
        assertThatThrownBy(() -> processor.valid("911999199012310021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate18BirthDateError() {
        assertThatThrownBy(() -> processor.valid("911398199002310021"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate18CheckDigitError() {
        assertThatThrownBy(() -> processor.valid("911398199012310022"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parse15Success() {
        ForeignerPermanentResidenceIdNumberInfo info = processor.parse("KAZ110090123105");
        assertThat(info).isNotNull();
        assertThat(info.getInternationalRegionInfo().getZhShortName()).isEqualTo("哈萨克斯坦");
        assertThat(info.getDomesticRegionInfo().getProvince()).isEqualTo("北京市");
        assertThat(info.getDomesticRegionInfo().getCity()).isNull();
        assertThat(info.getDomesticRegionInfo().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("901231");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parse18Success() {
        ForeignerPermanentResidenceIdNumberInfo info = processor.parse("911398199012310021");
        assertThat(info).isNotNull();
        assertThat(info.getDomesticRegionInfo().getProvince()).isEqualTo("北京市");
        assertThat(info.getDomesticRegionInfo().getCity()).isNull();
        assertThat(info.getDomesticRegionInfo().getCounty()).isNull();
        assertThat(info.getInternationalRegionInfo().getZhShortName()).isEqualTo("哈萨克斯坦");
        assertThat(info.getBirthDate()).isEqualTo("19901231");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("911398199012310020"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }
}