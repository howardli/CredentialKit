/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialParser;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.CredentialValidator;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 环境相关缺陷的回归测试
 */
@Isolated
class EnvironmentRobustnessTest {

    /**
     * 合法的机读护照（大写）
     */
    private static final String UPPERCASE_MRZ =
            "POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16";

    // ==================== 土耳其语locale回归测试 ====================
    // normalize使用Locale.ROOT前，默认locale为土耳其语时'i'会转为'İ'（非[A-Z]字符），
    // 导致合法输入被误判为格式错误

    @Test
    void lowercaseInputValidatesInTurkishLocale() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("tr", "TR"));
            assertThat(CredentialKit.validate(DefaultCredentialType.MACHINE_READABLE_PASSPORT, UPPERCASE_MRZ.toLowerCase(Locale.ROOT)).isValid())
                    .isTrue();
        } finally {
            Locale.setDefault(original);
        }
    }

    @Test
    void lowercaseInputParsesInTurkishLocale() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("tr", "TR"));
            assertThat(CredentialKit.parse(DefaultCredentialType.MACHINE_READABLE_PASSPORT, UPPERCASE_MRZ.toLowerCase(Locale.ROOT),
                    com.xiahaimoyu.credentialkit.info.MachineReadablePassportInfo.class)).isPresent();
        } finally {
            Locale.setDefault(original);
        }
    }

    // ==================== 中文名为null的排序回归测试 ====================
    // 排序比较器空安全前，两个同优先级且中文名为null的类型会让detect抛出NullPointerException

    /**
     * 中文名和英文名均返回null的测试类型
     */
    enum NullNameCredentialType implements CredentialType {

        NULL_NAME_A,
        NULL_NAME_B;

        @Override
        public String getChineseName() {
            return null;
        }

        @Override
        public String getEnglishName() {
            return null;
        }
    }

    /**
     * 匹配"NULLTEST"的测试处理器
     */
    static class NullNameProcessor extends CredentialProcessor<CredentialInfo> {

        NullNameProcessor() {
            super(
                    Collections.singletonList(
                            (CredentialValidator) credential ->
                                    credential.equals("NULLTEST")
                                            ? ValidationResult.success()
                                            : ValidationResult.failure(ErrorCode.BASIC_FORMAT_ERROR)
                    ),
                    Collections.<CredentialParser<CredentialInfo>>emptyList()
            );
        }

        @Override
        protected CredentialInfo createInfo() {
            return new CredentialInfo() {
            };
        }
    }

    @Test
    void detectDoesNotThrowWithNullChineseNames() {
        CredentialRegistry registry = CredentialRegistry.createEmpty();
        registry.register(NullNameCredentialType.NULL_NAME_A, new NullNameProcessor());
        registry.register(NullNameCredentialType.NULL_NAME_B, new NullNameProcessor());
        List<CredentialType> types = registry.detect("NULLTEST");
        assertThat(types).containsExactlyInAnyOrder(
                NullNameCredentialType.NULL_NAME_A, NullNameCredentialType.NULL_NAME_B);
    }
}
