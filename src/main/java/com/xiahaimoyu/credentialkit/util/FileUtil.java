/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具
 *
 * @author Howard.Li
 */
public class FileUtil {

    /**
     * 读取csv文件
     *
     * @param resourcePath 资源路径
     * @return 数据
     * @throws IOException 异常
     */
    public static List<List<String>> readCsvFromFile(String resourcePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (InputStream is = FileUtil.class.getResourceAsStream(resourcePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                data.add(parseCsvLine(line));
            }
        } catch (IOException e) {
            throw e;
        }
        return data;
    }

    /**
     * 解析csv行
     *
     * @param line 行数据
     * @return 解析结果
     */
    private static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        if (line == null || line.isEmpty()) {
            return values;
        }
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (inQuotes) {
                if (ch == '"') {
                    if (i + 1 < chars.length && chars[i + 1] == '"') {
                        currentValue.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    currentValue.append(ch);
                }
            } else {
                if (ch == ',') {
                    values.add(currentValue.toString().trim());
                    currentValue.setLength(0);
                } else if (ch == '"' && currentValue.length() == 0) {
                    inQuotes = true;
                } else {
                    currentValue.append(ch);
                }
            }
        }
        values.add(currentValue.toString().trim());
        return values;
    }
}
