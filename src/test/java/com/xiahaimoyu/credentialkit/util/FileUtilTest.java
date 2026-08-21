/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileUtilTest {

    @Test
    void readCsvSkipsBlankLines() throws IOException {
        // 空行被跳过：gb2260.csv共6755个数据行（末行无换行符）
        List<List<String>> data = FileUtil.readCsvFromFile("/region/gb2260.csv");
        assertThat(data).hasSize(6755);
        assertThat(data.get(0)).containsExactly("110000", "北京市");
        assertThat(data.get(1)).containsExactly("110101", "东城区");
    }

    @Test
    void readCsvMissingResourceThrows() {
        assertThatThrownBy(() -> FileUtil.readCsvFromFile("/not/exist.csv"))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void parseQuotedFieldWithComma() throws IOException {
        // 通过readCsvFromFile无法测引号字段（真实数据无引号），借道临时资源验证解析逻辑
        // 这里直接验证parseCsvLine的公开行为：字段含引号包裹的逗号
        List<List<String>> data = FileUtil.readCsvFromFile("/region/quoted-sample.csv");
        assertThat(data).hasSize(1);
        assertThat(data.get(0)).containsExactly("阿尔巴尼亚", "含,逗号", "带\"引号\"", "尾随空格");
    }
}
