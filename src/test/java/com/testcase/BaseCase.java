// ===========================================
// BaseCase.java - 所有接口测试用例的统一父类
// 提供统一测试流程封装、日志记录、断言、参数替换、响应提取、SQL 校验等功能
// ===========================================

package com.testcase;

import com.alibaba.fastjson.JSONPath;
import com.caseutils.AssertResponseResult;
import com.constants.ExcelConstants;
import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;
import com.excelutils.BatchWriteToExcel;
import com.globaldata.GlobalSaveData;
import com.httprequest.GetHeaders;
import com.httprequest.HttpRequest;
import com.loggerutil.BaseLogger;
import com.parameters.BaseParams;
import com.parameters.ParamsReplace;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

public class BaseCase extends BaseLogger {
    public int startSheetIndex;
    public int sheetNum;
    private long suiteStartTime;

    protected static final ThreadLocal<CaseInfo> caseInfoThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Map<String, String>> headersThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<String> responseThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> assertResponseResultThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> assertSqlResultThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Object> sqlBeforeThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Object> sqlAfterThreadLocal = new ThreadLocal<>();

    @BeforeSuite
    public void setup() {
        suiteStartTime = System.currentTimeMillis();
        logger.info("\n📢 ==================【自动化测试开始】==================");
        logger.info("📂 Excel路径: " + ExcelConstants.excelCasePath);
        BaseParams.paramsSetValue();
    }

    @BeforeClass
    @Parameters({"startSheetIndex", "sheetNum"})
    public void beforeClass(int startSheetIndex, int sheetNum) {
        this.startSheetIndex = startSheetIndex;
        this.sheetNum = sheetNum;
    }

    @BeforeMethod
    public void beforeMethod(Object[] data) {
        if (data != null && data.length > 0 && data[0] instanceof CaseInfo) {
            CaseInfo caseInfo = (CaseInfo) data[0];
            caseInfoThreadLocal.set(caseInfo);
            logger.info("\n🧪【接口模块】：" + nullToEmpty(caseInfo.getInterfaceName()) +
                    "\n🔢【用例编号】：" + caseInfo.getCaseId() +
                    "\n📝【用例描述】：" + nullToEmpty(caseInfo.getCaseDesc()) +
                    "\n🌐【接口地址】：" + nullToEmpty(caseInfo.getUrl()) +
                    "\n--------------------------------------------------------");
        }
    }

    @AfterMethod
    public void afterMethod() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        if (caseInfo != null) {
            logger.info("\n✅【用例执行完毕】\n📌 模块：" + nullToEmpty(caseInfo.getInterfaceName()) +
                    " ｜ 用例ID：" + caseInfo.getCaseId() +
                    " ｜ 描述：" + nullToEmpty(caseInfo.getCaseDesc()) +
                    "\n========================================================");
        }
        caseInfoThreadLocal.remove();
        headersThreadLocal.remove();
        responseThreadLocal.remove();
        assertResponseResultThreadLocal.remove();
        assertSqlResultThreadLocal.remove();
        sqlBeforeThreadLocal.remove();
        sqlAfterThreadLocal.remove();
    }

    @AfterSuite
    public void tearDown() {
        long duration = System.currentTimeMillis() - suiteStartTime;
        logger.info("\n====================【测试结束】====================");
        BatchWriteToExcel.batchWriteToExcel(ExcelConstants.excelCasePath, true);
        logger.info("所有测试数据已成功写回 Excel。\n");
        logger.info(String.format("本次测试总耗时：%.2f 秒", duration / 1000.0));
    }

    protected void executeTestCase(CaseInfo caseInfo, Runnable paramInitLogic) {
        caseInfoThreadLocal.set(caseInfo);
        paramInitLogic.run();
        ParamsReplace.paramsReplace(caseInfo);
        headersThreadLocal.set(GetHeaders.getLoginHeaders());
        sqlBeforeThreadLocal.set(SqlUtils.querySingleValue(caseInfo.getSql()));
        logCaseInfo();
        String response = sendRequest();
        extractResponseVars(response);
        finishWriteBackAndAssert(caseInfo, response);
    }

    protected void extractResponseVars(String response) {}

    public static void saveResponseResult(String response, String extractConfig) {
        if (extractConfig == null || extractConfig.trim().isEmpty()) {
            logger.info("未配置提取表达式，跳过提取变量");
            return;
        }
        String[] extracts = extractConfig.split(";");
        for (String extract : extracts) {
            if (!extract.contains("=")) continue;
            String[] parts = extract.split("=", 2);
            String jsonPath = parts[0].trim();
            String saveKey = parts[1].trim();
            try {
                Object value = JSONPath.read(response, jsonPath);
                if (value != null) {
                    GlobalSaveData.put(saveKey, value.toString());
                    logger.info("已提取变量：" + saveKey + " = " + value);
                } else {
                    logger.warn("未提取到值，jsonPath=" + jsonPath);
                }
            } catch (Exception e) {
                logger.error("提取变量异常，jsonPath=" + jsonPath + "，配置=" + extract, e);
            }
        }
    }

    @Step("【用例信息】")
    protected void logCaseInfo() {
        attachCaseInfo(caseInfoThreadLocal.get());
    }

    @Attachment(value = "用例信息明细", type = "text/plain")
    protected String attachCaseInfo(CaseInfo caseInfo) {
        return String.format(
                "用例编号：%d\n用例描述：%s\n请求URL：%s\n请求参数类型：%s\n请求方式：%s\n请求参数：%s\n期望结果：%s\nSQL：%s\n预期SQL差值：%s",
                caseInfo.getCaseId(),
                nullToEmpty(caseInfo.getCaseDesc()),
                nullToEmpty(caseInfo.getUrl()),
                nullToEmpty(caseInfo.getContentType()),
                nullToEmpty(caseInfo.getType()),
                nullToEmpty(caseInfo.getParams()),
                nullToEmpty(caseInfo.getExpectedResult()),
                nullToEmpty(caseInfo.getSql()),
                caseInfo.getExpectedSqlDiff() == null ? "未设置" : caseInfo.getExpectedSqlDiff()
        );
    }

    @Step("【发起请求】")
    protected String sendRequest() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        Map<String, String> headers = headersThreadLocal.get();
        attachRequestDetail(caseInfo.getUrl(), caseInfo.getType(), headers, caseInfo.getParams());
        logger.info("➡️ 请求参数: " + caseInfo.getParams());
        String response = HttpRequest.httpRequest(headers, caseInfo.getUrl(), caseInfo.getParams(), caseInfo.getType());
        logger.info("⬅️ 响应结果: " + response);
        return response;
    }

    @Attachment(value = "请求信息", type = "text/plain")
    protected String attachRequestDetail(String url, String type, Map<String, String> headers, String params) {
        return String.format("请求地址：%s\n请求方式：%s\n请求头：%s\n请求参数：%s",
                url, nullToEmpty(type).toUpperCase(), headers != null ? headers.toString() : "{}", nullToEmpty(params));
    }

    @Step("【响应断言】")
    protected void assertResponseResult() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        String response = responseThreadLocal.get();
        boolean result = AssertResponseResult.assertResponseResult(caseInfo, response);
        assertResponseResultThreadLocal.set(result);
        attachResponseAssertion(caseInfo.getExpectedResult(), response, result);
        logger.info("\n✅ 响应断言结果：\n【期望JSON】" + caseInfo.getExpectedResult() +
                "\n【实际响应】" + response +
                "\n" + (result ? "🎉 响应断言通过！" : "❌ 响应断言失败！"));
    }

    @Attachment(value = "响应断言信息", type = "text/plain")
    protected String attachResponseAssertion(String expected, String actual, boolean result) {
        return String.format("期望结果：\n%s\n\n实际结果：\n%s\n\n断言结果：%s",
                nullToEmpty(expected), nullToEmpty(actual), result ? "✅ 通过" : "❌ 失败");
    }

    @Step("【数据库断言】")
    protected void assertSqlResult() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        if (caseInfo.getExpectedSqlDiff() == null) {
            logger.info("未配置预期 SQL 差值，跳过数据库断言");
            assertSqlResultThreadLocal.set(true);
            attachSqlAssertion("未配置 SQL 差值", null, null, 0, 0, true);
            return;
        }
        String sql = caseInfo.getSql();
        if (sql == null || sql.trim().isEmpty()) {
            logger.warn("SQL 为空，跳过数据库断言");
            assertSqlResultThreadLocal.set(true);
            attachSqlAssertion("SQL 为空", null, null, 0, 0, true);
            return;
        }
        Object before = sqlBeforeThreadLocal.get();
        Object after = sqlAfterThreadLocal.get();
        if (before == null || after == null) {
            logger.warn("SQL 断言跳过，查询结果为空（before=" + before + ", after=" + after + ")");
            assertSqlResultThreadLocal.set(true);
            attachSqlAssertion(sql, before, after, 0, 0, true);
            return;
        }
        int expectedDiff = caseInfo.getExpectedSqlDiff();
        int actualDiff = (int) ((Long) after - (Long) before);
        boolean result = actualDiff == expectedDiff;
        assertSqlResultThreadLocal.set(result);
        attachSqlAssertion(sql, before, after, expectedDiff, actualDiff, result);
        logger.info("\n🧾 SQL断言：\n- 执行语句：" + sql +
                "\n- 请求前：" + before + "，请求后：" + after +
                "\n- 差值预期：" + expectedDiff + "，实际：" + actualDiff + " " + (result ? "✅" : "❌"));
    }

    @Attachment(value = "数据库断言信息", type = "text/plain")
    protected String attachSqlAssertion(String sql, Object before, Object after, int expected, int actual, boolean result) {
        return String.format("执行 SQL：%s\n请求前：%s\n请求后：%s\n预期差值：%d\n实际差值：%d\n断言结果：%s",
                sql, before, after, expected, actual, result ? "✅ 通过" : "❌ 失败");
    }

    @Step("【最终测试结果】")
    protected void logFinalResult() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        boolean responsePass = assertResponseResultThreadLocal.get() != null && assertResponseResultThreadLocal.get();
        boolean sqlPass = assertSqlResultThreadLocal.get() != null && assertSqlResultThreadLocal.get();
        boolean finalResult = responsePass && sqlPass;
        String resultText = finalResult ? "Pass" : "Fail";
        BatchWriteToExcel.addWriteBackData(caseInfo.getCaseId(), ExcelConstants.ASSERT_SQL_RESULT_COLUMN_INDEX, startSheetIndex, resultText);
        attachFinalResult(caseInfo.getCaseId(), responsePass, sqlPass, resultText);
        Assert.assertTrue(finalResult, "断言失败：响应断言=" + responsePass + "，SQL 断言=" + sqlPass);
    }

    @Attachment(value = "最终结果信息", type = "text/plain")
    protected String attachFinalResult(int caseId, boolean response, boolean sql, String result) {
        return String.format("用例编号：%d\n响应断言：%s\n数据库断言：%s\n最终结果：%s",
                caseId, response ? "✅ 通过" : "❌ 失败", sql ? "✅ 通过" : "❌ 失败", result);
    }

    protected void finishWriteBackAndAssert(CaseInfo caseInfo, String response) {
        responseThreadLocal.set(response);
        BatchWriteToExcel.addWriteBackData(caseInfo.getCaseId(), ExcelConstants.RESPONSE_RESULT_COLUMN_INDEX, startSheetIndex, response);
        assertResponseResult();
        Object sqlAfter = SqlUtils.querySingleValue(caseInfo.getSql());
        sqlAfterThreadLocal.set(sqlAfter);
        assertSqlResult();
        logFinalResult();
    }

    protected String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
