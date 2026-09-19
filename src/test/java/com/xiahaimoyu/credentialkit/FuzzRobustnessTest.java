/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit;

import com.xiahaimoyu.credentialkit.enums.CredentialType;
import com.xiahaimoyu.credentialkit.info.CredentialInfo;
import com.xiahaimoyu.credentialkit.processor.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 随机模糊测试
 * <p>
 * 守卫核心契约：任意非法输入（含中文、代理对、MRZ填充符、空白等）经过
 * validate/parse/detect 都不会以异常形式抛出。
 * 使用固定随机种子保证可复现。
 * </p>
 *
 * @author Howard.Li
 */
class FuzzRobustnessTest {

    /**
     * 随机输入字母表（数字、大小写字母、MRZ填充符、标点、中文、全角空格、孤立代理对）
     */
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz<>,.'\"-_测中文。　\uD800 ";

    /**
     * 固定随机种子（可复现）
     */
    private static final long SEED = 20260920L;

    /**
     * 随机输入轮数
     */
    private static final int ROUNDS = 20000;

    private final CredentialRegistry registry = CredentialRegistry.create();

    @Test
    void randomInputsNeverThrow() {
        Random random = new Random(SEED);
        for (int i = 0; i < ROUNDS; i++) {
            String input = randomString(random, random.nextInt(97));
            exercise(input);
        }
    }

    @Test
    void fixedEdgeInputsNeverThrow() {
        String[] edges = {"", " ", "   ", "﻿", "测", "P<", "9", "0123456789012345678", "330105197810270025@", null};
        for (String input : edges) {
            exercise(input);
        }
    }

    /**
     * 对单个输入执行全部公开校验/解析/识别入口
     *
     * @param input 证件号码
     */
    private void exercise(String input) {
        registry.detect(input);
        for (CredentialType type : registry.getSupportedTypes()) {
            ValidationResult result = registry.validate(type, input);
            assertThat(result).isNotNull();
            registry.parse(type, input);
            registry.parse(type, input, CredentialInfo.class);
        }
    }

    /**
     * 生成随机字符串
     *
     * @param random 随机源
     * @param length 长度
     * @return 随机字符串
     */
    private static String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
