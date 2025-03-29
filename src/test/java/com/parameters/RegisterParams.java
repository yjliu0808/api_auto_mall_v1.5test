package com.parameters;

import com.globaldata.GlobalSaveData;
import com.utils.GeneralUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author： Athena
 * @Date： 2025-03-26
 * @Desc： 注册类参数化
 **/
public class RegisterParams {
    public static void paramsSetValue(){
        //excel测试数据需要参数化的变量,开始赋值
        GlobalSaveData.put("${username}", GeneralUtils.generateRandomUsername());
        GlobalSaveData.put("${password}", "123456");
        GlobalSaveData.put("${nickName}",GeneralUtils.generateRandomNickName() );
        GlobalSaveData.put("${note}", "自动化测试注册");
        GlobalSaveData.put("${email}", GeneralUtils.generateRandomEmail());
        GlobalSaveData.put("${status}","1");
       }
}
