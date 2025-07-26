/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.info;

import java.lang.reflect.Field;

/**
 * 证件信息
 *
 * @author Howard.Li
 */
public abstract class CredentialInfo {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("{");
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);  // 允许访问私有字段
            try {
                sb.append(fields[i].getName())
                        .append("=")
                        .append(fields[i].get(this));
            } catch (IllegalAccessException e) {
                sb.append("?");
            }
            if (i < fields.length - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
