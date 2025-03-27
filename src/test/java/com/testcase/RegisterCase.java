package com.testcase;

import com.caseutils.TestDataProvider;
import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;
import com.httprequest.GetHeaders;
import com.parameters.BaseParams;
import com.parameters.ParamsReplace;
import com.parameters.RegisterParams;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

/**
 * @Author： Athena
 * @Date： 2025-03-26
 * @Desc： 注册接口测试
 **/
@Epic("用户模块")
@Feature("注册功能")
public class RegisterCase extends BaseCase{
/*
1. 参数准备与替换
参数初始化（比如通用参数、注册特有参数）
参数替换（比如 ${randomEmail}）
2. 日志记录
记录用例详细信息（Allure 步骤）
3. 请求前数据库操作
查询 SQL 执行前的状态（如用户是否已存在）
4. 发送请求并记录响应
发送 HTTP 请求
保存响应结果并写回 Excel
5. 响应断言
断言响应体中是否包含预期信息
6. 数据库断言
查询注册后用户信息，计算差值并断言
7. 最终结果处理
汇总响应断言和 SQL 断言结果
写回 Excel 最终结果
调用 Assert.assertTrue() 确认测试通过
* */
    @Test(description = "注册接口测试", dataProvider = "testDataCaseInfo", dataProviderClass = TestDataProvider.class)

    public void testRegister(CaseInfo caseInfo){
        // 1. 参数初始化 & 替换（⚠️ 替换后 SQL 才能执行）
        BaseParams.paramsSetValue();
        RegisterParams.paramsSetValue();
        ParamsReplace.paramsReplace(caseInfo);
        // 3. 请求头准备
        headersThreadLocal.set(GetHeaders.getLoginHeaders());
        // 4. 执行 SQL 请求前查询（必须放在替换参数之后）
        sqlBeforeThreadLocal.set(SqlUtils.querySingleValue(caseInfo.getSql()));
        // 5. 记录用例信息（Allure）
        logCaseInfo();
        // 6. 发请求
        String response = sendRequest();
        // 7. 响应断言 + SQL 断言 + 写回 Excel
       // finishWriteBackAndAssert(caseInfo, response);
    }
}
