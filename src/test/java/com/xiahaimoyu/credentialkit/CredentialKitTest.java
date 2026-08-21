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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void typedParseSuccess() {
        Optional<MainlandResidentIdInfo> infoOpt =
                CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025", MainlandResidentIdInfo.class);
        assertThat(infoOpt).isPresent();
        assertThat(infoOpt.get().getBirthDate()).isEqualTo("19781027");
    }

    @Test
    void typedParseError() {
        Optional<MainlandResidentIdInfo> infoOpt =
                CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270024", MainlandResidentIdInfo.class);
        assertThat(infoOpt).isEmpty();
    }

    @Test
    void typedParseWrongInfoClass() {
        assertThatThrownBy(() ->
                CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025", com.xiahaimoyu.credentialkit.info.UnifiedSocialCreditInfo.class))
                .isInstanceOf(ClassCastException.class);
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

    // ==================== null 处理一致性测试 ====================

    @Test
    void validateNullCredentialReturnsFailure() {
        ValidationResult result = CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, null);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorCode()).hasValue(ErrorCode.BASIC_FORMAT_ERROR);
    }

    @Test
    void parseNullCredentialReturnsEmpty() {
        Optional<? extends CredentialInfo> infoOpt = CredentialKit.parse(DefaultCredentialType.MAINLAND_RESIDENT_ID, null);
        assertThat(infoOpt).isEmpty();
    }

    // ==================== detect 排序确定性测试 ====================

    @Test
    void detectReturnsDeterministicOrder() {
        // 同一个输入多次调用应返回一致的顺序
        List<CredentialType> types1 = CredentialKit.detect("830000199201300022");
        List<CredentialType> types2 = CredentialKit.detect("830000199201300022");
        assertThat(types1).isEqualTo(types2);
        assertThat(types1).contains(DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT);
    }

    // ==================== 实例模式测试 ====================

    @Test
    void createRegistersAllBuiltInTypes() {
        CredentialRegistry kit = CredentialRegistry.create();
        Set<CredentialType> supported = kit.getSupportedTypes();
        assertThat(supported).containsExactlyInAnyOrderElementsOf(CredentialKit.getDefault().getSupportedTypes());
        assertThat(kit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025").isValid()).isTrue();
    }

    @Test
    void createEmptyHasNoProcessors() {
        CredentialRegistry kit = CredentialRegistry.createEmpty();
        assertThat(kit.getSupportedTypes()).isEmpty();
        assertThatThrownBy(() -> kit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void instanceRegistryIsIsolatedFromDefault() {
        CredentialRegistry kit = CredentialRegistry.createEmpty();
        kit.register(TestCredentialType.TEST_ID, new TestCredentialProcessor());
        // 实例可校验自定义类型，默认实例不受影响
        assertThat(kit.validate(TestCredentialType.TEST_ID, "12345678").isValid()).isTrue();
        // 基类CredentialInfo的toString返回类名简写
        assertThat(kit.parse(TestCredentialType.TEST_ID, "12345678").get().toString()).isEqualTo("TestCredentialInfo");
        assertThatThrownBy(() -> CredentialKit.validate(TestCredentialType.TEST_ID, "12345678"))
                .isInstanceOf(UnsupportedOperationException.class);

        // 实例上注销不影响默认实例
        kit.unregister(TestCredentialType.TEST_ID);
        assertThatThrownBy(() -> kit.validate(TestCredentialType.TEST_ID, "12345678"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(CredentialKit.validate(DefaultCredentialType.MAINLAND_RESIDENT_ID, "330105197810270025").isValid()).isTrue();
    }

    @Test
    void getTypeReflectsRegistrationType() {
        // 把内置身份证处理器注册到自定义类型下，getType必须返回注册类型而非硬编码的内置类型
        CredentialRegistry registry = CredentialRegistry.createEmpty();
        registry.register(TestCredentialType.TEST_ID, new com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor());
        Optional<? extends com.xiahaimoyu.credentialkit.info.CredentialInfo> info =
                registry.parse(TestCredentialType.TEST_ID, "330105197810270025");
        assertThat(info).isPresent();
        assertThat(info.get().getType()).isEqualTo(TestCredentialType.TEST_ID);
    }

    @Test
    void standaloneProcessorParseHasNullType() {
        // 直接使用处理器（未经注册中心）时类型为null，由调用方按需设置
        com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor processor =
                new com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor();
        Optional<com.xiahaimoyu.credentialkit.info.MainlandResidentIdInfo> info =
                processor.parse("330105197810270025");
        assertThat(info).isPresent();
        assertThat(info.get().getType()).isNull();
    }

    @Test
    void customTypeHasHigherDetectPriorityThanBuiltIns() {
        CredentialRegistry kit = CredentialRegistry.create();
        kit.register(TestCredentialType.TEST_ID, new TestCredentialProcessor());
        // "8位数字"同时命中测试类型（优先级0）和台湾通行证（优先级120），测试类型应排最前
        List<CredentialType> types = kit.detect("12345678");
        assertThat(types).first().isEqualTo(TestCredentialType.TEST_ID);
        assertThat(types).contains(DefaultCredentialType.TAIWAN_TRAVEL_PERMIT);
    }
}
