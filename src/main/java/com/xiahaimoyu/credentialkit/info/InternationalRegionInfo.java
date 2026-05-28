/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */

package com.xiahaimoyu.credentialkit.info;

import java.util.Objects;

/**
 * 国际地区信息
 *
 * @author Howard.Li
 */
public final class InternationalRegionInfo extends CredentialInfo {

    /**
     * 中文简称
     */
    private final String chineseShortName;

    /**
     * 英文简称
     */
    private final String englishShortName;

    /**
     * 中文全称
     */
    private final String chineseFullName;

    /**
     * 英文全称
     */
    private final String englishFullName;

    /**
     * 三位字母编码
     */
    private final String alpha3;

    /**
     * 两位字母编码
     */
    private final String alpha2;

    /**
     * 数字编码
     */
    private final String numeric;

    /**
     * 构造函数
     *
     * @param chineseShortName 中文简称
     * @param englishShortName 英文简称
     * @param chineseFullName  中文全称
     * @param englishFullName  英文全称
     * @param alpha3      三位字母编码
     * @param alpha2      两位字母编码
     * @param numeric     数字编码
     */
    public InternationalRegionInfo(String chineseShortName, String englishShortName, String chineseFullName, String englishFullName, String alpha3, String alpha2, String numeric) {
        this.chineseShortName = chineseShortName;
        this.englishShortName = englishShortName;
        this.chineseFullName = chineseFullName;
        this.englishFullName = englishFullName;
        this.alpha3 = alpha3;
        this.alpha2 = alpha2;
        this.numeric = numeric;
    }

    /**
     * 获取中文简称
     *
     * @return 中文简称
     */
    public String getChineseShortName() {
        return chineseShortName;
    }

    /**
     * 获取英文简称
     *
     * @return 英文简称
     */
    public String getEnglishShortName() {
        return englishShortName;
    }

    /**
     * 获取中文全称
     *
     * @return 中文全称
     */
    public String getChineseFullName() {
        return chineseFullName;
    }

    /**
     * 获取英文全称
     *
     * @return 英文全称
     */
    public String getEnglishFullName() {
        return englishFullName;
    }

    /**
     * 获取三位字母编码
     *
     * @return 三位字母编码
     */
    public String getAlpha3() {
        return alpha3;
    }

    /**
     * 获取两位字母编码
     *
     * @return 两位字母编码
     */
    public String getAlpha2() {
        return alpha2;
    }

    /**
     * 获取数字编码
     *
     * @return 数字编码
     */
    public String getNumeric() {
        return numeric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternationalRegionInfo that = (InternationalRegionInfo) o;
        return Objects.equals(alpha3, that.alpha3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha3);
    }

    @Override
    public String toString() {
        return "InternationalRegionInfo{" +
                "alpha3='" + alpha3 + '\'' +
                ", alpha2='" + alpha2 + '\'' +
                ", numeric='" + numeric + '\'' +
                ", chineseShortName='" + chineseShortName + '\'' +
                ", englishShortName='" + englishShortName + '\'' +
                '}';
    }
}
