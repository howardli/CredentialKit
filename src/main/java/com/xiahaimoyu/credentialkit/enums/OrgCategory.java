/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 机构类别
 *
 * @author Howard.Li
 */
public enum OrgCategory {

    ORG_ESTABLISHMENT_ORGAN(RegistrationDepartment.ORG_ESTABLISHMENT, "11", "机关"),
    ORG_ESTABLISHMENT_INSTITUTION(RegistrationDepartment.ORG_ESTABLISHMENT, "12", "事业单位"),
    ORG_ESTABLISHMENT_MASS_ORG(RegistrationDepartment.ORG_ESTABLISHMENT, "13", "编办直接管理机构编制的群众团体"),
    ORG_ESTABLISHMENT_OTHER(RegistrationDepartment.ORG_ESTABLISHMENT, "19", "其他"),

    FOREIGN_AFFAIRS_NEWS_AGENCY(RegistrationDepartment.FOREIGN_AFFAIRS, "21", "外国常驻新闻机构"),
    FOREIGN_AFFAIRS_OTHER(RegistrationDepartment.FOREIGN_AFFAIRS, "29", "其他"),

    JUDICIAL_ADMINISTRATION_LAWYER(RegistrationDepartment.JUDICIAL_ADMINISTRATION, "31", "律师执业机构"),
    JUDICIAL_ADMINISTRATION_COMMERCIAL_MEDIATION(RegistrationDepartment.JUDICIAL_ADMINISTRATION, "36", "商事调节组织"),
    JUDICIAL_ADMINISTRATION_OTHER(RegistrationDepartment.JUDICIAL_ADMINISTRATION, "39", "其他"),

    CULTURE_CULTURAL_CENTER(RegistrationDepartment.CULTURE, "41", "外国在华文化中心"),
    CULTURE_OTHER(RegistrationDepartment.CULTURE, "49", "其他"),

    CIVIL_AFFAIRS_SOCIAL_GROUP(RegistrationDepartment.CIVIL_AFFAIRS, "51", "社会团体"),
    CIVIL_AFFAIRS_PRIVATE_NON_ENTERPRISE(RegistrationDepartment.CIVIL_AFFAIRS, "52", "民办非企业单位"),
    CIVIL_AFFAIRS_FOUNDATION(RegistrationDepartment.CIVIL_AFFAIRS, "53", "基金会"),
    CIVIL_AFFAIRS_VILLAGE_COMMITTEE(RegistrationDepartment.CIVIL_AFFAIRS, "54", "村民委员会"),
    CIVIL_AFFAIRS_RESIDENT_COMMITTEE(RegistrationDepartment.CIVIL_AFFAIRS, "55", "居民委员会"),
    CIVIL_AFFAIRS_OTHER(RegistrationDepartment.CIVIL_AFFAIRS, "59", "其他"),

    TOURISM_FOREIGN_TOURISM_REP(RegistrationDepartment.TOURISM, "61", "外国政府旅游部门常驻代表机构"),
    TOURISM_HMT_TOURISM_REP(RegistrationDepartment.TOURISM, "62", "港澳台地区旅游部门常驻代表机构"),
    TOURISM_OTHER(RegistrationDepartment.TOURISM, "69", "其他"),


    RELIGION_ACTIVITY_SITE(RegistrationDepartment.RELIGION, "71", "宗教活动场所"),
    RELIGION_SCHOOL(RegistrationDepartment.RELIGION, "72", "宗教院校"),
    RELIGION_OTHER(RegistrationDepartment.RELIGION, "79", "其他"),

    LABOR_UNION_GRASSROOTS(RegistrationDepartment.LABOR_UNION, "81", "基层工会"),
    LABOR_UNION_INFORMAL(RegistrationDepartment.LABOR_UNION, "82", "非法人工会组织"),
    LABOR_UNION_OTHER(RegistrationDepartment.LABOR_UNION, "89", "其他"),


    MARKET_REGULATION_ENTERPRISE(RegistrationDepartment.MARKET_REGULATION, "91", "企业"),
    MARKET_REGULATION_INDIVIDUAL(RegistrationDepartment.MARKET_REGULATION, "92", "个体工商户"),
    MARKET_REGULATION_FARMERS_COOP(RegistrationDepartment.MARKET_REGULATION, "93", "农民专业合作社"),
    MARKET_REGULATION_OTHER(RegistrationDepartment.MARKET_REGULATION, "99", "其他"),

    CENTRAL_MILITARY_COMMISSION_REFORM_ORG_CIVILIAN(RegistrationDepartment.CENTRAL_MILITARY_COMMISSION_REFORM_ORG, "A1", "军队文职人员用人单位"),
    CENTRAL_MILITARY_COMMISSION_REFORM_ORG_OTHER(RegistrationDepartment.CENTRAL_MILITARY_COMMISSION_REFORM_ORG, "A9", "其他"),

    PUBLIC_SECURITY_POLICE_FOREIGN_NGO(RegistrationDepartment.PUBLIC_SECURITY, "G1", "境外非政府组织代表机构"),
    PUBLIC_SECURITY_OTHER(RegistrationDepartment.PUBLIC_SECURITY, "G9", "其他"),

    HOUSING_URBAN_RURAL_DEVELOPMENT_CONSTRUCTION_OWNER_MEETING(RegistrationDepartment.HOUSING_URBAN_RURAL_DEVELOPMENT, "J1", "业主大会"),
    HOUSING_URBAN_RURAL_DEVELOPMENT_OTHER(RegistrationDepartment.HOUSING_URBAN_RURAL_DEVELOPMENT, "J9", "其他"),

    AGRICULTURE_RURAL_AFFAIRS_GROUP_ECONOMY(RegistrationDepartment.AGRICULTURE_RURAL_AFFAIRS, "N1", "组级集体经济组织"),
    AGRICULTURE_RURAL_AFFAIRS_VILLAGE_ECONOMY(RegistrationDepartment.AGRICULTURE_RURAL_AFFAIRS, "N2", "村级集体经济组织"),
    AGRICULTURE_RURAL_AFFAIRS_TOWNSHIP_ECONOMY(RegistrationDepartment.AGRICULTURE_RURAL_AFFAIRS, "N3", "乡镇级集体经济组织"),
    AGRICULTURE_RURAL_AFFAIRS_OTHER(RegistrationDepartment.AGRICULTURE_RURAL_AFFAIRS, "N9", "其他"),


    OVERSEAS_CHINESE_FEDERATION_DISTRICT(RegistrationDepartment.OVERSEAS_CHINESE_FEDERATION, "Q1", "区县级侨联组织"),
    OVERSEAS_CHINESE_FEDERATION_TOWNSHIP(RegistrationDepartment.OVERSEAS_CHINESE_FEDERATION, "Q2", "乡镇级侨联组织"),
    OVERSEAS_CHINESE_FEDERATION_VILLAGE(RegistrationDepartment.OVERSEAS_CHINESE_FEDERATION, "Q3", "村级侨联组织"),
    OVERSEAS_CHINESE_FEDERATION_OTHER(RegistrationDepartment.OVERSEAS_CHINESE_FEDERATION, "Q9", "其他"),

    OTHER_OTHER(RegistrationDepartment.OTHER, "Y1", "其他"),
    ;

    /**
     * 登记管理部门
     */
    private final RegistrationDepartment registrationDepartment;

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param registrationDepartment 登记管理部门
     * @param code                   编码
     * @param desc                   描述
     */
    OrgCategory(RegistrationDepartment registrationDepartment, String code, String desc) {
        this.registrationDepartment = registrationDepartment;
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过编码获取机构类别
     *
     * @param code 编码
     * @return 机构类别
     */
    public static OrgCategory getByCode(String code) {
        for (OrgCategory i : OrgCategory.values()) {
            if (i.getCode().equals(code)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 获取登记管理部门
     *
     * @return 登记管理部门
     */
    public RegistrationDepartment getRegistrationDepartment() {
        return registrationDepartment;
    }

    /**
     * 获取编码
     *
     * @return 编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }
}
