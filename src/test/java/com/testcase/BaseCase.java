package com.testcase;

import com.caseutils.AssertResponseResult;
import com.constants.ExcelConstants;
import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;
import com.excelutils.BatchWriteToExcel;
import com.excelutils.GetExcelPath;
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
    //sheet开始索引
    public int startSheetIndex;
    //读取sheet个数
    public int sheetNum;

    // ===== ThreadLocal 上下文存储区域 =====
    protected static final ThreadLocal<CaseInfo> caseInfoThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Map<String, String>> headersThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<String> responseThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> assertResponseResultThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> assertSqlResultThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Object> sqlBeforeThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Object> sqlAfterThreadLocal = new ThreadLocal<>();
    //@BeforeSuite全局初始化一次（参数配置、日志入口）
    @BeforeSuite
    public void setup() {
        logger.info("=====================自动化开始=========================");
        //基础数据：ip参数化赋值
        BaseParams.paramsSetValue();
    }
    //@BeforeClass每个类读取自己的参数
    @BeforeClass
    @Parameters({"startSheetIndex","sheetNum"})
    public void beforeClass(int startSheetIndex,int sheetNum) {
        //接受testng.xml中parameters 参数
        this.startSheetIndex = startSheetIndex;
        this.sheetNum = sheetNum;
    }
    //每个测试用例执行完后，日志中会打印一行分隔符
    @AfterMethod
    public void afterMethod() {
        logger.info("=====================一条用例结束=========================================");
    }
    @AfterSuite
    public void tearDown() throws Exception {
        //批量回写
        logger.info("====测试结束====");
        BatchWriteToExcel.batchWriteToExcel(ExcelConstants.excelCasePath);
    }
    // ===== 自动设置和清理 ThreadLocal =====
    @BeforeMethod
    public void beforeMethod(Object[] data) {
        if (data != null && data.length > 0 && data[0] instanceof CaseInfo) {
            caseInfoThreadLocal.set((CaseInfo) data[0]);
        }
    }

    @AfterMethod
    public void clearThreadLocal() {
        caseInfoThreadLocal.remove();
        headersThreadLocal.remove();
        responseThreadLocal.remove();
        assertResponseResultThreadLocal.remove();
        assertSqlResultThreadLocal.remove();
        sqlBeforeThreadLocal.remove();
        sqlAfterThreadLocal.remove();
    }

    // ===== 用例信息 Allure 附件 =====
    @Step("【用例信息】")
    protected void logCaseInfo() {
        attachCaseInfo(caseInfoThreadLocal.get());
    }

    @Attachment(value = "用例信息明细", type = "text/plain")
    protected String attachCaseInfo(CaseInfo caseInfo) {
        return String.format(
                "用例编号：%d\n" +
                        "用例描述：%s\n" +
                        "请求URL：%s\n" +
                        "请求参数类型：%s\n" +
                        "请求方式：%s\n" +
                        "请求参数：%s\n" +
                        "期望结果：%s\n" +
                        "SQL：%s\n" +
                        "预期SQL差值：%s",
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

    // ===== 发送请求并记录请求信息 =====
    @Step("【发起请求】")
    protected String sendRequest() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        Map<String, String> headers = headersThreadLocal.get();

        // ✅ 打印请求头日志
        System.out.println("【调试日志】请求头：" + (headers != null ? headers.toString() : "无"));

        // ✅ 打印URL、方式、参数也可以一起加上，便于完整追踪
        System.out.println("【调试日志】请求URL：" + caseInfo.getUrl());
        System.out.println("【调试日志】请求方式：" + caseInfo.getType());
        System.out.println("【调试日志】请求参数：" + caseInfo.getParams());

        // Allure 附件记录（你已集成）
        attachRequestDetail(caseInfo.getUrl(), caseInfo.getType(), headers, caseInfo.getParams());

        return HttpRequest.httpRequest(headers, caseInfo.getUrl(), caseInfo.getParams(), caseInfo.getType());
    }


    @Attachment(value = "请求信息", type = "text/plain")
    protected String attachRequestDetail(String url, String type, Map<String, String> headers, String params) {
        return String.format("请求地址：%s\n请求方式：%s\n请求头：%s\n请求参数：%s",
                url,
                nullToEmpty(type).toUpperCase(),
                headers != null ? headers.toString() : "{}",
                nullToEmpty(params));
    }

    // ===== 响应断言 =====
    @Step("【响应断言】")
    protected void assertResponseResult() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        String response = responseThreadLocal.get();
        boolean result = AssertResponseResult.assertResponseResult(caseInfo, response);
        assertResponseResultThreadLocal.set(result);
        attachResponseAssertion(caseInfo.getExpectedResult(), response, result);
    }

    @Attachment(value = "响应断言信息", type = "text/plain")
    protected String attachResponseAssertion(String expected, String actual, boolean result) {
        return String.format("期望结果：\n%s\n\n实际结果：\n%s\n\n断言结果：%s",
                nullToEmpty(expected),
                nullToEmpty(actual),
                result ? "✅ 通过" : "❌ 失败");
    }

    // ===== 数据库断言 =====
    @Step("【数据库断言】")
    protected void assertSqlResult() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();
        String sql = caseInfo.getSql();

        if (sql == null || sql.trim().isEmpty()) {
            logger.warn("SQL 为空，跳过数据库断言");
            assertSqlResultThreadLocal.set(true); // 默认通过，避免中断
            // 🧩 添加 Allure 附件说明：为什么跳过
            attachSqlAssertion("未设置 SQL，跳过数据库断言", null, null, caseInfo.getExpectedSqlDiff(), caseInfo.getExpectedSqlDiff(), true);
            return;
        }

        Object before = sqlBeforeThreadLocal.get();
        Object after = sqlAfterThreadLocal.get();

        if (before == null || after == null) {
            logger.warn("SQL 断言跳过，查询结果为空（before=" + before + ", after=" + after + ")");
            assertSqlResultThreadLocal.set(true); // 默认通过，避免中断
            // 🧩 添加 Allure 附件说明：查询结果为空
            attachSqlAssertion(sql, before, after, caseInfo.getExpectedSqlDiff(), caseInfo.getExpectedSqlDiff(), true);
            return;
        }

        int expectedDiff = caseInfo.getExpectedSqlDiff();
        int actualDiff = (int) ((Long) after - (Long) before);
        boolean result = actualDiff == expectedDiff;

        assertSqlResultThreadLocal.set(result);
        attachSqlAssertion(sql, before, after, expectedDiff, actualDiff, result);
    }


    @Attachment(value = "数据库断言信息", type = "text/plain")
    protected String attachSqlAssertion(String sql, Object before, Object after, int expected, int actual, boolean result) {
        return String.format("执行 SQL：%s\n请求前：%s\n请求后：%s\n预期差值：%d\n实际差值：%d\n断言结果：%s",
                sql, before, after, expected, actual, result ? "✅ 通过" : "❌ 失败");
    }

    // ===== 最终断言汇总 =====
    @Step("【最终测试结果】")
    protected void logFinalResult() {
        CaseInfo caseInfo = caseInfoThreadLocal.get();

        boolean responsePass = assertResponseResultThreadLocal.get() != null && assertResponseResultThreadLocal.get();
        boolean sqlPass = assertSqlResultThreadLocal.get() != null && assertSqlResultThreadLocal.get();

        boolean finalResult = responsePass && sqlPass;
        String resultText = finalResult ? "Pass" : "Fail";

        BatchWriteToExcel.addWriteBackData(caseInfo.getCaseId(), ExcelConstants.ASSERT_SQL_RESULT_COLUMN_INDEX,
               startSheetIndex, resultText);

        attachFinalResult(caseInfo.getCaseId(), responsePass, sqlPass, resultText);

        Assert.assertTrue(finalResult, "断言失败：响应断言=" + responsePass + "，SQL 断言=" + sqlPass);
    }

    @Attachment(value = "最终结果信息", type = "text/plain")
    protected String attachFinalResult(int caseId, boolean response, boolean sql, String result) {
        return String.format("用例编号：%d\n响应断言：%s\n数据库断言：%s\n最终结果：%s",
                caseId,
                response ? "✅ 通过" : "❌ 失败",
                sql ? "✅ 通过" : "❌ 失败",
                result);
    }

    // ===== 公共写回与断言入口 =====
    protected void finishWriteBackAndAssert(CaseInfo caseInfo, String response) {
        System.out.println("\n==========【开始执行断言与写回】==========");
        System.out.println("【用例ID】" + caseInfo.getCaseId());
        System.out.println("【用例描述】" + caseInfo.getCaseDesc());
        System.out.println("【请求URL】" + caseInfo.getUrl());
        System.out.println("【请求方式】" + caseInfo.getType());
        System.out.println("【请求参数】" + caseInfo.getParams());
        System.out.println("【期望结果】" + caseInfo.getExpectedResult());
        System.out.println("【实际响应】" + response);
        System.out.println("=======================================");

        // 保存响应内容
        responseThreadLocal.set(response);

        // 写回响应内容到 Excel
        BatchWriteToExcel.addWriteBackData(
                caseInfo.getCaseId(),
                ExcelConstants.RESPONSE_RESULT_COLUMN_INDEX,
                startSheetIndex,
                response
        );

        // 响应断言
        assertResponseResult();

        // 查询 SQL 请求后的值（用于断言）
        Object sqlAfter = SqlUtils.querySingleValue(caseInfo.getSql());
        sqlAfterThreadLocal.set(sqlAfter);

        // 数据库断言
        assertSqlResult();

        // 汇总断言并写入最终结果
        logFinalResult();

        // 批量写入所有回写数据
        BatchWriteToExcel.batchWriteToExcel(ExcelConstants.excelCasePath);

        System.out.println("==========【断言与写回结束】用例ID: " + caseInfo.getCaseId() + " ==========\n");
    }



    // ===== 工具方法 =====
    protected String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
