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
public class DateUtil {

    /**
     * 校验日期是否在今天之前
     *
     * @param dateStr 日期字符串
     * @return 是否在今天之前
     */
    public static boolean validDateBeforeNow(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 校验日期是否合法
     *
     * @param dateStr 日期字符串
     * @return 是否合法
     */
    public static boolean validDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
