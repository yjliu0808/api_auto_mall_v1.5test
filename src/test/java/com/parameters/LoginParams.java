package com.parameters;

import com.globaldata.GlobalSaveData;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： 登录接口参数化
 **/
public class LoginParams {
    public static void paramsSetValue(){
        //excel测试数据需要参数化的变量,开始赋值

        GlobalSaveData.put("${username1}","test");
        GlobalSaveData.put("${username1}", "test");

        GlobalSaveData.put("${password1}","123456");
        GlobalSaveData.put("${username2}","testA");
        GlobalSaveData.put("${password2}","123456");
        GlobalSaveData.put("${username3}","test");
        GlobalSaveData.put("${password3}","1234567");
    }

}
