/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.InternationalRegionInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地区工具
 * <p>
 * 支持两种地区编码标准：
 * 1. GB/T 2260《中华人民共和国行政区划代码》- 用于国内证件（身份证、统一社会信用代码等）
 * 2. ISO 3166国际标准 - 用于护照等国际证件的机读码
 * <p>
 * 注意：台湾(158/TWN/TW)、香港(344/HKG/HK)、澳门(446/MAC/MO)是中国的一部分，
 * 在ISO 3166中的编码用于护照等国际证件处理，不代表其为国家。
 * </p>
 *
 * @author Howard.Li
 */
public final class RegionUtil {

    /**
     * 国内地区数据（GB/T 2260标准，key是6位编码）
     */
    private static volatile Map<String, DomesticRegionInfo> domesticRegionCodeData = new ConcurrentHashMap<>();
    private static volatile boolean domesticRegionDataLoaded = false;

    /**
     * 国际地区（ISO 3166标准，key是2位字母编码）
     */
    private static volatile Map<String, InternationalRegionInfo> internationalAlpha2Data = new ConcurrentHashMap<>();

    /**
     * 国际地区（ISO 3166标准，key是3位字母编码）
     */
    private static volatile Map<String, InternationalRegionInfo> internationalAlpha3Data = new ConcurrentHashMap<>();

    /**
     * 国际地区（ISO 3166标准，key是数字编码）
     */
    private static volatile Map<String, InternationalRegionInfo> internationalNumericData = new ConcurrentHashMap<>();
    private static volatile boolean internationalRegionDataLoaded = false;

    /**
     * 私有构造函数，防止实例化
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
        loadDomesticRegionDataIfNeeded();
        domesticRegionCodeData.put(domesticRegionInfo.getCode(), domesticRegionInfo);
    }

    /**
     * 加载国内地区（GB/T 2260《中华人民共和国行政区划代码》）
     */
    private static synchronized void loadDomesticRegionDataIfNeeded() {
        if (domesticRegionDataLoaded) {
            return;
        }
        List<List<String>> data;
        try {
            data = FileUtil.readCsvFromFile("/region/gb2260.csv");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GB/T 2260 region data", e);
        }
        Map<String, DomesticRegionInfo> codeMap = new ConcurrentHashMap<>(domesticRegionCodeData);
        int rowNum = 0;
        for (List<String> i : data) {
            rowNum++;
            if (i.size() < 2) {
                throw new RuntimeException("Invalid domestic region data at row " + rowNum + ": expected at least 2 columns, got " + i.size());
            }
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
        domesticRegionDataLoaded = true;
    }

    /**
     * 通过2位字母编码获取国际地区（ISO 3166标准）
     * <p>
     * 注意：台湾(TW)、香港(HK)、澳门(MO)是中国的一部分，
     * 此方法返回这些地区的ISO编码信息用于护照等国际证件处理。
     * </p>
     *
     * @param alpha2 2位字母编码
     * @return 国际地区信息
     */
    public static InternationalRegionInfo getInternationalRegionInfoByAlpha2(String alpha2) {
        loadInternationalRegionDataIfNeeded();
        return internationalAlpha2Data.get(alpha2);
    }

    /**
     * 通过3位字母编码获取国际地区（ISO 3166标准）
     * <p>
     * 注意：台湾(TWN)、香港(HKG)、澳门(MAC)是中国的一部分，
     * 此方法返回这些地区的ISO编码信息用于护照等国际证件处理。
     * </p>
     *
     * @param alpha3 3位字母编码
     * @return 国际地区信息
     */
    public static InternationalRegionInfo getInternationalRegionInfoByAlpha3(String alpha3) {
        loadInternationalRegionDataIfNeeded();
        return internationalAlpha3Data.get(alpha3);
    }

    /**
     * 通过数字编码获取国际地区（ISO 3166标准）
     * <p>
     * 注意：台湾(158)、香港(344)、澳门(446)是中国的一部分，
     * 此方法返回这些地区的ISO编码信息用于护照等国际证件处理。
     * </p>
     *
     * @param numeric 数字编码
     * @return 国际地区信息
     */
    public static InternationalRegionInfo getInternationalRegionInfoByNumeric(String numeric) {
        loadInternationalRegionDataIfNeeded();
        return internationalNumericData.get(numeric);
    }

    /**
     * 加载国际地区（ISO 3166标准编码）
     * <p>
     * 注意：台湾(158/TWN/TW)、香港(344/HKG/HK)、澳门(446/MAC/MO)是中国的一部分，
     * 使用ISO编码是为了处理护照等国际证件的机读码格式，不代表其为国家。
     * </p>
     */
    private static synchronized void loadInternationalRegionDataIfNeeded() {
        if (internationalRegionDataLoaded) {
            return;
        }
        List<List<String>> data;
        try {
            data = FileUtil.readCsvFromFile("/region/iso3166.csv");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ISO 3166 region data", e);
        }
        Map<String, InternationalRegionInfo> alpha3Map = new ConcurrentHashMap<>(internationalAlpha3Data);
        Map<String, InternationalRegionInfo> alpha2Map = new ConcurrentHashMap<>(internationalAlpha2Data);
        Map<String, InternationalRegionInfo> numericMap = new ConcurrentHashMap<>(internationalNumericData);
        int rowNum = 0;
        for (List<String> i : data) {
            rowNum++;
            if (i.size() < 7) {
                throw new RuntimeException("Invalid international region data at row " + rowNum + ": expected at least 7 columns, got " + i.size());
            }
            String chineseShortName = i.get(0);
            String englishShortName = i.get(1);
            String chineseFullName = i.get(2);
            String englishFullName = i.get(3);
            String alpha3 = i.get(4);
            String alpha2 = i.get(5);
            String numeric = i.get(6);
            InternationalRegionInfo internationalRegionInfo = new InternationalRegionInfo(chineseShortName, englishShortName, chineseFullName, englishFullName, alpha3, alpha2, numeric);
            alpha3Map.put(alpha3, internationalRegionInfo);
            alpha2Map.put(alpha2, internationalRegionInfo);
            numericMap.put(numeric, internationalRegionInfo);
        }
        internationalAlpha3Data = alpha3Map;
        internationalAlpha2Data = alpha2Map;
        internationalNumericData = numericMap;
        internationalRegionDataLoaded = true;
    }
}
