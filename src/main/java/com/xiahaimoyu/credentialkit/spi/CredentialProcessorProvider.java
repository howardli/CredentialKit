/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.spi;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;

import java.util.Map;

/**
 * 证件处理器提供者（SPI）
 * <p>
 * 第三方通过Java的{@link java.util.ServiceLoader}机制扩展证件类型：
 * 在jar包的{@code META-INF/services/com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider}
 * 文件中写入实现类的全限定名，实现类返回证件类型到处理器的映射。
 * </p>
 * <p>
 * 使用{@link com.xiahaimoyu.credentialkit.CredentialRegistry#create()}创建的实例会自动加载
 * classpath上的所有提供者；提供者加载失败时异常会向上传播。
 * </p>
 * <p>
 * 实现类必须有公开的无参构造器。
 * </p>
 *
 * @author Howard.Li
 */
public interface CredentialProcessorProvider {

    /**
     * 获取证件类型到处理器的映射
     *
     * @return 映射（key为证件类型，value为对应的处理器）
     */
    Map<CredentialType, CredentialProcessor<? extends CredentialInfo>> getProcessors();
}
