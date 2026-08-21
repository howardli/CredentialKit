/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.InternationalRegionInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegionUtilTest {

    @Test
    void getDomesticRegionInfoByCode() {
        DomesticRegionInfo region = RegionUtil.getDomesticRegionInfoByCode("330105");
        assertThat(region).isNotNull();
        assertThat(region.getProvince()).isEqualTo("浙江省");
        assertThat(region.getCounty()).isEqualTo("拱墅区");
        assertThat(RegionUtil.getDomesticRegionInfoByCode("999999")).isNull();
    }

    @Test
    void getInternationalRegionInfo() {
        InternationalRegionInfo byAlpha3 = RegionUtil.getInternationalRegionInfoByAlpha3("CHN");
        assertThat(byAlpha3).isNotNull();
        assertThat(byAlpha3.getAlpha2()).isEqualTo("CN");
        assertThat(RegionUtil.getInternationalRegionInfoByAlpha2("CN")).isEqualTo(byAlpha3);
        assertThat(RegionUtil.getInternationalRegionInfoByNumeric("156")).isEqualTo(byAlpha3);
        assertThat(RegionUtil.getInternationalRegionInfoByAlpha3("ZZZ")).isNull();
    }

    @Test
    void addAndRemoveDomesticRegionData() {
        DomesticRegionInfo region = new DomesticRegionInfo("999999", "测试省", "测试市", "测试区");
        try {
            RegionUtil.addDomesticRegionData(region);
            assertThat(RegionUtil.getDomesticRegionInfoByCode("999999")).isEqualTo(region);

            DomesticRegionInfo overridden = new DomesticRegionInfo("999999", "测试省2", null, null);
            RegionUtil.addDomesticRegionData(overridden);
            assertThat(RegionUtil.getDomesticRegionInfoByCode("999999")).isEqualTo(overridden);
        } finally {
            // 无论断言结果如何都恢复原状，避免污染其他测试
            RegionUtil.removeDomesticRegionData("999999");
        }
        assertThat(RegionUtil.getDomesticRegionInfoByCode("999999")).isNull();
        assertThat(RegionUtil.removeDomesticRegionData("999999")).isNull();
    }

    @Test
    void addAndRemoveInternationalRegionData() {
        InternationalRegionInfo region = new InternationalRegionInfo("测试地区", "TEST", "测试地区", "Test Region", "TST", "TS", "900");
        try {
            RegionUtil.addInternationalRegionData(region);
            assertThat(RegionUtil.getInternationalRegionInfoByAlpha3("TST")).isEqualTo(region);
            assertThat(RegionUtil.getInternationalRegionInfoByAlpha2("TS")).isEqualTo(region);
            assertThat(RegionUtil.getInternationalRegionInfoByNumeric("900")).isEqualTo(region);
        } finally {
            // 无论断言结果如何都恢复原状，避免污染其他测试
            RegionUtil.removeInternationalRegionData("TST");
        }
        assertThat(RegionUtil.getInternationalRegionInfoByAlpha3("TST")).isNull();
        assertThat(RegionUtil.getInternationalRegionInfoByAlpha2("TS")).isNull();
        assertThat(RegionUtil.getInternationalRegionInfoByNumeric("900")).isNull();
        assertThat(RegionUtil.removeInternationalRegionData("TST")).isNull();
    }

    @Test
    void addInternationalRegionDataWithPartialCodes() {
        InternationalRegionInfo region = new InternationalRegionInfo(null, null, null, null, "XXX", null, null);
        try {
            RegionUtil.addInternationalRegionData(region);
            assertThat(RegionUtil.getInternationalRegionInfoByAlpha3("XXX")).isEqualTo(region);
        } finally {
            RegionUtil.removeInternationalRegionData("XXX");
        }
    }
}
