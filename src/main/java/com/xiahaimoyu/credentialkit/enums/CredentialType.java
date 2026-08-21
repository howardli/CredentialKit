/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

/**
 * 证件类型接口
 * <p>
 * 该接口定义证件类型的基本属性，用于统一不同证件类型的定义。
 * 实现该接口的枚举类定义具体的证件类型及其名称。
 * </p>
 *
 * @author Howard.Li
 */
public interface CredentialType {

    /**
     * 获取证件类型中文名称
     *
     * @return 中文名称
     */
    String getChineseName();

    /**
     * 获取证件类型英文名称
     *
     * @return 英文名称
     */
    String getEnglishName();

    /**
     * 获取智能识别优先级
     * <p>
     * 数值越小优先级越高。当{@code detect}返回多个候选类型时，结果按优先级升序排列，
     * 优先级最高的候选排在最前。默认值为0（最高优先级），内置证件类型使用较大数值，
     * 因此自定义证件类型在识别结果中天然排在内置类型之前。
     * </p>
     *
     * @return 优先级（数值越小优先级越高）
     */
    default int getDetectPriority() {
        return 0;
    }
}
