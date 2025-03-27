package com.parameters;

import com.globaldata.GlobalSaveData;
import com.loggerutil.BaseLogger;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： 基本的参数化数据
 **/
public class BaseParams extends BaseLogger {
    public static void paramsSetValue(){
        //excel测试数据需要参数化的变量,开始赋值
        GlobalSaveData.put("${ip}","http://129.28.122.208:8089");
    }
}
