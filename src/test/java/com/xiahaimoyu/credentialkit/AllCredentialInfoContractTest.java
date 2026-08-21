/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.ForeignerPermanentResidenceIdProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMacaoResidencePermitProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMacaoTravelPermitProcessor;
import com.xiahaimoyu.credentialkit.processor.MachineReadablePassportProcessor;
import com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor;
import com.xiahaimoyu.credentialkit.processor.TaiwanResidencePermitProcessor;
import com.xiahaimoyu.credentialkit.processor.TaiwanTravelPermitProcessor;
import com.xiahaimoyu.credentialkit.processor.UnifiedSocialCreditProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 全部证件信息类型的通用契约测试
 * <p>
 * 对每种内置证件类型验证：两次解析结果equals/hashCode一致、
 * toString非空、与null和其他类型不相等、getType返回注册类型。
 * 解析结果常被放入集合，equals/hashCode是公共API契约的一部分。
 * </p>
 */
class AllCredentialInfoContractTest {

    /**
     * 测试样例：每种证件一个合法号码和对应处理器
     */
    private static Object[][] specimens() {
        return new Object[][]{
                {DefaultCredentialType.MAINLAND_RESIDENT_ID, new MainlandResidentIdProcessor(), "330105197810270025"},
                {DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT, new HkMacaoTravelPermitProcessor(), "M1234567801"},
                {DefaultCredentialType.TAIWAN_TRAVEL_PERMIT, new TaiwanTravelPermitProcessor(), "1234567801"},
                {DefaultCredentialType.HK_MACAO_RESIDENCE_PERMIT, new HkMacaoResidencePermitProcessor(), "810000199408230021"},
                {DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT, new TaiwanResidencePermitProcessor(), "830000199201300022"},
                {DefaultCredentialType.FOREIGNER_PERMANENT_RESIDENCE_ID, new ForeignerPermanentResidenceIdProcessor(), "911398199012310021"},
                {DefaultCredentialType.FOREIGNER_PERMANENT_RESIDENCE_ID, new ForeignerPermanentResidenceIdProcessor(), "KAZ110090123105"},
                {DefaultCredentialType.MACHINE_READABLE_PASSPORT, new MachineReadablePassportProcessor(),
                        "POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16"},
                {DefaultCredentialType.UNIFIED_SOCIAL_CREDIT, new UnifiedSocialCreditProcessor(), "91330106MA27Y4U47R"}
        };
    }

    @ParameterizedTest
    @MethodSource("specimens")
    void infoEqualsAndHashCodeAreConsistent(DefaultCredentialType type,
                                            CredentialProcessor<? extends CredentialInfo> processor,
                                            String credential) {
        // 经注册中心解析，getType()应返回注册类型
        CredentialInfo a = parse(processor, credential);
        CredentialInfo b = parse(processor, credential);

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("不是证件信息");
        assertThat(a).isEqualTo(a);
        assertThat(a.toString()).isNotBlank();
        // 经CredentialKit（默认注册中心）解析的同类信息应equals，且getType返回注册类型
        Optional<? extends CredentialInfo> viaKit = CredentialKit.parse(type, credential);
        assertThat(viaKit).isPresent();
        assertThat(viaKit.get()).isEqualTo(a);
        assertThat(viaKit.get().getType()).isEqualTo(type);
    }

    @Test
    void differentInfoTypesAreNotEqual() {
        // 两个"形状相似"但类型不同的信息对象不应相等（各自类不同）
        assertThat(parse(new MainlandResidentIdProcessor(), "110101197810270029"))
                .isNotEqualTo(parse(new TaiwanResidencePermitProcessor(), "830000199201300022"));
    }

    /**
     * 通过处理器解析并断言成功
     */
    private <T extends CredentialInfo> T parse(CredentialProcessor<T> processor, String credential) {
        Optional<T> info = processor.parse(credential);
        assertThat(info).as("解析应成功: " + credential).isPresent();
        return info.get();
    }
}
