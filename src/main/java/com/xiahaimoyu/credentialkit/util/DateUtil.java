/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期工具
 *
 * @author Howard.Li
 */
public final class DateUtil {

    private static final DateTimeFormatter BASIC_ISO_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter YYMMDD_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     * 私有构造函数，防止实例化
     */
    private DateUtil() {
    }

    /**
     * 校验日期是否在今天之前
     *
     * @param dateStr 日期字符串（YYYYMMDD格式）
     * @return 是否在今天之前
     */
    public static boolean validDateBeforeNow(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, BASIC_ISO_FORMAT);
            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 校验日期是否合法
     *
     * @param dateStr 日期字符串（YYYYMMDD格式）
     * @return 是否合法
     */
    public static boolean validDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, BASIC_ISO_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 校验YYMMDD格式日期是否合法
     *
     * @param dateStr 日期字符串（YYMMDD格式）
     * @return 是否合法
     */
    public static boolean validDateYYMMDD(String dateStr) {
        try {
            LocalDate parsed = parseYYMMDD(dateStr);
            return parsed != null;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 解析YYMMDD格式日期
     *
     * @param dateStr 日期字符串（YYMMDD格式）
     * @return LocalDate，如果解析失败则返回null
     */
    public static LocalDate parseYYMMDD(String dateStr) {
        if (dateStr == null || dateStr.length() != 6) {
            return null;
        }
        try {
            int year = Integer.parseInt(dateStr.substring(0, 2));
            int month = Integer.parseInt(dateStr.substring(2, 4));
            int day = Integer.parseInt(dateStr.substring(4, 6));
            // 判断世纪：如果年份 >= 50，则为1900年代；否则为2000年代
            int fullYear = year >= 50 ? 1900 + year : 2000 + year;
            return LocalDate.of(fullYear, month, day);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 从YYYYMMDD格式日期字符串计算年龄
     *
     * @param birthDate 出生日期字符串（YYYYMMDD格式）
     * @return 年龄，如果日期无效则返回-1
     */
    public static int calculateAge(String birthDate) {
        try {
            LocalDate birth = LocalDate.parse(birthDate, BASIC_ISO_FORMAT);
            return calculateAge(birth);
        } catch (DateTimeParseException e) {
            return -1;
        }
    }

    /**
     * 从LocalDate计算年龄
     *
     * @param birthDate 出生日期
     * @return 年龄
     */
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return -1;
        }
        LocalDate now = LocalDate.now();
        if (birthDate.isAfter(now)) {
            return -1;
        }
        return Period.between(birthDate, now).getYears();
    }

    /**
     * 判断是否成年（18岁以上）
     *
     * @param birthDate 出生日期字符串（YYYYMMDD格式）
     * @return 是否成年
     */
    public static boolean isAdult(String birthDate) {
        int age = calculateAge(birthDate);
        return age >= 18;
    }

    /**
     * 判断是否成年（18岁以上）
     *
     * @param birthDate 出生日期
     * @return 是否成年
     */
    public static boolean isAdult(LocalDate birthDate) {
        int age = calculateAge(birthDate);
        return age >= 18;
    }

    /**
     * 将YYYYMMDD格式转换为YYYY-MM-DD格式
     *
     * @param dateStr 日期字符串（YYYYMMDD格式）
     * @return YYYY-MM-DD格式日期字符串
     */
    public static String formatToIsoDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            return null;
        }
        return dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8);
    }

    /**
     * 将YYMMDD格式转换为YYYY-MM-DD格式
     *
     * @param dateStr 日期字符串（YYMMDD格式）
     * @return YYYY-MM-DD格式日期字符串
     */
    public static String formatYYMMDDToIsoDate(String dateStr) {
        LocalDate date = parseYYMMDD(dateStr);
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
