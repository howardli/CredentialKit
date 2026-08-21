/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.spi.TestSpiCredentialProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SPI自动发现测试
 * <p>
 * 测试类路径上存在
 * {@code META-INF/services/com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider}
 * 注册的{@link TestSpiCredentialProvider}，验证{@link CredentialRegistry#create()}
 * 能自动加载第三方处理器。
 * </p>
 */
class SpiDiscoveryTest {

    @Test
    void createLoadsSpiProviders() {
        CredentialRegistry registry = CredentialRegistry.create();
        assertThat(registry.getSupportedTypes()).contains(TestSpiCredentialProvider.TestSpiCredentialType.SPI_TEST_ID);
        assertThat(registry.validate(TestSpiCredentialProvider.TestSpiCredentialType.SPI_TEST_ID, "SPI123456").isValid()).isTrue();
        assertThat(registry.validate(TestSpiCredentialProvider.TestSpiCredentialType.SPI_TEST_ID, "SPI12345").isValid()).isFalse();
    }

    @Test
    void createEmptySkipsSpiProviders() {
        CredentialRegistry registry = CredentialRegistry.createEmpty();
        assertThat(registry.getSupportedTypes()).doesNotContain(TestSpiCredentialProvider.TestSpiCredentialType.SPI_TEST_ID);
    }

    @Test
    void defaultRegistryIncludesSpiProviders() {
        assertThat(CredentialKit.getSupportedTypes()).contains(TestSpiCredentialProvider.TestSpiCredentialType.SPI_TEST_ID);
        List<CredentialType> types = CredentialKit.detect("SPI123456");
        assertThat(types).containsExactly(TestSpiCredentialProvider.TestSpiCredentialType.SPI_TEST_ID);
    }
}
