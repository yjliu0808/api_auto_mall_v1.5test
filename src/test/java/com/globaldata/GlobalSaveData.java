package com.globaldata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： 全局存储响应结果及参数化参数的数据类
 **/
public class   GlobalSaveData {

    // 线程安全的全局数据容器
    private static final Map<String, String> saveResultMap = new ConcurrentHashMap<>();

    /**
     * 存数据
     */
    public static void put(String key, String value) {
        saveResultMap.put(key, value);
    }

    /**
     * 取数据
     */
    public static String get(String key) {
        return saveResultMap.get(key);
    }

    /**
     * 删除某项
     */
    public static void remove(String key) {
        saveResultMap.remove(key);
    }

    /**
     * 清空所有数据
     */
    public static void clear() {
        saveResultMap.clear();
    }

    /**
     * 判断 key 是否存在
     */
    public static boolean containsKey(String key) {
        return saveResultMap.containsKey(key);
    }

    /**
     * 获取只读副本（避免外部篡改原始数据）
     */
    public static Map<String, String> getAll() {
        return new ConcurrentHashMap<>(saveResultMap);
    }
}
