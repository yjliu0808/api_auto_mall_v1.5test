package com.testcase;
import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;
import com.excelutils.ReadExcel;
import com.globaldata.GlobalSaveData;
import com.httprequest.GetHeaders;
import com.parameters.AddRoleParams;
import com.parameters.LoginParams;
import com.parameters.RegisterParams;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
/**
 * @Author： Athena
 * @Date： 2025-04-08
 * @Desc： 添加用户角色
 **/

public class AddRoleCase extends BaseCase{
    /**
     * 添加用户角色接口测试方法，使用统一执行流程
     */
    @Epic("角色模块")
    @Feature("添加用户角色功能")
    @Story("输入有效角色信息后成功添加")
    @Owner("Athena")
    @Severity(SeverityLevel.CRITICAL)
    @Description("验证添加用户角色的接口是否正常")
    @Test(description = "添加用户角色接口测试", dataProvider = "datas")
    public void testRegister(CaseInfo caseInfo) {
        executeTestCase(caseInfo, AddRoleParams::paramsSetValue);
    }

    /**
     * 数据提供者：读取 Excel 注册用例
     */
    @DataProvider(name = "datas")
    public Object[] datas() throws Exception {
        return ReadExcel.readExcel(startSheetIndex, sheetNum).toArray();
    }

    // 不重写 extractResponseVars()，表示该接口不提取响应字段
}


