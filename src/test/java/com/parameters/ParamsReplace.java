package com.parameters;

import com.entity.CaseInfo;
import com.globaldata.GlobalSaveData;
import com.loggerutil.BaseLogger;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： 替换 CaseInfo 中的参数占位符，如 ${token}、${userId}
 **/
public class ParamsReplace extends BaseLogger {

    /**
     * 替换 CaseInfo 中的参数占位符
     */
    public static void paramsReplace(CaseInfo caseInfo) {
        if (caseInfo == null) return;

        // 获取待替换字段
        String url = caseInfo.getUrl();
        String params = caseInfo.getParams();
        String expectedResult = caseInfo.getExpectedResult();
        String sql = caseInfo.getSql();

        // 获取替换数据源（一次性）
        Map<String, String> replacementMap = GlobalSaveData.getAll();

        // 打印替换前的内容
        logger.info("参数替换前：" +
                "\nURL: " + url +
                "\nParams: " + params +
                "\nExpectedResult: " + expectedResult +
                "\nSQL: " + sql);

        // 遍历所有需要替换的占位符
        for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
            String key = entry.getKey();       // 例：${token}
            String value = entry.getValue();   // 例：abc123

            url = safeReplace(url, key, value);
            params = safeReplace(params, key, value);
            expectedResult = safeReplace(expectedResult, key, value);
            sql = safeReplace(sql, key, value);
        }

        // 设置回 CaseInfo
        caseInfo.setUrl(url);
        caseInfo.setParams(params);
        caseInfo.setExpectedResult(expectedResult);
        caseInfo.setSql(sql);

        // 打印替换后的内容
        logger.info("参数替换后：" +
                "\nURL: " + url +
                "\nParams: " + params +
                "\nExpectedResult: " + expectedResult +
                "\nSQL: " + sql);
    }

    /**
     * 安全替换方法（防止空指针）
     */
    private static String safeReplace(String source, String target, String replacement) {
        if (StringUtils.isBlank(source)) return source;
        return source.replace(target, replacement);
    }
}
