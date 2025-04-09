package com.parameters;

import com.globaldata.GlobalSaveData;

/**
 * @Author： Athena
 * @Date： 2025-04-08
 * @Desc： 添加用户角色的参数化
 **/
public class AddRoleParams {
    public static void paramsSetValue(){
        GlobalSaveData.put("${name}", "自动化测试-"+com.utils.GeneralUtils.generateRandomUsername());
        GlobalSaveData.put("${description}","自动化测试-"+com.utils.GeneralUtils.generateRandomUsername());
        GlobalSaveData.put("${adminCount}", "0");
        GlobalSaveData.put("${status}", "0");

    }
}
