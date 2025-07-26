/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */

package com.xiahaimoyu.credentialkit.info;

/**
 * 国际地区信息
 *
 * @author Howard.Li
 */
public class InternationalRegionInfo extends CredentialInfo {

    /**
     * 中文简称
     */
    private final String zhShortName;

    /**
     * 英文简称
     */
    private final String enShortName;

    /**
     * 中文全称
     */
    private final String zhFullName;

    /**
     * 英文全称
     */
    private final String enFullName;

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
     * @param zhShortName 中文简称
     * @param enShortName 英文简称
     * @param zhFullName  中文全称
     * @param enFullName  英文全称
     * @param alpha3      三位字母编码
     * @param alpha2      两位字母编码
     * @param numeric     数字编码
     */
    public InternationalRegionInfo(String zhShortName, String enShortName, String zhFullName, String enFullName, String alpha3, String alpha2, String numeric) {
        this.zhShortName = zhShortName;
        this.enShortName = enShortName;
        this.zhFullName = zhFullName;
        this.enFullName = enFullName;
        this.alpha3 = alpha3;
        this.alpha2 = alpha2;
        this.numeric = numeric;
    }

    /**
     * 获取中文简称
     *
     * @return 中文简称
     */
    public String getZhShortName() {
        return zhShortName;
    }

    /**
     * 获取英文简称
     *
     * @return 英文简称
     */
    public String getEnShortName() {
        return enShortName;
    }

    /**
     * 获取中文全称
     *
     * @return 中文全称
     */
    public String getZhFullName() {
        return zhFullName;
    }

    /**
     * 获取英文全称
     *
     * @return 英文全称
     */
    public String getEnFullName() {
        return enFullName;
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

}
