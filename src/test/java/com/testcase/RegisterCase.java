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
import com.parameters.RegisterParams;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Epic("用户模块")
@Feature("注册功能")
public class RegisterCase extends BaseCase {
    @Test(description = "注册接口测试", dataProvider = "datas")
    public void registerLogin(CaseInfo caseInfo) {
        caseInfoThreadLocal.set(caseInfo);
        //参数化赋值
        RegisterParams.paramsSetValue();
        //参数化替换
        ParamsReplace.paramsReplace(caseInfo);
        headersThreadLocal.set(GetHeaders.getLoginHeaders());
        sqlBeforeThreadLocal.set(SqlUtils.querySingleValue(caseInfo.getSql()));
        logCaseInfo();
        //发起请求
        String response = sendRequest();
        //批量回写到excel
        finishWriteBackAndAssert(caseInfo, response);
    }
    @DataProvider(name = "datas")
    public Object[] datas() throws Exception {
        //1、从excel读取用例信息
        return ReadExcel.readExcel(startSheetIndex, sheetNum).toArray();
    }

}
