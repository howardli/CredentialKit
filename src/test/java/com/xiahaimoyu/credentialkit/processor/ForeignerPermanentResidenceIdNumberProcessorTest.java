/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.ForeignerPermanentResidenceIdNumberInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(processor.valid("KAZ110090123105")).isTrue();
    }

    @Test
    void validate15InternationalRegionError() {
        assertThat(processor.validate("KAA110090123105").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate15DomesticRegionError() {
        assertThat(processor.validate("KAZ990090123105").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate15BirthDateError() {
        assertThat(processor.validate("KAZ110090023105").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate15CheckDigitError() {
        assertThat(processor.validate("KAZ110090123107").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validate18Success() {
        assertThat(processor.valid("911398199012310021")).isTrue();
    }

    @Test
    void validate18DomesticRegionError() {
        assertThat(processor.validate("999398199012310021").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }


    @Test
    void validate18InternationalRegionError() {
        assertThat(processor.validate("911999199012310021").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validate18BirthDateError() {
        assertThat(processor.validate("911398199002310021").getErrorCode())
                .hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void validate18CheckDigitError() {
        assertThat(processor.validate("911398199012310022").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parse15Success() {
        Optional<ForeignerPermanentResidenceIdNumberInfo> infoOpt = processor.parse("KAZ110090123105");
        assertThat(infoOpt).isPresent();
        ForeignerPermanentResidenceIdNumberInfo info = infoOpt.get();
        assertThat(info.getInternationalRegionInfo().getZhShortName()).isEqualTo("哈萨克斯坦");
        assertThat(info.getDomesticRegionInfo().getProvince()).isEqualTo("北京市");
        assertThat(info.getDomesticRegionInfo().getCity()).isNull();
        assertThat(info.getDomesticRegionInfo().getCounty()).isNull();
        assertThat(info.getBirthDate()).isEqualTo("901231");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parse18Success() {
        Optional<ForeignerPermanentResidenceIdNumberInfo> infoOpt = processor.parse("911398199012310021");
        assertThat(infoOpt).isPresent();
        ForeignerPermanentResidenceIdNumberInfo info = infoOpt.get();
        assertThat(info.getDomesticRegionInfo().getProvince()).isEqualTo("北京市");
        assertThat(info.getDomesticRegionInfo().getCity()).isNull();
        assertThat(info.getDomesticRegionInfo().getCounty()).isNull();
        assertThat(info.getInternationalRegionInfo().getZhShortName()).isEqualTo("哈萨克斯坦");
        assertThat(info.getBirthDate()).isEqualTo("19901231");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("911398199012310020")).isEmpty();
    }
}
