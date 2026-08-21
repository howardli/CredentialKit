/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@Isolated
class DateUtilTest {

    @AfterEach
    void resetClock() {
        DateUtil.resetClock();
    }

    @Test
    void validDate() {
        assertThat(DateUtil.validDate("19781027")).isTrue();
        assertThat(DateUtil.validDate("19781327")).isFalse();
        assertThat(DateUtil.validDate("19780230")).isFalse();
        assertThat(DateUtil.validDate("abc")).isFalse();
    }

    @Test
    void validDateBeforeNowUsesInjectedClock() {
        // 固定时间为2020-01-01：2019年在过去，2021年在未来
        DateUtil.setClock(Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneId.of("UTC")));
        assertThat(DateUtil.validDateBeforeNow("20191231")).isTrue();
        assertThat(DateUtil.validDateBeforeNow("20200101")).isTrue();
        assertThat(DateUtil.validDateBeforeNow("20200102")).isFalse();
    }

    @Test
    void validDateBeforeNowAfterClockReset() {
        DateUtil.setClock(Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneId.of("UTC")));
        DateUtil.resetClock();
        assertThat(DateUtil.validDateBeforeNow("19781027")).isTrue();
        assertThat(DateUtil.validDateBeforeNow("29991231")).isFalse();
    }

    @Test
    void toFullYearDatePrefers21stCenturyWhenValid() {
        // 固定时间为2030-01-01：25 -> 2025是有效的过去日期
        DateUtil.setClock(Clock.fixed(Instant.parse("2030-01-01T00:00:00Z"), ZoneId.of("UTC")));
        assertThat(DateUtil.toFullYearDate("250101")).isEqualTo("20250101");
        assertThat(DateUtil.toFullYearDate("991231")).isEqualTo("19991231");
    }

    @Test
    void toFullYearDateFallsBackTo20thCentury() {
        // 固定时间为2030-01-01：99 -> 2099在未来，回退到1999
        DateUtil.setClock(Clock.fixed(Instant.parse("2030-01-01T00:00:00Z"), ZoneId.of("UTC")));
        assertThat(DateUtil.toFullYearDate("991231")).isEqualTo("19991231");
    }

    @Test
    void toFullYearExpirationDatePrefers21stCenturyWithin20Years() {
        // 固定时间为2026-08-21：26 -> 2026未超出2046-08-21
        DateUtil.setClock(Clock.fixed(Instant.parse("2026-08-21T00:00:00Z"), ZoneId.of("UTC")));
        assertThat(DateUtil.toFullYearExpirationDate("210126")).isEqualTo("20210126");
        assertThat(DateUtil.toFullYearExpirationDate("300101")).isEqualTo("20300101");
    }

    @Test
    void toFullYearExpirationDateFallsBackForFarFuture() {
        // 固定时间为2026-08-21：99 -> 2099超出2046-08-21，回退到1999
        DateUtil.setClock(Clock.fixed(Instant.parse("2026-08-21T00:00:00Z"), ZoneId.of("UTC")));
        assertThat(DateUtil.toFullYearExpirationDate("990101")).isEqualTo("19990101");
    }
}
