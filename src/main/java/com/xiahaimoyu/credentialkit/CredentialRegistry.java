/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.enums.DefaultCredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.CredentialProcessor;
import com.xiahaimoyu.credentialkit.processor.MainlandResidentIdProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMacaoTravelPermitProcessor;
import com.xiahaimoyu.credentialkit.processor.TaiwanTravelPermitProcessor;
import com.xiahaimoyu.credentialkit.processor.HkMacaoResidencePermitProcessor;
import com.xiahaimoyu.credentialkit.processor.TaiwanResidencePermitProcessor;
import com.xiahaimoyu.credentialkit.processor.ForeignerPermanentResidenceIdProcessor;
import com.xiahaimoyu.credentialkit.processor.MachineReadablePassportProcessor;
import com.xiahaimoyu.credentialkit.processor.UnifiedSocialCreditProcessor;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;
import com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 证件注册中心
 * <p>
 * 持有独立的证件类型到处理器映射，提供校验、解析、智能识别等能力。
 * 不同实例之间注册表互相隔离，适用于测试隔离、多租户等场景。
 * </p>
 * <p>
 * 通过{@link #create()}创建的实例注册全部内置证件处理器，
 * 并通过{@link ServiceLoader}加载classpath上以SPI方式注册的第三方处理器
 * （{@code META-INF/services/com.xiahaimoyu.credentialkit.spi.CredentialProcessorProvider}）。
 * </p>
 *
 * @author Howard.Li
 * @see CredentialKit
 */
public final class CredentialRegistry {

    /**
     * 识别结果排序：优先级升序，优先级相同时按中文名排序（null排在最前，保证顺序确定）
     */
    private static final Comparator<CredentialType> DETECT_ORDER = Comparator
            .comparingInt(CredentialType::getDetectPriority)
            .thenComparing(CredentialType::getChineseName, Comparator.nullsFirst(Comparator.naturalOrder()));

    /**
     * 证件和处理器映射
     */
    private final Map<CredentialType, CredentialProcessor<? extends CredentialInfo>> processors = new ConcurrentHashMap<>();

    /**
     * 构造器
     */
    private CredentialRegistry() {
    }

    /**
     * 创建证件注册中心
     * <p>
     * 注册全部内置证件处理器，并通过{@link ServiceLoader}加载classpath上
     * 以SPI方式注册的第三方处理器。
     * </p>
     *
     * @return 证件注册中心
     */
    public static CredentialRegistry create() {
        CredentialRegistry registry = new CredentialRegistry();
        registry.register(DefaultCredentialType.MAINLAND_RESIDENT_ID, new MainlandResidentIdProcessor());
        registry.register(DefaultCredentialType.HK_MACAO_TRAVEL_PERMIT, new HkMacaoTravelPermitProcessor());
        registry.register(DefaultCredentialType.TAIWAN_TRAVEL_PERMIT, new TaiwanTravelPermitProcessor());
        registry.register(DefaultCredentialType.HK_MACAO_RESIDENCE_PERMIT, new HkMacaoResidencePermitProcessor());
        registry.register(DefaultCredentialType.TAIWAN_RESIDENCE_PERMIT, new TaiwanResidencePermitProcessor());
        registry.register(DefaultCredentialType.FOREIGNER_PERMANENT_RESIDENCE_ID, new ForeignerPermanentResidenceIdProcessor());
        registry.register(DefaultCredentialType.MACHINE_READABLE_PASSPORT, new MachineReadablePassportProcessor());
        registry.register(DefaultCredentialType.UNIFIED_SOCIAL_CREDIT, new UnifiedSocialCreditProcessor());
        loadSpiProviders(registry);
        return registry;
    }

    /**
     * 创建空的证件注册中心
     * <p>
     * 不注册任何处理器，也不加载SPI提供者，由调用方自行注册。
     * </p>
     *
     * @return 证件注册中心
     */
    public static CredentialRegistry createEmpty() {
        return new CredentialRegistry();
    }

    /**
     * 加载SPI提供的第三方处理器
     * <p>
     * 依次扫描线程上下文类加载器和本库的类加载器（按提供者类名去重），
     * 兼容提供者位于webapp等与库不同的类加载器场景。
     * </p>
     * <p>
     * 提供者加载或执行失败时，抛出携带提供者信息的{@link IllegalStateException}并中止创建，
     * 避免残缺的注册表静默生效。
     * </p>
     *
     * @param registry 目标注册中心
     */
    private static void loadSpiProviders(CredentialRegistry registry) {
        Map<String, CredentialProcessorProvider> providers = new LinkedHashMap<>();
        collectProviders(ServiceLoader.load(CredentialProcessorProvider.class), providers);
        ClassLoader libraryLoader = CredentialProcessorProvider.class.getClassLoader();
        if (libraryLoader != null && libraryLoader != Thread.currentThread().getContextClassLoader()) {
            collectProviders(ServiceLoader.load(CredentialProcessorProvider.class, libraryLoader), providers);
        }
        for (CredentialProcessorProvider provider : providers.values()) {
            try {
                Map<CredentialType, CredentialProcessor<? extends CredentialInfo>> provided = provider.getProcessors();
                for (Map.Entry<CredentialType, CredentialProcessor<? extends CredentialInfo>> entry : provided.entrySet()) {
                    registry.register(entry.getKey(), entry.getValue());
                }
            } catch (RuntimeException e) {
                throw new IllegalStateException("SPI证件处理器提供者执行失败: " + provider.getClass().getName(), e);
            }
        }
    }

    /**
     * 收集单个类加载器下的SPI提供者（按类名去重）
     *
     * @param loader   服务加载器
     * @param dedup    提供者去重结果（key是提供者类名）
     */
    private static void collectProviders(ServiceLoader<CredentialProcessorProvider> loader,
                                         Map<String, CredentialProcessorProvider> dedup) {
        Iterator<CredentialProcessorProvider> iterator = loader.iterator();
        while (true) {
            CredentialProcessorProvider provider;
            try {
                if (!iterator.hasNext()) {
                    return;
                }
                provider = iterator.next();
            } catch (ServiceConfigurationError e) {
                throw new IllegalStateException("加载SPI证件处理器提供者失败", e);
            }
            dedup.putIfAbsent(provider.getClass().getName(), provider);
        }
    }

    /**
     * 注册处理器
     *
     * @param type      证件类型
     * @param processor 证件处理器
     * @throws NullPointerException 如果证件类型或证件处理器是空
     */
    public void register(final CredentialType type, final CredentialProcessor<? extends CredentialInfo> processor) {
        Objects.requireNonNull(type, "证件类型是空");
        Objects.requireNonNull(processor, "证件处理器是空");
        processors.put(type, processor);
    }

    /**
     * 注销处理器
     *
     * @param type 证件类型
     * @throws NullPointerException 如果证件类型是空
     */
    public void unregister(final CredentialType type) {
        Objects.requireNonNull(type, "证件类型是空");
        processors.remove(type);
    }

    /**
     * 获取已注册的全部证件类型
     *
     * @return 已注册的证件类型集合（不可变）
     */
    public Set<CredentialType> getSupportedTypes() {
        return Collections.unmodifiableSet(new HashSet<>(processors.keySet()));
    }

    /**
     * 智能识别证件类型
     * <p>
     * 遍历所有已注册的处理器，通过校验逻辑识别证件类型。
     * 校验通过的证件类型会被收集返回，按识别优先级升序排列（优先级相同时按中文名称排序，保证顺序确定）。
     * </p>
     * <p>
     * 注意：证件号码的规格化（去除首尾空格、转大写）由各处理器自行完成，
     * 本方法直接将原始输入交由处理器校验，以保证子类覆盖 {@code normalize} 时在此处同样生效。
     * </p>
     *
     * @param credential 证件号码
     * @return 推断的证件类型列表（空列表表示无匹配，单元素表示唯一类型，多元素表示多个候选）
     */
    public List<CredentialType> detect(final String credential) {
        if (credential == null) {
            return Collections.emptyList();
        }
        List<CredentialType> matchedTypes = new ArrayList<>();
        for (Map.Entry<CredentialType, CredentialProcessor<? extends CredentialInfo>> entry : processors.entrySet()) {
            if (entry.getValue().validate(credential).isValid()) {
                matchedTypes.add(entry.getKey());
            }
        }
        matchedTypes.sort(DETECT_ORDER);
        return matchedTypes;
    }

    /**
     * 获取证件处理器
     *
     * @param type 证件类型
     * @return 证件处理器
     * @throws NullPointerException        如果证件类型是空
     * @throws UnsupportedOperationException 如果不支持该证件类型
     */
    private CredentialProcessor<? extends CredentialInfo> getProcessor(final CredentialType type) {
        Objects.requireNonNull(type, "证件类型是空");
        final CredentialProcessor<? extends CredentialInfo> processor = processors.get(type);
        if (processor == null) {
            throw new UnsupportedOperationException("不支持校验" + type);
        }
        return processor;
    }

    /**
     * 校验证件并返回详细结果
     *
     * @param type       证件类型
     * @param credential 证件号码（允许为null，规格化后为空字符串，校验必然失败）
     * @return 校验结果
     */
    public ValidationResult validate(final CredentialType type, final String credential) {
        final CredentialProcessor<? extends CredentialInfo> processor = getProcessor(type);
        return processor.validate(credential);
    }

    /**
     * 解析证件
     * <p>
     * 解析成功后，证件信息的{@link CredentialInfo#getType()}会被设置为实际注册的证件类型。
     * </p>
     *
     * @param type       证件类型
     * @param credential 证件号码（允许为null，规格化后为空字符串，解析必然失败）
     * @return 解析后的证件信息，如果解析失败则返回Optional.empty()
     */
    public Optional<? extends CredentialInfo> parse(final CredentialType type, final String credential) {
        final CredentialProcessor<? extends CredentialInfo> processor = getProcessor(type);
        return processor.parse(credential).map(info -> {
            info.setType(type);
            return info;
        });
    }

    /**
     * 解析证件并返回指定类型
     * <p>
     * 与{@link #parse(CredentialType, String)}相比，本方法直接返回具体类型，调用方无需强转。
     * 如果解析结果的实际类型与{@code infoClass}不符则抛出{@link ClassCastException}。
     * </p>
     *
     * @param type       证件类型
     * @param credential 证件号码
     * @param infoClass  证件信息类型
     * @param <T>        证件信息类型
     * @return 解析后的证件信息，如果解析失败则返回Optional.empty()
     * @throws NullPointerException 如果infoClass是空
     * @throws ClassCastException   如果解析结果的实际类型与infoClass不符
     */
    public <T extends CredentialInfo> Optional<T> parse(final CredentialType type, final String credential, final Class<T> infoClass) {
        Objects.requireNonNull(infoClass, "证件信息类型是空");
        return parse(type, credential).map(infoClass::cast);
    }
}
