/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 校验位工具
 *
 * @author Howard.Li
 */
public class CheckDigitUtil {

    /**
     * 中华人民共和国居民身份证号码权重
     */
    private static final int[] ID_CARD_WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 中华人民共和国居民身份证号码检查和字符
     */
    private static final char[] ID_CARD_CHECKSUM_CHARS = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 统一社会信用代码权重
     */
    private static final int[] USCI_WEIGHT_FACTORS = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

    /**
     * 统一社会信用代码校验位字符
     */
    private static final char[] USCI_CHECK_CODE_MAP = "0123456789ABCDEFGHJKLMNPQRTUWXY".toCharArray();

    /**
     * 统一社会信用代码校验字符
     */
    private static final Map<Character, Integer> USCI_CHAR_VALUE_MAP = new HashMap<>();

    static {
        for (int i = 0; i < USCI_CHECK_CODE_MAP.length; i++) {
            USCI_CHAR_VALUE_MAP.put(USCI_CHECK_CODE_MAP[i], i);
        }
    }

    /**
     * 组织机构代码权重
     */
    private static final int[] ORGANIZATION_CODE_WEIGHTS = {3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 可机读护照编码权重
     */
    private static final int[] MACHINE_READABLE_PASSPORT_CODE_WEIGHTS = {7, 3, 1};

    /**
     * 获取中华人民共和国居民身份证号码校验位
     *
     * @param credential 身份证号码
     * @return 身份证号码校验位
     */
    public static char getIdCardCheckDigit(String credential) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (credential.charAt(i) - '0') * ID_CARD_WEIGHTS[i];
        }
        return ID_CARD_CHECKSUM_CHARS[sum % 11];
    }

    /**
     * 获取统一社会信用代码校验位
     *
     * @param credential 统一社会信用代码
     * @return 统一社会信用代码校验位
     */
    public static char getUnifiedSocialCreditCodeCheckDigit(String credential) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = credential.charAt(i);
            Integer charValue = USCI_CHAR_VALUE_MAP.get(c);
            if (charValue == null) {
                throw new IllegalArgumentException("包含无效字符: " + c);
            }
            sum += charValue * USCI_WEIGHT_FACTORS[i];
        }
        int mod = sum % 31;
        int checkValue = 31 - mod;
        checkValue = checkValue % 31;
        return USCI_CHECK_CODE_MAP[checkValue];
    }

    /**
     * 获取组织机构代码校验位
     *
     * @param credential 组织机构代码
     * @return 组织机构代码校验位
     */
    public static char getOrganizationCodeCheckDigit(String credential) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            char c = credential.charAt(i);
            int num = -1;
            if (c >= '0' && c <= '9') {
                num = c - '0';
            } else if (c >= 'A' && c <= 'Z') {
                num = c - 'A' + 10;
            }
            sum += num * ORGANIZATION_CODE_WEIGHTS[i];
        }
        int remainder = sum % 11;
        int checkValue = 11 - remainder;
        char expectedChecksum;
        if (checkValue == 10) {
            expectedChecksum = 'X';
        } else {
            expectedChecksum = (char) ('0' + checkValue);
        }
        return expectedChecksum;
    }

    /**
     * 获取可机读护照编码校验位
     *
     * @param credential 可机读护照编码
     * @return 可机读护照编码校验位
     */
    public static char getMachineReadablePassportCodeCheckDigit(String credential) {
        int sum = 0;
        for (int i = 0; i < credential.length(); i++) {
            char c = credential.charAt(i);
            int num = 0;
            if (c >= '0' && c <= '9') {
                num = c - '0';
            } else if (c >= 'A' && c <= 'Z') {
                num = c - 'A' + 10;
            }
            sum += num * MACHINE_READABLE_PASSPORT_CODE_WEIGHTS[i % 3];
        }
        return (char) ('0' + sum % 10);
    }
}
