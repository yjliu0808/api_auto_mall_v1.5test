package com.httprequest;

import com.globaldata.GlobalSaveData;
import com.loggerutil.BaseLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author： Athena
 * @Date： 2025-03-20
 * @Desc： 构建请求头工具类
 **/
public class GetHeaders extends BaseLogger {

    /**
     * 获取默认请求头
     */
    public static Map<String, String> getDefaultHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        return headersMap;
    }
    /**
     * 获取登录请求头
     */
    public static Map<String, String> getLoginHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        String token = GlobalSaveData.get("${token}");
        headersMap.put("authorization", "Bearer "+token);
        return headersMap;
    }


    /**
     * 在默认请求头基础上添加一个额外的请求头
     */
    public static Map<String, String> getHeadersWith(String key, String value) {
        Map<String, String> headersMap = getDefaultHeaders();
        headersMap.put(key, value);
        return headersMap;
    }

    /**
     * 在默认请求头基础上添加多个额外请求头
     */
    public static Map<String, String> getHeadersWith(Map<String, String> additionalHeaders) {
        Map<String, String> headersMap = getDefaultHeaders();
        if (additionalHeaders != null) {
            headersMap.putAll(additionalHeaders);
        }
        return headersMap;
    }
}
