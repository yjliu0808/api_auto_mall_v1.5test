package com.httprequest;

import com.loggerutil.BaseLogger;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @Author： Athena
 * @Date： 2025-03-18
 * @Desc： 封装统一 HTTP 请求工具
 **/
public class HttpRequest extends BaseLogger {

    /**
     * 发起 HTTP 请求
     *
     * @param headers 请求头
     * @param url     请求地址
     * @param params  请求体内容（JSON）
     * @param type    请求方法：get/post/put/patch/delete
     * @return 响应体字符串
     */
    public static String httpRequest(Map<String, String> headers, String url, String params, String type) {
        checkParamsNotNull(headers, url, params, type);

        RequestSpecification requestSpec = given().headers(headers).contentType(ContentType.JSON);
        String responseBody;

        try {
            switch (type.toLowerCase()) {
                case "get":
                   // logger.info("【GET 请求】URL: " + url);
                    responseBody = requestSpec.get(url).asString();
                    break;
                case "post":
                   // logger.info("【POST 请求】URL: " + url + "，Params: " + params);
                    responseBody = requestSpec.body(params).post(url).asString();
                    break;
                case "patch":
                   // logger.info("【PATCH 请求】URL: " + url + "，Params: " + params);
                    responseBody = requestSpec.body(params).patch(url).asString();
                    break;
                case "put":
                   // logger.info("【PUT 请求】URL: " + url + "，Params: " + params);
                    responseBody = requestSpec.body(params).put(url).asString();
                    break;
                case "delete":
                   // logger.info("【DELETE 请求】URL: " + url + "，Params: " + params);
                    responseBody = requestSpec.body(params).delete(url).asString();
                    break;
                default:
                    throw new IllegalArgumentException("不支持的请求类型: " + type);
            }

          //  logger.info("【响应结果】：\n" + responseBody);
            return responseBody;

        } catch (Exception e) {
            logger.error("HTTP 请求发送失败: " + url, e);
            throw new RuntimeException("HTTP 请求发送失败", e);
        }
    }

    /**
     * 参数非空校验
     */
    private static void checkParamsNotNull(Map<String, String> headers, String url, String params, String type) {
        if (headers == null) {
            throw new IllegalArgumentException("请求头 headers 不能为空");
        }
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("请求地址 url 不能为空");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("请求类型 type 不能为空");
        }
        if (!"get".equalsIgnoreCase(type) && (params == null || params.trim().isEmpty())) {
            throw new IllegalArgumentException("请求体参数 params 不能为空（非 GET 请求）");
        }
    }
}
