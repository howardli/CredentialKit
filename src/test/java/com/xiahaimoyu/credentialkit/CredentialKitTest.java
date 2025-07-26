/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdNumberInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialKitTest {

    @Test
    void validSuccess() {
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID_NUMBER, "330105197810270025")).isTrue();
    }

    @Test
    void validError() {
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID_NUMBER, "330105197810270024")).isFalse();
    }

    @Test
    void parseSuccess() {
        MainlandResidentIdNumberInfo info = (MainlandResidentIdNumberInfo) CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID_NUMBER, "330105197810270025");
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
        MainlandResidentIdNumberInfo info = (MainlandResidentIdNumberInfo) CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID_NUMBER, "330105197810270021");
        assertThat(info).isNull();
    }
}