package com.parameters;

import com.globaldata.GlobalSaveData;

/**
 * @Author： Athena
 * @Date： 2025-04-08
 * @Desc： 添加菜单参数化
 **/
public class AddMenuParams {
    public static void paramsSetValue(){
        GlobalSaveData.put("${title}", "自动化测试菜单-"+com.utils.GeneralUtils.generateRandomUsername());
        GlobalSaveData.put("${parentId}","0");
        GlobalSaveData.put("${name}", "自动化测试前端名称-"+ com.utils.GeneralUtils.generateRandomUsername());
        GlobalSaveData.put("${icon}", com.utils.GeneralUtils.getRandomModule());
        GlobalSaveData.put("${hidden}", "0");
        GlobalSaveData.put("${sort}", "0");

    }
}
