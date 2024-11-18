package com.tellhow.example.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/11/12 08:40 yanfei 新建
 * @since JDK1.8
 */
public class Main {
    public static void main(String[] args) {
        Map<String, Map<String, String>> map = new HashMap<>();
        map.computeIfPresent("aaa", (key, value) -> map.remove("aaa"));
        map.computeIfAbsent("aaa", s -> new HashMap<String, String>()).put("0", "aa");
    }
}
