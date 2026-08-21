/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenderTest {

    @Test
    void fromDigit() {
        assertThat(Gender.fromDigit(0)).isEqualTo(Gender.FEMALE);
        assertThat(Gender.fromDigit(2)).isEqualTo(Gender.FEMALE);
        assertThat(Gender.fromDigit(1)).isEqualTo(Gender.MALE);
        assertThat(Gender.fromDigit(9)).isEqualTo(Gender.MALE);
    }

    @Test
    void getDesc() {
        assertThat(Gender.MALE.getDesc()).isEqualTo("男");
        assertThat(Gender.FEMALE.getDesc()).isEqualTo("女");
        assertThat(Gender.UNKNOWN.getDesc()).isEqualTo("未知");
    }
}
