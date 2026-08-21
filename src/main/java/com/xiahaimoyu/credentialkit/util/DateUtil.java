/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * 日期工具
 * <p>
 * 涉及"当前日期"的判断使用可替换的{@link Clock}，默认为系统时钟。
 * 测试时可通过{@link #setClock(Clock)}固定时间，验证跨临界日期的行为。
 * </p>
 *
 * @author Howard.Li
 */
public final class DateUtil {

    private static final DateTimeFormatter BASIC_ISO_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    /**
     * 时钟（volatile保证多线程可见）
     */
    private static volatile Clock clock = Clock.systemDefaultZone();

    /**
     * 私有构造函数，防止实例化
     */
    private DateUtil() {
    }

    /**
     * 设置时钟
     * <p>
     * 主要用于测试场景固定"当前时间"。
     * </p>
     *
     * @param clock 时钟
     * @throws NullPointerException 如果clock为空
     */
    public static void setClock(Clock clock) {
        DateUtil.clock = Objects.requireNonNull(clock, "时钟是空");
    }

    /**
     * 恢复系统时钟
     */
    public static void resetClock() {
        clock = Clock.systemDefaultZone();
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    private static LocalDate today() {
        return LocalDate.now(clock);
    }

    /**
     * 尝试解析日期
     *
     * @param dateStr 日期字符串（YYYYMMDD格式）
     * @return 解析结果，解析失败则返回null
     */
    private static LocalDate tryParse(String dateStr) {
        try {
            return LocalDate.parse(dateStr, BASIC_ISO_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 校验日期是否在今天之前
     *
     * @param dateStr 日期字符串（YYYYMMDD格式）
     * @return 是否在今天之前
     */
    public static boolean validDateBeforeNow(String dateStr) {
        LocalDate date = tryParse(dateStr);
        return date != null && !date.isAfter(today());
    }

    /**
     * 校验日期是否合法
     *
     * @param dateStr 日期字符串（YYYYMMDD格式）
     * @return 是否合法
     */
    public static boolean validDate(String dateStr) {
        return tryParse(dateStr) != null;
    }

    /**
     * 将YYMMDD格式的日期转换为YYYYMMDD格式
     * <p>
     * 优先使用20xx前缀（更可能是当前在世的人），若20xx不是有效的过去日期则使用19xx。
     * </p>
     *
     * @param yyBirthDate YYMMDD格式的日期（6位）
     * @return YYYYMMDD格式的日期（8位）
     */
    public static String toFullYearDate(String yyBirthDate) {
        if (validDateBeforeNow("20" + yyBirthDate)) {
            return "20" + yyBirthDate;
        }
        return "19" + yyBirthDate;
    }

    /**
     * 将YYMMDD格式的有效期转换为YYYYMMDD格式
     * <p>
     * 优先使用20xx前缀；若20xx日期超出当前日期20年以上（如99表示1999而非2099），
     * 则使用19xx前缀。
     * </p>
     *
     * @param yyExpirationDate YYMMDD格式的有效期（6位）
     * @return YYYYMMDD格式的有效期（8位）
     */
    public static String toFullYearExpirationDate(String yyExpirationDate) {
        LocalDate date = tryParse("20" + yyExpirationDate);
        if (date != null && !date.isAfter(today().plusYears(20))) {
            return "20" + yyExpirationDate;
        }
        return "19" + yyExpirationDate;
    }
}
