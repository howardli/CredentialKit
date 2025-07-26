/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */

package com.xiahaimoyu.credentialkit.enums;

/**
 * 登记管理部门
 *
 * @author Howard.Li
 */
public enum RegistrationDepartment {

    ORG_ESTABLISHMENT("1", "机构编制"),

    FOREIGN_AFFAIRS("2", "外交"),

    JUDICIAL_ADMINISTRATION("3", "司法行政"),

    CULTURE("4", "文化"),

    CIVIL_AFFAIRS("5", "民政"),

    TOURISM("6", "旅游"),

    RELIGION("7", "宗教"),

    LABOR_UNION("8", "工会"),

    MARKET_REGULATION("9", "市场监管"),

    CENTRAL_MILITARY_COMMISSION_REFORM_ORG("A", "中央军委改革和编制办公室"),

    PUBLIC_SECURITY("G", "公安"),

    HOUSING_URBAN_RURAL_DEVELOPMENT("J", "住房城乡建设"),

    AGRICULTURE_RURAL_AFFAIRS("N", "农业农村"),

    OVERSEAS_CHINESE_FEDERATION("Q", "侨联"),

    OTHER("Y", "其他"),

    ;

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
     * @param code 编码
     * @param desc 描述
     */
    RegistrationDepartment(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过编码获取登记管理部门
     *
     * @param code 编码
     * @return 登记管理部门
     */
    public static RegistrationDepartment getByCode(String code) {
        for (RegistrationDepartment i : RegistrationDepartment.values()) {
            if (i.getCode().equals(code)) {
                return i;
            }
        }
        return null;
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
