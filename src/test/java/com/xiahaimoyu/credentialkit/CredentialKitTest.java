/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdInfo;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialKitTest {

    @Test
    void validateSuccess() {
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025").isValid()).isTrue();
    }

    @Test
    void validateError() {
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270024").getErrorCode())
                .hasValue(ErrorCode.CHECK_DIGIT_ERROR);
    }

    @Test
    void parseSuccess() {
        Optional<? extends CredentialInfo> infoOpt = CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025");
        assertThat(infoOpt).isPresent();
        MainlandResidentIdInfo info = (MainlandResidentIdInfo) infoOpt.get();
        assertThat(info.getRegion().getCode()).isEqualTo("330105");
        assertThat(info.getRegion().getProvince()).isEqualTo("浙江省");
        assertThat(info.getRegion().getCity()).isEqualTo("杭州市");
        assertThat(info.getRegion().getCounty()).isEqualTo("拱墅区");
        assertThat(info.getBirthDate()).isEqualTo("19781027");
        assertThat(info.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void parseError() {
        Optional<? extends CredentialInfo> infoOpt = CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270021");
        assertThat(infoOpt).isEmpty();
    }

    // 智能识别测试
    @Test
    void detectMainlandResidentId() {
        List<CredentialType> types = CredentialKit.detect("330105197810270025");
        assertThat(types).contains(DefaultCredentialType.MAINLAND_RESIDENT_ID);
    }

    @Test
    void detectHkMacaoResidencePermit() {
        List<CredentialType> types = CredentialKit.detect("810000199001010019");
        assertThat(types).contains(DefaultCredentialType.HK_MACAO_RESIDENCE_PERMIT);
    }

    @Test
    void detectTaiwanResidencePermit() {
        List<CredentialType> types = CredentialKit.detect("830000199001010012");
        assertThat(types).contains(DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT);
    }

    @Test
    void detectHkMacaoTravelPermit() {
        List<CredentialType> types = CredentialKit.detect("H12345678");
        assertThat(types).contains(DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT);
    }

    @Test
    void detectUnifiedSocialCredit() {
        List<CredentialType> types = CredentialKit.detect("91330106MA27Y4U47R");
        assertThat(types).contains(DefaultCredentialType.UNIFIED_SOCIAL_CREDIT);
    }

    @Test
    void detectUnknown() {
        List<CredentialType> types = CredentialKit.detect("invalid");
        assertThat(types).isEmpty();
    }

    @Test
    void detectSingleType() {
        // 大陆居民身份证应该只匹配一个类型
        List<CredentialType> types = CredentialKit.detect("330105197810270025");
        assertThat(types).hasSize(1);
        assertThat(types.get(0)).isEqualTo(DefaultCredentialType.MAINLAND_RESIDENT_ID);
    }

    // ==================== 边界条件测试 ====================

    @Test
    void nullInput() {
        assertThat(CredentialKit.detect(null)).isEmpty();
    }

    @Test
    void emptyInput() {
        assertThat(CredentialKit.detect("")).isEmpty();
    }

    @Test
    void whitespaceOnlyInput() {
        assertThat(CredentialKit.detect("   ")).isEmpty();
    }

    @Test
    void invalidCharacters() {
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "33010519781027002@").isValid()).isFalse();
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "33010519781027002测").isValid()).isFalse();
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "33010519781027002 5").isValid()).isFalse();
    }

    @Test
    void tooShortInput() {
        assertThat(CredentialKit.detect("330105")).isEmpty();
    }

    @Test
    void tooLongInput() {
        assertThat(CredentialKit.detect("330105197810270025000")).isEmpty();
    }

    @Test
    void futureBirthDate() {
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105209910270025").isValid()).isFalse();
        ValidationResult result = CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105209910270025");
        assertThat(result.getErrorCode()).hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void invalidBirthDate() {
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197813270025").isValid()).isFalse();
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810000025").isValid()).isFalse();
    }

    @Test
    void invalidRegionCode() {
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "999999197810270025").isValid()).isFalse();
        ValidationResult result = CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "999999197810270025");
        assertThat(result.getErrorCode()).hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void getTypeFromParsedInfo() {
        Optional<? extends CredentialInfo> infoOpt = CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getType()).isEqualTo(DefaultCredentialType.MAINLAND_RESIDENT_ID);
    }
}
