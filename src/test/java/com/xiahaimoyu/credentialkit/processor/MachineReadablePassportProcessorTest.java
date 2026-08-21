/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
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

    @Test
    void validateSuccess() {
        assertThat(processor.validate("POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16").isValid()).isTrue();
    }

    @Test
    void validateGermanySuccess() {
        assertThat(processor.validate("POD<<ZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16").isValid()).isTrue();
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
        assertThat(info.getBirthDate()).isEqualTo("19730427");
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        assertThat(info.getExpirationDate()).isEqualTo("20210126");
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
        assertThat(info.getBirthDate()).isEqualTo("19730427");
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        assertThat(info.getExpirationDate()).isEqualTo("20210126");
        assertThat(info.getPersonalNumber()).isEqualTo("19203301");
    }

    @Test
    void parseUnknownGender() {
        // 性别位为<（未指定）时应解析为UNKNOWN；复合校验位按规则重算
        String mrz = "POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279<210126619203301<<<<<<16";
        // 重新计算受影响的校验位：生日校验位、复合校验位
        String birthDate = mrz.substring(57, 63);
        char birthCheck = CheckDigitUtil.getMachineReadablePassportCheckDigit(birthDate);
        mrz = mrz.substring(0, 63) + birthCheck + mrz.substring(64);
        String compositeInput = mrz.substring(44, 54) + mrz.substring(57, 64) + mrz.substring(65, 87);
        char compositeCheck = CheckDigitUtil.getMachineReadablePassportCheckDigit(compositeInput);
        mrz = mrz.substring(0, 87) + compositeCheck;

        Optional<MachineReadablePassportInfo> infoOpt = processor.parse(mrz);
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getGender()).isEqualTo(Gender.UNKNOWN);
    }

    @Test
    void parseSurnameOnly() {
        // 姓名字段只有姓（无<<分隔的名）：givenName保持null，姓名在第1行不影响第2行校验位
        StringBuilder mrz = new StringBuilder("POCHN");
        mrz.append("ZHANG");
        for (int i = 0; i < 34; i++) {
            mrz.append('<');
        }
        mrz.append("G489476464CHN7304279M210126619203301<<<<<<16");
        Optional<MachineReadablePassportInfo> infoOpt = processor.parse(mrz.toString());
        assertThat(infoOpt).isPresent();
        MachineReadablePassportInfo info = infoOpt.get();
        assertThat(info.getSurname()).isEqualTo("ZHANG");
        assertThat(info.getGivenName()).isNull();
        assertThat(info.getGender()).isEqualTo(Gender.MALE);
        // 两次解析的givenName均为null时equals仍应成立
        assertThat(processor.parse(mrz.toString())).contains(info);
    }

    @Test
    void parseError() {
        assertThat(processor.parse("AOCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16")).isEmpty();
    }
}
