/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.processor;

import com.xiahaimoyu.credentialkit.enums.ErrorCode;
import com.xiahaimoyu.credentialkit.enums.Gender;
import com.xiahaimoyu.credentialkit.exception.CredentialException;
import com.xiahaimoyu.credentialkit.info.InternationalRegionInfo;
import com.xiahaimoyu.credentialkit.info.MachineReadablePassportCodeInfo;
import com.xiahaimoyu.credentialkit.util.CheckDigitUtil;
import com.xiahaimoyu.credentialkit.util.DateUtil;
import com.xiahaimoyu.credentialkit.util.RegionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 可机读护照编码处理器
 *
 * @author Howard.Li
 */
public class MachineReadablePassportCodeProcessor extends CredentialProcessor<MachineReadablePassportCodeInfo> {

    /**
     * 基础校验正则
     */
    private static final Pattern PATTERN = Pattern.compile("^P[A-Z<][A-Z<]{3}[A-Z<]{39}[A-Z0-9<]{9}[0-9][A-Z<]{3}[0-9]{6}[0-9][MF<][0-9]{6}[0-9][A-Z0-9<]{14}[0-9<][0-9]$");

    /**
     * 姓名正则
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^([A-Z]+<)*[A-Z]+(<<([A-Z]+<)*[A-Z]+)?$");

    /**
     * 护照编码中和ISO标准不一致的地区和组织
     */
    private static final Map<String, InternationalRegionInfo> extInternationalRegionInfoMap;

    static {
        extInternationalRegionInfoMap = new HashMap<>();
        InternationalRegionInfo regionInfo = new InternationalRegionInfo("英国海外领土公民", null, "英国海外领土公民", "British Overseas Territories Citizen", "GBD", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "英国国民（海外）", "British National(Overseas)", "GBN", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "英国海外公民", "British Overseas Citizen", "GBO", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "英籍人士", "British Subject", "GBS", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "受英国保护人士", "British Protected Person", "GBP", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo("德国", "GERMANY", "德意志联邦共和国", "the Federal Republic of Germany", "D<<", "DE", "276");
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo("科索沃", "KOSOVO", "科索沃共和国", "the Republic of Kosovo", "RKS", "KS", null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "欧盟", "European Union(EU)", "EUE", "EU", null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "联合国组织或者该组织的一名官员", "United Nations Organization or one of its officials", "UNO", "UN", null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "联合国专门机构或者该机构的一名官员", "United Nations specialized agency or one of its officials", "UNA", "UN", null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "持有联合国驻科索沃临时管理特派团（UNMIK）签发的旅行证件的科索沃居民", "Resident of Kosovo to whom a travel document has been issued by the United Nations Interim Administration Mission in Kosovo(UNMIK)", "UNK", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "非洲开发银行（ADB）", "African Development Bank (ADB)", "XBA", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "非洲进出口银行（AFREXIM）", "African Export-Import Bank (AFREXIM bank)", "XIM", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "加勒比共同体或其一名使者（CARICOM）", "Caribbean Community or one of its emissaries (CARICOM)", "XCC", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "欧洲理事会", "Council of Europe", "XCE", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "东部和南部非洲共同市场（COMESA）", "Common Market for Eastern and Southern Africa (COMESA)", "XCO", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "西非国家经济共同体（ECOWAS）", "Economic Community of West African States (ECOWAS)", "XEC", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "国际刑事警察组织（INTERPOL）", "International Criminal Police Organization (INTERPOL)", "XPO", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "东加勒比国家组织（OECS）", "Organization of Eastern Caribbean States (OECS)", "XES", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "地中海议会大会（PAM）", "Parliamentary Assembly of the Mediterranean (PAM)", "XMP", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "马耳他最高军教团或其一名使者", "Sovereign Military Order of Malta or one of its emissaries", "XOM", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "南部非洲发展共同体", "Southern African Development Community", "XDC", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "1954年《无国籍人地位公约》第1条定义的无国籍人", "Stateless person, as defined in Article 1 of the 1954 Convention Relating to the Status of Stateless Persons", "XXA", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "经1967年议定书修订的1951年《难民地位公约》第1条定义的难民", "Refugee, as defined in Article 1 of the 1951 Convention Relating to the Status of Refugees as amended by the 1967 Protocol", "XXB", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "不在上述XXB代码定义之内的难民", "Refugee, other than as defined under the code XXB above", "XXC", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
        regionInfo = new InternationalRegionInfo(null, null, "未确定国籍的人，签发国认为不管该人的地位是什么，没有必要为其确定上述任何XXA、XXB或XXC代码。该类可以包括既不是无国籍人，也不是难民，而是在签发国合法居住的不明国籍的人", "Person of unspecified nationality, for whom issuing State does not consider it necessary to specify any of the codes XXA, XXB or XXC above, whatever that person’s status may be. This category may include a person who is neither stateless nor a refugee but who is of unknown nationality and legally residing in the State of issue", "XXX", null, null);
        extInternationalRegionInfoMap.put(regionInfo.getAlpha3(), regionInfo);
    }

    /**
     * 构造校验器列表
     *
     * @return 校验器列表
     */
    @Override
    protected List<CredentialValidator> buildValidators() {
        return Arrays.asList(
                //基本格式校验
                credential -> {
                    if (credential == null || credential.length() != 88 || !PATTERN.matcher(credential).matches()) {
                        throw CredentialException.of(ErrorCode.BASIC_FORMAT_ERROR, "基本格式校验失败：{0}", credential);
                    }
                },
                //校验签发地区
                credential -> {
                    String regionCode = credential.substring(2, 5);
                    if (getRegionInfo(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "签发地区不对：{0}", regionCode);
                    }
                },
                //校验名字
                credential -> {
                    String name = rightTrim(credential.substring(5, 44));
                    if (!NAME_PATTERN.matcher(name).matches()) {
                        throw CredentialException.of(ErrorCode.NAME_ERROR, "名字不对：{0}", name);
                    }
                },
                //校验护照号码校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(44, 53));
                    if (checkDigit != credential.charAt(53)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(53));
                    }
                },
                //校验归属地
                credential -> {
                    String regionCode = credential.substring(54, 57);
                    if (getRegionInfo(regionCode) == null) {
                        throw CredentialException.of(ErrorCode.REGION_ERROR, "地区不对：{0}", regionCode);
                    }
                },
                //校验生日
                credential -> {
                    String birthDate = credential.substring(57, 63);
                    if (!DateUtil.validDateBeforeNow("19" + birthDate) && !DateUtil.validDateBeforeNow("20" + birthDate)) {
                        throw CredentialException.of(ErrorCode.BIRTH_DATE_ERROR, "生日不对：{0}", birthDate);
                    }
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(birthDate);
                    if (checkDigit != credential.charAt(63)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(63));
                    }
                },
                //校验有效期
                credential -> {
                    String expirationDate = credential.substring(65, 71);
                    if (!DateUtil.validDate("19" + expirationDate) && !DateUtil.validDate("20" + expirationDate)) {
                        throw CredentialException.of(ErrorCode.EXPIRATION_DATE_ERROR, "有效期不对：{0}", expirationDate);
                    }
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(expirationDate);
                    if (checkDigit != credential.charAt(71)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(71));
                    }
                },
                //校验个人号码校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(72, 86));
                    if (checkDigit != credential.charAt(86)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(86));
                    }
                },
                //校验护照校验位
                credential -> {
                    char checkDigit = CheckDigitUtil.getMachineReadablePassportCodeCheckDigit(credential.substring(44, 54) + credential.substring(57, 64) + credential.substring(65, 87));
                    if (checkDigit != credential.charAt(87)) {
                        throw CredentialException.of(ErrorCode.CHECK_DIGIT_ERROR, "校验位不对：预期是{0}，实际是{1}", checkDigit, credential.charAt(87));
                    }
                }

        );
    }

    /**
     * 构造解析器列表
     *
     * @return 解析器列表
     */
    @Override
    protected List<CredentialParser<MachineReadablePassportCodeInfo>> buildParsers() {
        return Arrays.asList(
                //解析签发地区
                (credential, info) -> {
                    String regionCode = credential.substring(2, 5);
                    info.setIssuingRegion(getRegionInfo(regionCode));
                },
                //解析名字
                (credential, info) -> {
                    String name = rightTrim(credential.substring(5, 44));
                    String[] names = name.split("<<");
                    info.setSurname(names[0].replace("<", " "));
                    if (names.length > 1) {
                        info.setGivenName(names[1].replace("<", " "));
                    }
                },
                //解析护照号
                (credential, info) -> {
                    info.setPassportNumber(credential.substring(44, 53));
                },
                //解析归属地
                (credential, info) -> {
                    String regionCode = credential.substring(54, 57);
                    info.setRegion(getRegionInfo(regionCode));
                },
                //解析生日
                (credential, info) -> {
                    info.setBirthdate(credential.substring(57, 63));
                },
                //解析性别
                (credential, info) -> {
                    char gender = credential.charAt(64);
                    if (gender == 'M') {
                        info.setGender(Gender.MALE);
                    } else if (gender == 'F') {
                        info.setGender(Gender.FEMALE);
                    } else if (gender == '<') {
                        info.setGender(Gender.UNKNOWN);
                    }
                },
                //解析有效期
                (credential, info) -> {
                    info.setExpirationDate(credential.substring(65, 71));
                },
                //解析个人号码
                (credential, info) -> {
                    String personalNumber = rightTrim(credential.substring(72, 86)).replace("<", " ");
                    info.setPersonalNumber(personalNumber);
                }
        );
    }

    /**
     * 获取可机读护照编码信息
     *
     * @return 可机读护照编码信息
     */
    @Override
    protected MachineReadablePassportCodeInfo getInfo() {
        return new MachineReadablePassportCodeInfo();
    }

    /**
     * 去掉结尾的<
     *
     * @param str 字符串
     * @return 去掉结尾的<后的字符串
     */
    private String rightTrim(String str) {
        return str.replaceAll("<+$", "");
    }

    /**
     * 获取地区
     *
     * @param regionCode 地区3位字母码
     * @return 地区信息
     */
    private InternationalRegionInfo getRegionInfo(String regionCode) {
        InternationalRegionInfo regionInfo = RegionUtil.getInternationalRegionInfoByAlpha3(regionCode);
        if (regionInfo == null) {
            regionInfo = extInternationalRegionInfoMap.get(regionCode);
        }
        return regionInfo;
    }
}
