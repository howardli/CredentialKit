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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialKitTest {

    @Test
    void validSuccess() {
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025")).isTrue();
    }

    @Test
    void validError() {
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270024")).isFalse();
    }

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
    void detectMultipleTypes() {
        // 测试可能匹配多个类型的情况
        List<CredentialType> types = CredentialKit.detect("330105197810270025");
        assertThat(types).isNotEmpty();
        assertThat(types.get(0)).isEqualTo(DefaultCredentialType.MAINLAND_RESIDENT_ID);
    }

    // 自动识别校验测试
    @Test
    void validAutoDetect() {
        assertThat(CredentialKit.valid("330105197810270025")).isTrue();
    }

    @Test
    void validateAutoDetect() {
        ValidationResult result = CredentialKit.validate("330105197810270025");
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void validateAutoDetectUnknown() {
        ValidationResult result = CredentialKit.validate("invalid");
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorCode()).hasValue(ErrorCode.BASIC_FORMAT_ERROR);
        assertThat(result.getMessage()).hasValue("无法识别证件类型");
    }

    // 批量操作测试
    @Test
    void validateBatch() {
        List<String> credentials = Arrays.asList(
                "330105197810270025",
                "330105197810270024"
        );
        List<ValidationResult> results = CredentialKit.validateBatch(credentials);
        assertThat(results).hasSize(2);
        assertThat(results.get(0).isValid()).isTrue();
        assertThat(results.get(1).isValid()).isFalse();
    }

    @Test
    void parseBatch() {
        List<String> credentials = Arrays.asList(
                "330105197810270025",
                "invalid"
        );
        Map<String, Optional<? extends CredentialInfo>> results = CredentialKit.parseBatch(credentials);
        assertThat(results).hasSize(2);
        assertThat(results.get("330105197810270025")).isPresent();
        assertThat(results.get("invalid")).isEmpty();
    }

    @Test
    void getTypeStatistics() {
        List<String> credentials = Arrays.asList(
                "330105197810270025",
                "330105197810270025",
                "H12345678",
                "M12345678",
                "invalid"
        );
        Map<CredentialType, Long> stats = CredentialKit.getTypeStatistics(credentials);
        assertThat(stats.get(DefaultCredentialType.MAINLAND_RESIDENT_ID)).isEqualTo(2L);
        assertThat(stats.get(DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT)).isEqualTo(2L);
    }

    // 规格化测试
    @Test
    void normalize() {
        assertThat(CredentialKit.normalize("  h12345678  ")).isEqualTo("H12345678");
        assertThat(CredentialKit.normalize(null)).isNull();
    }

    // 注册支持测试
    @Test
    void isSupported() {
        assertThat(CredentialKit.isSupported(DefaultCredentialType.MAINLAND_RESIDENT_ID)).isTrue();
        assertThat(CredentialKit.isSupported(null)).isFalse();
    }

    @Test
    void getRegisteredTypes() {
        assertThat(CredentialKit.getRegisteredTypes()).contains(
                DefaultCredentialType.MAINLAND_RESIDENT_ID,
                DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT,
                DefaultCredentialType.UNIFIED_SOCIAL_CREDIT
        );
    }

    // ==================== 边界条件测试 ====================

    @Test
    void nullInput() {
        assertThat(CredentialKit.valid(null)).isFalse();
        assertThat(CredentialKit.detect(null)).isEmpty();
        assertThat(CredentialKit.parse(null)).isEmpty();
        assertThat(CredentialKit.validate(null).isValid()).isFalse();
    }

    @Test
    void emptyInput() {
        assertThat(CredentialKit.valid("")).isFalse();
        assertThat(CredentialKit.detect("")).isEmpty();
        assertThat(CredentialKit.parse("")).isEmpty();
    }

    @Test
    void whitespaceOnlyInput() {
        assertThat(CredentialKit.valid("   ")).isFalse();
        assertThat(CredentialKit.detect("   ")).isEmpty();
    }

    @Test
    void invalidCharacters() {
        // 包含特殊字符
        assertThat(CredentialKit.valid("33010519781027002@")).isFalse();
        // 包含中文
        assertThat(CredentialKit.valid("33010519781027002测")).isFalse();
        // 包含空格
        assertThat(CredentialKit.valid("33010519781027002 5")).isFalse();
    }

    @Test
    void tooShortInput() {
        assertThat(CredentialKit.valid("330105")).isFalse();
        assertThat(CredentialKit.detect("330105")).isEmpty();
    }

    @Test
    void tooLongInput() {
        // 超长输入
        assertThat(CredentialKit.valid("330105197810270025000")).isFalse();
        assertThat(CredentialKit.detect("330105197810270025000")).isEmpty();
    }

    @Test
    void futureBirthDate() {
        // 未来生日（不合法）
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105209910270025")).isFalse();
        ValidationResult result = CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105209910270025");
        assertThat(result.getErrorCode()).hasValue(ErrorCode.BIRTH_DATE_ERROR);
    }

    @Test
    void invalidBirthDate() {
        // 无效日期（13月）
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197813270025")).isFalse();
        // 无效日期（00日）
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810000025")).isFalse();
    }

    @Test
    void invalidRegionCode() {
        // 无效地区码
        assertThat(CredentialKit.valid(DefaultCredentialType.MAINLAND_RESIDENT_ID, "999999197810270025")).isFalse();
        ValidationResult result = CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "999999197810270025");
        assertThat(result.getErrorCode()).hasValue(ErrorCode.REGION_ERROR);
    }

    @Test
    void getTypeFromParsedInfo() {
        Optional<? extends CredentialInfo> infoOpt = CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025");
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getType()).isEqualTo(DefaultCredentialType.MAINLAND_RESIDENT_ID);
    }

    @Test
    void batchWithNullAndEmpty() {
        List<String> credentials = Arrays.asList("330105197810270025", null, "", "invalid");
        List<ValidationResult> results = CredentialKit.validateBatch(credentials);
        assertThat(results).hasSize(4);
        assertThat(results.get(0).isValid()).isTrue();
        assertThat(results.get(1).isValid()).isFalse();
        assertThat(results.get(2).isValid()).isFalse();
        assertThat(results.get(3).isValid()).isFalse();
    }
}
