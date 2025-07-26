/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.InternationalRegionInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地区工具
 *
 * @author Howard.Li
 */
public class RegionUtil {

    /**
     * 国内地区数据（key是6位编码）
     */
    private static Map<String, DomesticRegionInfo> domesticRegionCodeData = new ConcurrentHashMap<>();

    /**
     * 国际地区（key是2位字母编码）
     */
    private static Map<String, InternationalRegionInfo> internationalAlpha2Data = new ConcurrentHashMap<>();

    /**
     * 国际地区（key是3位字母编码）
     */
    private static Map<String, InternationalRegionInfo> internationalAlpha3Data = new ConcurrentHashMap<>();

    /**
     * 国际地区（key是数字编码）
     */
    private static Map<String, InternationalRegionInfo> internationalNumericData = new ConcurrentHashMap<>();

    /**
     * 构造器
     */
    private RegionUtil() {
    }

    /**
     * 通过编码获取国内地区
     *
     * @param code 编码
     * @return 国内地区
     */
    public static DomesticRegionInfo getDomesticRegionInfoByCode(String code) {
        loadDomesticRegionDataIfNeeded();
        return domesticRegionCodeData.get(code);
    }

    /**
     * 增加自定义国内地区
     *
     * @param domesticRegionInfo 国内地区
     */
    public static void addDomesticRegionData(DomesticRegionInfo domesticRegionInfo) {
        domesticRegionCodeData.put(domesticRegionInfo.getCode(), domesticRegionInfo);
    }

    /**
     * 加载国内地区
     */
    private static synchronized void loadDomesticRegionDataIfNeeded() {
        if (!domesticRegionCodeData.isEmpty()) {
            return;
        }
        List<List<String>> data = new ArrayList<>();
        try {
            data = FileUtil.readCsvFromFile("/region/domestic.csv");
        } catch (IOException e) {
            return;
        }
        Map<String, DomesticRegionInfo> codeMap = new ConcurrentHashMap<>();
        for (List<String> i : data) {
            String code = i.get(0);
            String value = i.get(1);
            String province = null;
            String city = null;
            String county = null;
            if (code.endsWith("0000")) {
                province = value;
            } else if (code.endsWith("00")) {
                DomesticRegionInfo provinceRegion = codeMap.get(code.substring(0, 2) + "0000");
                if (provinceRegion != null) {
                    province = provinceRegion.getProvince();
                }
                city = value;
            } else {
                DomesticRegionInfo provinceRegion = codeMap.get(code.substring(0, 2) + "0000");
                if (provinceRegion != null) {
                    province = provinceRegion.getProvince();
                }
                DomesticRegionInfo cityRegion = codeMap.get(code.substring(0, 4) + "00");
                if (cityRegion != null) {
                    city = cityRegion.getCity();
                }
                county = value;
            }
            DomesticRegionInfo domesticRegionInfo = new DomesticRegionInfo(code, province, city, county);
            codeMap.put(code, domesticRegionInfo);
        }
        domesticRegionCodeData = codeMap;
    }

    /**
     * 通过2位字母编码获取国际地区
     *
     * @param alpha2 2位字母编码
     * @return 国际地区
     */
    public static InternationalRegionInfo getInternationalRegionInfoByAlpha2(String alpha2) {
        loadInternationalRegionDataIfNeeded();
        return internationalAlpha2Data.get(alpha2);
    }

    /**
     * 通过3位字母编码获取国际地区
     *
     * @param alpha3 3位字母编码
     * @return 国际地区
     */
    public static InternationalRegionInfo getInternationalRegionInfoByAlpha3(String alpha3) {
        loadInternationalRegionDataIfNeeded();
        return internationalAlpha3Data.get(alpha3);
    }

    /**
     * 通过数字编码获取国际地区
     *
     * @param numeric 数字编码
     * @return 国际地区
     */
    public static InternationalRegionInfo getInternationalRegionInfoByNumeric(String numeric) {
        loadInternationalRegionDataIfNeeded();
        return internationalNumericData.get(numeric);
    }

    /**
     * 加载国际地区
     */
    private static synchronized void loadInternationalRegionDataIfNeeded() {
        if (!internationalAlpha2Data.isEmpty() && !internationalAlpha3Data.isEmpty() && !internationalNumericData.isEmpty()) {
            return;
        }
        List<List<String>> data = new ArrayList<>();
        try {
            data = FileUtil.readCsvFromFile("/region/international.csv");
        } catch (IOException e) {
            return;
        }
        Map<String, InternationalRegionInfo> alpha3Map = new ConcurrentHashMap<>();
        Map<String, InternationalRegionInfo> alpha2Map = new ConcurrentHashMap<>();
        Map<String, InternationalRegionInfo> numericMap = new ConcurrentHashMap<>();
        for (List<String> i : data) {
            String zhShortName = i.get(0);
            String enShortName = i.get(1);
            String zhFullName = i.get(2);
            String enFullName = i.get(3);
            String alpha3 = i.get(4);
            String alpha2 = i.get(5);
            String numeric = i.get(6);
            InternationalRegionInfo internationalRegionInfo = new InternationalRegionInfo(zhShortName, enShortName, zhFullName, enFullName, alpha3, alpha2, numeric);
            alpha3Map.put(alpha3, internationalRegionInfo);
            alpha2Map.put(alpha2, internationalRegionInfo);
            numericMap.put(numeric, internationalRegionInfo);
        }
        internationalAlpha3Data = alpha3Map;
        internationalAlpha2Data = alpha2Map;
        internationalNumericData = numericMap;
    }
}
