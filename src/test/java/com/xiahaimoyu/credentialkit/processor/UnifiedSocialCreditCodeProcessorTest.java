/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.OrgCategory;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.UnifiedSocialCreditCodeInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UnifiedSocialCreditCodeProcessorTest {

    private UnifiedSocialCreditCodeProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new UnifiedSocialCreditCodeProcessor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateSuccess() {
        assertThatCode(() -> processor.valid("91330106MA27Y4U47R"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateFormatError() {
        assertThatThrownBy(() -> processor.valid("X1330106MA27Y4U47R"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateOrgCategoryError() {
        assertThatThrownBy(() -> processor.valid("88330106MA27Y4U47R"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ORG_CATEGORY_ERROR);
    }

    @Test
    void validateRegionError() {
        assertThatThrownBy(() -> processor.valid("91880106MA27Y4U47R"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }

    @Test
    void validateOrganizationCodeCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("91330106MA27Y4U46R"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateUnifiedSocialCreditCodeCheckDigitError() {
        assertThatThrownBy(() -> processor.valid("91330106MA27Y4U47Y"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        UnifiedSocialCreditCodeInfo info = processor.parse("91330106MA27Y4U47R");
        assertThat(info).isNotNull();
        assertThat(info.getOrgCategory()).isEqualTo(OrgCategory.MARKET_REGULATION_ENTERPRISE);
        assertThat(info.getRegion().getCode()).isEqualTo("330106");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("西湖区");
        assertThat(info.getOrganizationCode()).isEqualTo("MA27Y4U47");
    }

    @Test
    void parseError() {
        assertThatThrownBy(() -> processor.parse("91880106MA27Y4U47R"))
                .isInstanceOf(CredentialException.class)
                .extracting(ex -> ((CredentialException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REGION_ERROR);
    }
}