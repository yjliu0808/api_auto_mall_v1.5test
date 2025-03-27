package com.caseutils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.entity.CaseInfo;
import com.loggerutil.BaseLogger;

import java.util.Map;
import java.util.Set;

/**
 * @Author： Athena
 * @Date： 2025-03-22
 * @Desc： 对响应结果进行断言校验（支持 JSONPath 解析）
 **/
public class AssertResponseResult extends BaseLogger {

    /**
     * 根据期望结果断言响应体中的关键字段值
     *
     * @param caseInfo        用例对象，包含期望值 JSON
     * @param responseResult  实际响应 JSON 字符串
     * @return 是否断言通过
     */
    public static boolean assertResponseResult(CaseInfo caseInfo, String responseResult) {
        boolean assertResult = true;

        String expectedResultJson = caseInfo.getExpectedResult();
        if (expectedResultJson == null || expectedResultJson.trim().isEmpty()) {
            logger.warn("【警告】用例中未设置期望结果，跳过断言！");
            return true;
        }

        Map<String, Object> expectedMap = JSONObject.parseObject(expectedResultJson, Map.class);

        for (Map.Entry<String, Object> entry : expectedMap.entrySet()) {
            String expectedKey = entry.getKey();
            Object expectedValue = entry.getValue();

            // 从响应中提取实际值
            Object actualValue = JSONPath.read(responseResult, "$." + expectedKey);

            // 断言判断
            if (!expectedValue.equals(actualValue)) {
                logger.info("❌ 断言失败 - 字段：" + expectedKey +
                        "，期望值：" + expectedValue +
                        "，实际值：" + actualValue);
                assertResult = false;
            } else {
                logger.info("✅ 断言通过 - 字段：" + expectedKey +
                        "，值：" + actualValue);
            }
        }

        if (assertResult) {
            logger.info("🎉 所有字段断言通过！");
        }

        return assertResult;
    }
}
