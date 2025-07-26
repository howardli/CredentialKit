/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

/**
 * 台湾居民来往大陆通行证号码信息
 *
 * @author Howard.Li
 */
public class TwTravelPermitNumberInfo extends CredentialInfo {

    /**
     * 换证次数（未知是-1）
     */
    private int replacementTime = -1;

    /**
     * 获取换证次数
     *
     * @return 换证次数
     */
    public int getReplacementTime() {
        return replacementTime;
    }

    /**
     * 设置换证次数
     *
     * @param replacementTime 换证次数
     */
    public void setReplacementTime(int replacementTime) {
        this.replacementTime = replacementTime;
    }

}
