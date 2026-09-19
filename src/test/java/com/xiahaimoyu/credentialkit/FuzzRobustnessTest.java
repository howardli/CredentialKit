/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 模糊健壮性测试
 * <p>
 * 校验库的核心契约是：对任意输入（包括格式损坏的输入）validate/parse/detect都不抛出异常，
 * 而是返回校验失败/空结果。本测试用固定种子的随机变异合法样本和纯随机字符串
 * 对该契约做批量验证，任何校验器中的缺陷（如非法字符触发的运行时异常）都会在此暴露。
 * </p>
 * <p>
 * 种子固定保证失败可复现；变异操作包括替换、插入、删除、截断和大小写翻转，
 * 覆盖"接近合法但损坏"的边界输入。
 * </p>
 */
class FuzzRobustnessTest {

    /**
     * 随机种子（固定，失败可复现）
     */
    private static final long SEED = 20260920L;

    /**
     * 变异字符池（含USCI字符集外的I/O/S/V/Z、小写、填充符、空白、标点和多语言字符）
     */
    private static final String DIRTY_ALPHABET = "0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "<>,.-_@#$%&*()[]{}~^|:;?!/ \t\n\r"
            + "测试漢字한국어Ελληνικάрусскийéàü";

    /**
     * 各证件类型的合法样本（变异的母本）
     */
    private static final List<String> VALID_SAMPLES = Arrays.asList(
            "330105197810270025",
            "330105781027002",
            "810000199001010019",
            "830000199001010012",
            "H12345678",
            "M1234567801",
            "12345678",
            "1234567801",
            "KAZ110090123105",
            "911398199012310021",
            "91330106MA27Y4U47R",
            "A1110000MA27Y4U477",
            "POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<G489476464CHN7304279M210126619203301<<<<<<16",
            "POCHNZHANG<<SAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<E1234567<4CHN7304279M2101266<<<<<<<<<<<<<<04",
            "SPI123456"
    );

    /**
     * 注册中心（含SPI加载的测试类型）
     */
    private final CredentialRegistry registry = CredentialRegistry.create();

    @Test
    void mutatedValidCredentialsNeverThrow() {
        assertThat(registry.getSupportedTypes()).hasSizeGreaterThanOrEqualTo(8);
        Random random = new Random(SEED);
        for (int i = 0; i < 30_000; i++) {
            String sample = VALID_SAMPLES.get(random.nextInt(VALID_SAMPLES.size()));
            exerciseAllTypes(mutate(random, sample));
        }
    }

    @Test
    void randomStringsNeverThrow() {
        assertThat(registry.getSupportedTypes()).hasSizeGreaterThanOrEqualTo(8);
        Random random = new Random(SEED + 1);
        for (int i = 0; i < 30_000; i++) {
            exerciseAllTypes(randomString(random, random.nextInt(96)));
        }
    }

    /**
     * 对输入做全量校验、解析与识别
     * <p>
     * 本方法不显式断言：任何环节抛出异常即测试失败，这正是被验证的契约。
     * </p>
     *
     * @param input 任意输入
     */
    private void exerciseAllTypes(String input) {
        for (CredentialType type : registry.getSupportedTypes()) {
            registry.validate(type, input);
            registry.parse(type, input);
        }
        registry.detect(input);
    }

    /**
     * 随机变异合法样本
     *
     * @param random 随机源
     * @param sample 合法样本
     * @return 变异后的样本（可能恰好仍合法，也可能损坏）
     */
    private static String mutate(Random random, String sample) {
        if (sample.isEmpty()) {
            return sample;
        }
        switch (random.nextInt(5)) {
            case 0: {
                // 替换一个字符
                char[] chars = sample.toCharArray();
                chars[random.nextInt(chars.length)] = DIRTY_ALPHABET.charAt(random.nextInt(DIRTY_ALPHABET.length()));
                return new String(chars);
            }
            case 1: {
                // 插入一个字符
                int position = random.nextInt(sample.length() + 1);
                char inserted = DIRTY_ALPHABET.charAt(random.nextInt(DIRTY_ALPHABET.length()));
                return sample.substring(0, position) + inserted + sample.substring(position);
            }
            case 2: {
                // 删除一个字符
                if (sample.length() <= 1) {
                    return sample;
                }
                int position = random.nextInt(sample.length());
                return sample.substring(0, position) + sample.substring(position + 1);
            }
            case 3: {
                // 截断
                return sample.substring(0, 1 + random.nextInt(sample.length()));
            }
            default: {
                // 大小写翻转
                int position = random.nextInt(sample.length());
                char c = sample.charAt(position);
                char flipped = Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c);
                return sample.substring(0, position) + flipped + sample.substring(position + 1);
            }
        }
    }

    /**
     * 生成纯随机字符串
     *
     * @param random 随机源
     * @param length 长度
     * @return 随机字符串
     */
    private static String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(DIRTY_ALPHABET.charAt(random.nextInt(DIRTY_ALPHABET.length())));
        }
        return sb.toString();
    }
}
