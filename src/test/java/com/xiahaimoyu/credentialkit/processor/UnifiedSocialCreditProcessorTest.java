/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.OrgCategory;
import com.xiahaimoyu.credentialkit.info.UnifiedSocialCreditInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UnifiedSocialCreditProcessorTest {

    private UnifiedSocialCreditProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new UnifiedSocialCreditProcessor();
    }

    @Test
    void validateSuccess() {
        assertThat(processor.validate("91330106MA27Y4U47R").isValid()).isTrue();
    }

    @Test
    void validateSuccessWithLetterOrgCategory() {
        // A1 是有效的OrgCategory（中央军委改革和编制办公室）
        assertThat(processor.validate("A1110000MA27Y4U477").isValid()).isTrue();
    }

    @Test
    void validateFormatError() {
        assertThat(processor.validate("91330106MA27Y4U4").getErrorCode())
                .hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void validateOrgCategoryError() {
        assertThat(processor.validate("88330106MA27Y4U47R").getErrorCode())
                .hasValue(ErrorCode.ORG_CATEGORY_ERROR);
    }

    @Test
    void validateRegionError() {
        assertThat(processor.validate("91880106MA27Y4U47R").getErrorCode())
                .hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void validateOrganizationCodeCheckDigitError() {
        assertThat(processor.validate("91330106MA27Y4U46R").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void validateUnifiedSocialCreditCodeCheckDigitError() {
        assertThat(processor.validate("91330106MA27Y4U47Y").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        Optional<UnifiedSocialCreditInfo> infoOpt = processor.parse("91330106MA27Y4U47R");
        assertThat(infoOpt).isPresent();
        UnifiedSocialCreditInfo info = infoOpt.get();
        assertThat(info.getOrgCategory()).isEqualTo(OrgCategory.MARKET_REGULATION_ENTERPRISE);
        assertThat(info.getRegion().getCode()).isEqualTo("330106");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("西湖区");
        assertThat(info.getOrganizationCode()).isEqualTo("MA27Y4U47");
    }

    @Test
    void parseError() {
        assertThat(processor.parse("91880106MA27Y4U47R")).isEmpty();
    }
}
