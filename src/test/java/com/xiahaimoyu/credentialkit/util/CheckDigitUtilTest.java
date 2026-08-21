/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CheckDigitUtilTest {

    @Test
    void idCardCheckDigit() {
        assertThat(CheckDigitUtil.getIdCardCheckDigit("33010519781027002")).isEqualTo('5');
        // 15位号码补19世纪生日后：110101+19000101+001
        assertThat(CheckDigitUtil.getIdCardCheckDigit("11010119000101001")).isEqualTo('4');
    }

    @Test
    void idCardCheckDigitRejectsBadInput() {
        assertThatThrownBy(() -> CheckDigitUtil.getIdCardCheckDigit(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CheckDigitUtil.getIdCardCheckDigit("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("长度");
        assertThatThrownBy(() -> CheckDigitUtil.getIdCardCheckDigit("3301051978102700X"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("非法字符");
    }

    @Test
    void unifiedSocialCreditCodeCheckDigit() {
        assertThat(CheckDigitUtil.getUnifiedSocialCreditCodeCheckDigit("91330106MA27Y4U47")).isEqualTo('R');
    }

    @Test
    void unifiedSocialCreditCodeCheckDigitRejectsBadInput() {
        assertThatThrownBy(() -> CheckDigitUtil.getUnifiedSocialCreditCodeCheckDigit(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CheckDigitUtil.getUnifiedSocialCreditCodeCheckDigit("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("长度");
        assertThatThrownBy(() -> CheckDigitUtil.getUnifiedSocialCreditCodeCheckDigit("91330106MA27Y4U4I"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("无效字符");
    }

    @Test
    void organizationCodeCheckDigit() {
        // 组织机构代码8位+校验位，X表示10
        assertThat(CheckDigitUtil.getOrganizationCodeCheckDigit("MA27Y4U4")).isEqualTo('7');
        assertThat(CheckDigitUtil.getOrganizationCodeCheckDigit("12345678")).isEqualTo('8');
    }

    @Test
    void organizationCodeCheckDigitRejectsBadInput() {
        assertThatThrownBy(() -> CheckDigitUtil.getOrganizationCodeCheckDigit(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CheckDigitUtil.getOrganizationCodeCheckDigit("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("长度");
        assertThatThrownBy(() -> CheckDigitUtil.getOrganizationCodeCheckDigit("MA27Y4U-"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("无效字符");
    }

    @Test
    void machineReadablePassportCheckDigit() {
        assertThat(CheckDigitUtil.getMachineReadablePassportCheckDigit("G48947646")).isEqualTo('4');
        assertThat(CheckDigitUtil.getMachineReadablePassportCheckDigit("730427")).isEqualTo('9');
    }

    @Test
    void machineReadablePassportCheckDigitRejectsBadInput() {
        assertThatThrownBy(() -> CheckDigitUtil.getMachineReadablePassportCheckDigit(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CheckDigitUtil.getMachineReadablePassportCheckDigit("G48947646-"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("无效字符");
    }
}
