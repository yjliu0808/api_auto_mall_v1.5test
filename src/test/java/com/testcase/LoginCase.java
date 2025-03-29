package com.testcase;

import com.alibaba.fastjson.JSONPath;
import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;
import com.excelutils.ReadExcel;
import com.globaldata.GlobalSaveData;
import com.httprequest.GetHeaders;
import com.parameters.BaseParams;
import com.parameters.LoginParams;
import com.parameters.ParamsReplace;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Epic("用户模块")
@Feature("登录功能")
public class LoginCase extends BaseCase {
    @Test(description = "登录接口测试", dataProvider = "datas")
    public void testLogin(CaseInfo caseInfo) {
        caseInfoThreadLocal.set(caseInfo);
        //参数化赋值
        LoginParams.paramsSetValue();
        //参数化替换
        ParamsReplace.paramsReplace(caseInfo);
        headersThreadLocal.set(GetHeaders.getDefaultHeaders());
        sqlBeforeThreadLocal.set(SqlUtils.querySingleValue(caseInfo.getSql()));
        logCaseInfo();
        //发起请求
        String response = sendRequest();
        //保存响应结果token
        saveResponseResult(response,"$.data.token=${token}");
        //批量回写到excel
        finishWriteBackAndAssert(caseInfo, response);
    }
    @DataProvider(name = "datas")
    public Object[] datas() throws Exception {
        //1、从excel读取用例信息
        return ReadExcel.readExcel(startSheetIndex, sheetNum).toArray();
    }

}
