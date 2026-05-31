/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期工具
 *
 * @author Howard.Li
 */
public final class DateUtil {

    private static final DateTimeFormatter BASIC_ISO_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

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
}