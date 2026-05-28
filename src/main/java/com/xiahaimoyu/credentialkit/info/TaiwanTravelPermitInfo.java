/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import java.util.Objects;

/**
 * 台湾居民来往大陆通行证信息
 *
 * @author Howard.Li
 */
public final class TaiwanTravelPermitInfo extends CredentialInfo {

    /**
     * 换证次数（未知是-1）
     */
    private int replacementTime = -1;

    /**
     * 获取换证次数
     *
     * @return 换证次数（未知是-1）
     */
    public int getReplacementTime() {
        return replacementTime;
    }

    /**
     * 设置换证次数
     *
     * @param replacementTime 换证次数
     * @return this（链式调用）
     */
    public TaiwanTravelPermitInfo setReplacementTime(int replacementTime) {
        this.replacementTime = replacementTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiwanTravelPermitInfo that = (TaiwanTravelPermitInfo) o;
        return replacementTime == that.replacementTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(replacementTime);
    }

    @Override
    public String toString() {
        return "TaiwanTravelPermitInfo{" +
                "replacementTime=" + replacementTime +
                '}';
    }
}
