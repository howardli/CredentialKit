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
 * <p>
 * 采用双重检查锁定（Double-Checked Locking）实现懒加载，保证线程安全且高效。
 * </p>
 *
 * @author Howard.Li
 */
public final class RegionUtil {

    /**
     * 国内地区数据（GB/T 2260标准，key是6位编码）
     */
    private static volatile Map<String, DomesticRegionInfo> domesticRegionCodeData;

    /**
     * 国际地区（ISO 3166标准，key是2位字母编码）
     */
    private static volatile Map<String, InternationalRegionInfo> internationalAlpha2Data;

    /**
     * 国际地区（ISO 3166标准，key是3位字母编码）
     */
    private static volatile Map<String, InternationalRegionInfo> internationalAlpha3Data;

    /**
     * 国际地区（ISO 3166标准，key是数字编码）
     */
    private static volatile Map<String, InternationalRegionInfo> internationalNumericData;

    /**
     * 加载锁对象
     */
    private static final Object DOMESTIC_LOCK = new Object();
    private static final Object INTERNATIONAL_LOCK = new Object();

    /**
     * 私有构造函数，防止实例化
     */
    private RegionUtil() {
    }

    /**
     * 通过编码获取国内地区
     *
     * @param code 编码
     * @return 国内地区，如果不存在则返回null
     */
    public static DomesticRegionInfo getDomesticRegionInfoByCode(String code) {
        if (domesticRegionCodeData == null) {
            synchronized (DOMESTIC_LOCK) {
                if (domesticRegionCodeData == null) {
                    loadDomesticRegionData();
                }
            }
        }
        return domesticRegionCodeData.get(code);
    }

    /**
     * 添加或覆盖国内地区数据
     *
     * @param domesticRegionInfo 国内地区信息
     */
    public static void addDomesticRegionData(DomesticRegionInfo domesticRegionInfo) {
        getDomesticRegionInfoByCode(domesticRegionInfo.getCode()); // 确保数据已加载
        domesticRegionCodeData.put(domesticRegionInfo.getCode(), domesticRegionInfo);
    }

    /**
     * 添加或覆盖国际地区数据（ISO 3166标准）
     *
     * @param internationalRegionInfo 国际地区信息
     */
    public static void addInternationalRegionData(InternationalRegionInfo internationalRegionInfo) {
        ensureInternationalRegionDataLoaded(); // 确保数据已加载
        internationalAlpha2Data.put(internationalRegionInfo.getAlpha2(), internationalRegionInfo);
        internationalAlpha3Data.put(internationalRegionInfo.getAlpha3(), internationalRegionInfo);
        internationalNumericData.put(internationalRegionInfo.getNumeric(), internationalRegionInfo);
    }

    /**
     * 加载国内地区数据（GB/T 2260《中华人民共和国行政区划代码》）
     */
    private static void loadDomesticRegionData() {
        List<List<String>> data;
        try {
            data = FileUtil.readCsvFromFile("/region/gb2260.csv");
        } catch (IOException e) {
            throw new RuntimeException("加载GB/T 2260地区数据失败", e);
        }
        Map<String, DomesticRegionInfo> codeMap = new ConcurrentHashMap<>();
        int rowNum = 0;
        for (List<String> row : data) {
            rowNum++;
            if (row.size() < 2) {
                throw new RuntimeException("国内地区数据格式错误，第" + rowNum + "行应有至少2列，实际" + row.size() + "列");
            }
            String code = row.get(0);
            String value = row.get(1);
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
     * 通过2位字母编码获取国际地区（ISO 3166标准）
     * <p>
     * 注意：台湾(TW)、香港(HK)、澳门(MO)是中国的一部分，
     * 此方法返回这些地区的ISO编码信息用于护照等国际证件处理。
     * </p>
     *
     * @param alpha2 2位字母编码
     * @return 国际地区信息，如果不存在则返回null
     */
    public static InternationalRegionInfo getInternationalRegionInfoByAlpha2(String alpha2) {
        ensureInternationalRegionDataLoaded();
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
     * @return 国际地区信息，如果不存在则返回null
     */
    public static InternationalRegionInfo getInternationalRegionInfoByAlpha3(String alpha3) {
        ensureInternationalRegionDataLoaded();
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
     * @return 国际地区信息，如果不存在则返回null
     */
    public static InternationalRegionInfo getInternationalRegionInfoByNumeric(String numeric) {
        ensureInternationalRegionDataLoaded();
        return internationalNumericData.get(numeric);
    }

    /**
     * 确保国际地区数据已加载（双重检查锁定）
     */
    private static void ensureInternationalRegionDataLoaded() {
        if (internationalAlpha3Data == null) {
            synchronized (INTERNATIONAL_LOCK) {
                if (internationalAlpha3Data == null) {
                    loadInternationalRegionData();
                }
            }
        }
    }

    /**
     * 加载国际地区数据（ISO 3166标准编码）
     * <p>
     * 注意：台湾(158/TWN/TW)、香港(344/HKG/HK)、澳门(446/MAC/MO)是中国的一部分，
     * 使用ISO编码是为了处理护照等国际证件的机读码格式，不代表其为国家。
     * </p>
     */
    private static void loadInternationalRegionData() {
        List<List<String>> data;
        try {
            data = FileUtil.readCsvFromFile("/region/iso3166.csv");
        } catch (IOException e) {
            throw new RuntimeException("加载ISO 3166地区数据失败", e);
        }
        Map<String, InternationalRegionInfo> alpha3Map = new ConcurrentHashMap<>();
        Map<String, InternationalRegionInfo> alpha2Map = new ConcurrentHashMap<>();
        Map<String, InternationalRegionInfo> numericMap = new ConcurrentHashMap<>();
        int rowNum = 0;
        for (List<String> row : data) {
            rowNum++;
            if (row.size() < 7) {
                throw new RuntimeException("国际地区数据格式错误，第" + rowNum + "行应有至少7列，实际" + row.size() + "列");
            }
            String chineseShortName = row.get(0);
            String englishShortName = row.get(1);
            String chineseFullName = row.get(2);
            String englishFullName = row.get(3);
            String alpha3 = row.get(4);
            String alpha2 = row.get(5);
            String numeric = row.get(6);
            InternationalRegionInfo internationalRegionInfo = new InternationalRegionInfo(
                    chineseShortName, englishShortName, chineseFullName, englishFullName, alpha3, alpha2, numeric);
            alpha3Map.put(alpha3, internationalRegionInfo);
            alpha2Map.put(alpha2, internationalRegionInfo);
            numericMap.put(numeric, internationalRegionInfo);
        }
        internationalAlpha3Data = alpha3Map;
        internationalAlpha2Data = alpha2Map;
        internationalNumericData = numericMap;
    }
}