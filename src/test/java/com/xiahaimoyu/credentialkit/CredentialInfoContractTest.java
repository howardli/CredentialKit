/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.InternationalRegionInfo;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportInfo;
import com.xiahaimoyu.credentialkit.info.MainlandResidentIdInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor;
import com.xiahaimoyu.credentialkit.processor.MachineReadablePassportProcessor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 证件信息对象的equals/hashCode/toString契约测试
 * <p>
 * 解析结果常被放入集合或用于比较，equals/hashCode的正确性是公共API契约的一部分。
 * </p>
 */
class CredentialInfoContractTest {

    private static final String ID = "330105197810270025";

    private static final String MRZ =
            "POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16";

    @Test
    void mainlandResidentIdInfoContract() {
        MainlandResidentIdInfo a = parse(new MainlandResidentIdProcessor(), ID);
        MainlandResidentIdInfo b = parse(new MainlandResidentIdProcessor(), ID);
        MainlandResidentIdInfo c = parse(new MainlandResidentIdProcessor(), "110101190001010014");

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("birthDate");
        assertThat(a.getRegion().toString()).contains("code='330105'");
    }

    @Test
    void machineReadablePassportInfoContract() {
        MachineReadablePassportInfo a = parse(new MachineReadablePassportProcessor(), MRZ);
        MachineReadablePassportInfo b = parse(new MachineReadablePassportProcessor(), MRZ);
        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("surname");
        assertThat(a.getIssuingRegion().toString()).contains("alpha3='CHN'");
    }

    @Test
    void domesticRegionInfoContract() {
        DomesticRegionInfo a = new DomesticRegionInfo("330105", "浙江省", "杭州市", "拱墅区");
        DomesticRegionInfo b = new DomesticRegionInfo("330105", "不同名", "不同市", "不同区");
        DomesticRegionInfo c = new DomesticRegionInfo("330106", "浙江省", "杭州市", "西湖区");
        // equals只看code：同一编码的不同历史名称视为同一地区
        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("province='浙江省'");
    }

    @Test
    void internationalRegionInfoContract() {
        InternationalRegionInfo a = new InternationalRegionInfo("中国", "CHINA", "中华人民共和国", "the People's Republic of China", "CHN", "CN", "156");
        InternationalRegionInfo b = new InternationalRegionInfo("中国", "CHINA", "中华人民共和国", "the People's Republic of China", "CHN", "CN", "156");
        InternationalRegionInfo c = new InternationalRegionInfo("美国", "USA", "美利坚合众国", "the United States of America", "USA", "US", "840");
        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("alpha2='CN'");
    }

    @Test
    void setTypeRejectsNull() {
        MainlandResidentIdInfo info = parse(new MainlandResidentIdProcessor(), ID);
        assertThatThrownBy(() -> info.setType(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("证件类型是空");
    }

    @Test
    void typedParseSetsTypeOnInfo() {
        Optional<MainlandResidentIdInfo> info = CredentialKit.parse(
                DefaultCredentialType.MAINLAND_RESIDENT_ID, ID, MainlandResidentIdInfo.class);
        assertThat(info).isPresent();
        assertThat(info.get().getType()).isEqualTo(DefaultCredentialType.MAINLAND_RESIDENT_ID);
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
