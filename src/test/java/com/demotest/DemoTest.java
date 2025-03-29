package com.demotest;

import com.excelutils.ReadExcel;
import com.entity.CaseInfo;


import java.util.List;

/**
 * @Author： Athena
 * @Date： 2025-03-17
 * @Desc： 单元测试方法
 **/
public class DemoTest {
    public static void main(String[] args) {
        //测试数据是否连接成功
        /*  DatabaseConnection.databaseConnection("/database.properties");*/
        //测试发送http请求
       /* Map<String,String> map = new HashMap<>();
        map.put("content-type","application/json");
        HttpRequest.httpRequest
                (map,"http://129.28.122.208:8089/admin/login","{\"username\":\"athena\",\"password\":\"123456\"}","post");
        */
        //测试读取properties配置文件
        //Properties properties = ReadProperties.readProperties("/excel.properties");
        //测试读取excel文件数据

    }


}
