package com.testcase;


import com.caseutils.TestDataProvider;

import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;

import com.globaldata.GlobalSaveData;
import com.httprequest.GetHeaders;

import com.parameters.BaseParams;
import com.parameters.LoginParams;
import com.parameters.ParamsReplace;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONPath;
import org.testng.log4testng.Logger;

@Epic("用户模块")
@Feature("登录功能")
public class LoginCase extends BaseCase {

    @Test(description = "登录接口测试", dataProvider = "testDataCaseInfo", dataProviderClass = TestDataProvider.class)
    public void testLogin(CaseInfo caseInfo) {
        // 1. 设置上下文（必须第一步）
        caseInfoThreadLocal.set(caseInfo);
        // 2. 参数初始化 & 替换（⚠️ 替换后 SQL 才能执行）
        BaseParams.paramsSetValue();
        LoginParams.paramsSetValue();
        ParamsReplace.paramsReplace(caseInfo);
        // 3. 请求头准备
        headersThreadLocal.set(GetHeaders.getDefaultHeaders());
        // 4. 执行 SQL 请求前查询（必须放在替换参数之后）
        sqlBeforeThreadLocal.set(SqlUtils.querySingleValue(caseInfo.getSql()));
        // 5. 记录用例信息（Allure）
        logCaseInfo();
        // 6. 发请求
        String response = sendRequest();
        ////存储响应结果 token
        saveResponseResult(response);
        // 7. 响应断言 + SQL 断言 + 写回 Excel
        finishWriteBackAndAssert(caseInfo, response);
    }
    /*
     * @ Description //存储响应结果
     * @ Param[]
     * @ return void
     **/
    public void saveResponseResult(String response){
        //存储响应结果token到saveResultMap
        String token = (String) JSONPath.read(response, "$.data.token");
        Integer code = (Integer) JSONPath.read(response, "$.code");
        if (code != null && code == 200 && token != null) {
            GlobalSaveData.put("${token}", token );
            logger.info("登录成功，token 已保存："+ token);
        } else {
            logger.warn("登录失败，未提取 token。响应为："+response);
        }
    }
}
