/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.HkMacaoTravelPermitNonChineseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HkMacaoTravelPermitNonChineseProcessorTest {

    private HkMacaoTravelPermitNonChineseProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new HkMacaoTravelPermitNonChineseProcessor();
    }

    @Test
    void validateHkSuccess() {
        assertThat(processor.validate("HA1234567").isValid()).isTrue();
    }

    @Test
    void validateMoSuccess() {
        assertThat(processor.validate("MA7654321").isValid()).isTrue();
    }

    @Test
    void validateChinesePermitNumberRejected() {
        // 中国籍通行证号码（H/M + 8位数字）不应命中非中国籍类型
        assertThat(processor.validate("H12345678").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateFormatError() {
        // 字头只能是HA/MA
        assertThat(processor.validate("HB1234567").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateLengthError() {
        assertThat(processor.validate("HA12345678").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
        assertThat(processor.validate("HA123456").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parseHkSuccess() {
        Optional<HkMacaoTravelPermitNonChineseInfo> infoOpt = processor.parse("HA1234567");
        assertThat(infoOpt).isPresent();
        HkMacaoTravelPermitNonChineseInfo info = infoOpt.get();
        assertThat(info.getRegion().getCode()).isEqualTo("HA");
        assertThat(info.getRegion().getProvince()).isEqualTo("香港特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
    }

    @Test
    void parseMoSuccess() {
        Optional<HkMacaoTravelPermitNonChineseInfo> infoOpt = processor.parse("MA7654321");
        assertThat(infoOpt).isPresent();
        HkMacaoTravelPermitNonChineseInfo info = infoOpt.get();
        assertThat(info.getRegion().getCode()).isEqualTo("MA");
        assertThat(info.getRegion().getProvince()).isEqualTo("澳门特别行政区");
        assertThat(info.getRegion().getCity()).isNull();
        assertThat(info.getRegion().getCounty()).isNull();
    }

    @Test
    void parseError() {
        assertThat(processor.parse("HA123456")).isEmpty();
        assertThat(processor.parse("KZ1234567")).isEmpty();
        assertThat(processor.parse(null)).isEmpty();
    }
}
