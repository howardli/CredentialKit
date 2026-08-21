/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import com.xiahaimoyu.credentialkit.info.DomesticRegionInfo;
import com.xiahaimoyu.credentialkit.info.InternationalRegionInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
 * 数据采用"不可变快照 + 复制写入"模式：读取路径直接在不可变{@code HashMap}上查找，
 * 无锁且最快；{@code addXxx}/{@code removeXxx}在锁内复制整表后整体替换快照引用，
 * 保证线程安全。初始数据懒加载（双重检查锁定）。
 * </p>
 * <p>
 * 注意：首次访问会懒加载CSV数据（实测约100~150ms，主要是资源I/O）。
 * 对冷启动延迟敏感的服务，可在启动阶段调用
 * {@code RegionUtil.getDomesticRegionInfoByCode("110000")}和
 * {@code RegionUtil.getInternationalRegionInfoByAlpha3("CHN")}预热。
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
     * 国际地区数据（ISO 3166标准）
     */
    private static volatile InternationalRegionData internationalRegionData;

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
     * 国际地区数据快照（三种编码到同一地区信息的视图）
     */
    private static final class InternationalRegionData {

        /**
         * key是3位字母编码
         */
        private final Map<String, InternationalRegionInfo> byAlpha3;

        /**
         * key是2位字母编码
         */
        private final Map<String, InternationalRegionInfo> byAlpha2;

        /**
         * key是数字编码
         */
        private final Map<String, InternationalRegionInfo> byNumeric;

        InternationalRegionData(Map<String, InternationalRegionInfo> byAlpha3,
                                Map<String, InternationalRegionInfo> byAlpha2,
                                Map<String, InternationalRegionInfo> byNumeric) {
            this.byAlpha3 = byAlpha3;
            this.byAlpha2 = byAlpha2;
            this.byNumeric = byNumeric;
        }
    }

    /**
     * 获取国内地区数据快照（懒加载，双重检查锁定）
     *
     * @return 国内地区数据
     */
    private static Map<String, DomesticRegionInfo> domesticData() {
        Map<String, DomesticRegionInfo> data = domesticRegionCodeData;
        if (data == null) {
            synchronized (DOMESTIC_LOCK) {
                if (domesticRegionCodeData == null) {
                    domesticRegionCodeData = loadDomesticRegionData();
                }
                data = domesticRegionCodeData;
            }
        }
        return data;
    }

    /**
     * 通过编码获取国内地区
     *
     * @param code 编码
     * @return 国内地区，如果不存在则返回null
     */
    public static DomesticRegionInfo getDomesticRegionInfoByCode(String code) {
        return domesticData().get(code);
    }

    /**
     * 添加或覆盖国内地区数据
     *
     * @param domesticRegionInfo 国内地区信息
     * @throws NullPointerException 如果domesticRegionInfo为空或其code为空
     */
    public static void addDomesticRegionData(DomesticRegionInfo domesticRegionInfo) {
        Objects.requireNonNull(domesticRegionInfo, "国内地区信息是空");
        Objects.requireNonNull(domesticRegionInfo.getCode(), "地区编码不能为空");
        synchronized (DOMESTIC_LOCK) {
            Map<String, DomesticRegionInfo> copy = new HashMap<>(domesticData());
            copy.put(domesticRegionInfo.getCode(), domesticRegionInfo);
            domesticRegionCodeData = Collections.unmodifiableMap(copy);
        }
    }

    /**
     * 移除国内地区数据
     *
     * @param code 编码
     * @return 被移除的地区信息，如果不存在则返回null
     */
    public static DomesticRegionInfo removeDomesticRegionData(String code) {
        Objects.requireNonNull(code, "地区编码不能为空");
        synchronized (DOMESTIC_LOCK) {
            DomesticRegionInfo removed = domesticData().get(code);
            if (removed != null) {
                Map<String, DomesticRegionInfo> copy = new HashMap<>(domesticData());
                copy.remove(code);
                domesticRegionCodeData = Collections.unmodifiableMap(copy);
            }
            return removed;
        }
    }

    /**
     * 加载国内地区数据（GB/T 2260《中华人民共和国行政区划代码》）
     *
     * @return 国内地区数据（不可变）
     */
    private static Map<String, DomesticRegionInfo> loadDomesticRegionData() {
        List<List<String>> data;
        try {
            data = FileUtil.readCsvFromFile("/region/gb2260.csv");
        } catch (IOException e) {
            throw new RuntimeException("加载GB/T 2260地区数据失败", e);
        }
        Map<String, DomesticRegionInfo> codeMap = new HashMap<>();
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
        return Collections.unmodifiableMap(codeMap);
    }

    /**
     * 获取国际地区数据快照（懒加载，双重检查锁定）
     *
     * @return 国际地区数据
     */
    private static InternationalRegionData internationalData() {
        InternationalRegionData data = internationalRegionData;
        if (data == null) {
            synchronized (INTERNATIONAL_LOCK) {
                if (internationalRegionData == null) {
                    internationalRegionData = loadInternationalRegionData();
                }
                data = internationalRegionData;
            }
        }
        return data;
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
        return internationalData().byAlpha2.get(alpha2);
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
        return internationalData().byAlpha3.get(alpha3);
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
        return internationalData().byNumeric.get(numeric);
    }

    /**
     * 添加或覆盖国际地区数据（ISO 3166标准）
     *
     * @param internationalRegionInfo 国际地区信息
     * @throws NullPointerException 如果internationalRegionInfo为空或其alpha3为空
     */
    public static void addInternationalRegionData(InternationalRegionInfo internationalRegionInfo) {
        Objects.requireNonNull(internationalRegionInfo, "国际地区信息是空");
        Objects.requireNonNull(internationalRegionInfo.getAlpha3(), "alpha3编码不能为空");
        synchronized (INTERNATIONAL_LOCK) {
            InternationalRegionData current = internationalData();
            Map<String, InternationalRegionInfo> alpha3Copy = new HashMap<>(current.byAlpha3);
            Map<String, InternationalRegionInfo> alpha2Copy = new HashMap<>(current.byAlpha2);
            Map<String, InternationalRegionInfo> numericCopy = new HashMap<>(current.byNumeric);
            alpha3Copy.put(internationalRegionInfo.getAlpha3(), internationalRegionInfo);
            if (internationalRegionInfo.getAlpha2() != null) {
                alpha2Copy.put(internationalRegionInfo.getAlpha2(), internationalRegionInfo);
            }
            if (internationalRegionInfo.getNumeric() != null) {
                numericCopy.put(internationalRegionInfo.getNumeric(), internationalRegionInfo);
            }
            internationalRegionData = new InternationalRegionData(
                    Collections.unmodifiableMap(alpha3Copy),
                    Collections.unmodifiableMap(alpha2Copy),
                    Collections.unmodifiableMap(numericCopy));
        }
    }

    /**
     * 移除国际地区数据（按3位字母编码）
     * <p>
     * 该地区对应的2位字母编码和数字编码视图会一并移除。
     * </p>
     *
     * @param alpha3 3位字母编码
     * @return 被移除的地区信息，如果不存在则返回null
     */
    public static InternationalRegionInfo removeInternationalRegionData(String alpha3) {
        Objects.requireNonNull(alpha3, "alpha3编码不能为空");
        synchronized (INTERNATIONAL_LOCK) {
            InternationalRegionData current = internationalData();
            InternationalRegionInfo removed = current.byAlpha3.get(alpha3);
            if (removed != null) {
                Map<String, InternationalRegionInfo> alpha3Copy = new HashMap<>(current.byAlpha3);
                Map<String, InternationalRegionInfo> alpha2Copy = new HashMap<>(current.byAlpha2);
                Map<String, InternationalRegionInfo> numericCopy = new HashMap<>(current.byNumeric);
                alpha3Copy.remove(alpha3);
                alpha2Copy.values().removeIf(removed::equals);
                numericCopy.values().removeIf(removed::equals);
                internationalRegionData = new InternationalRegionData(
                        Collections.unmodifiableMap(alpha3Copy),
                        Collections.unmodifiableMap(alpha2Copy),
                        Collections.unmodifiableMap(numericCopy));
            }
            return removed;
        }
    }

    /**
     * 加载国际地区数据（ISO 3166标准编码）
     * <p>
     * 注意：台湾(158/TWN/TW)、香港(344/HKG/HK)、澳门(446/MAC/MO)是中国的一部分，
     * 使用ISO编码是为了处理护照等国际证件的机读码格式，不代表其为国家。
     * </p>
     *
     * @return 国际地区数据（不可变）
     */
    private static InternationalRegionData loadInternationalRegionData() {
        List<List<String>> data;
        try {
            data = FileUtil.readCsvFromFile("/region/iso3166.csv");
        } catch (IOException e) {
            throw new RuntimeException("加载ISO 3166地区数据失败", e);
        }
        Map<String, InternationalRegionInfo> alpha3Map = new HashMap<>();
        Map<String, InternationalRegionInfo> alpha2Map = new HashMap<>();
        Map<String, InternationalRegionInfo> numericMap = new HashMap<>();
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
        return new InternationalRegionData(
                Collections.unmodifiableMap(alpha3Map),
                Collections.unmodifiableMap(alpha2Map),
                Collections.unmodifiableMap(numericMap));
    }
}
