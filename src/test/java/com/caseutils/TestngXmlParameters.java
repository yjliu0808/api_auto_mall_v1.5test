package com.caseutils;

import com.loggerutil.BaseLogger;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Objects;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： testbg.xml中parameter标签 通过testng的注解  @Parameters读取数据
 **/
public class TestngXmlParameters extends BaseLogger {
    public static String propertiesFileName;
    public static String propertiesFileParamsName;
    public static int startSheetIndex;
    public static int  sheetNum;
    /*注意： @Parameters 需要和 @BeforeTest, @BeforeClass, @BeforeMethod 之类的 TestNG 生命周期方法配合使用，
     否则不会被 TestNG 自动调用。*/
    //同一个测试类中的所有 @Test 方法共享 @BeforeClass 方法。如果当前类只有 @BeforeClass 而没有 @Test 方法，那么 @BeforeClass 不会被执行
    @BeforeClass
    //这里是在类执行之前先运行：读取testbg.xml中parameter标签的value值
    //propertiesFileName是excel的配置文件名称（前缀加/）
    //propertiesFileParamsName是excel的配置文件里面的key值
    //startSheetIndex是读取excel中测试用例时，需要从哪个sheet开始的值
    //sheetNum是读取excel中测试用例时，每次读取几个sheet的值
    @Parameters({"propertiesFileName","propertiesFileParamsName","startSheetIndex","sheetNum"})
    public void setParameters(String propertiesFileName,String propertiesFileParamsName,int startSheetIndex,int  sheetNum){
        logger.info("============自动化测试开始============");
        logger.info("开始读取testng.xml中parameter标签的value值 ");
        //这里是需要将读取到的数据进去全局变量赋值，方便其他类调用
        this.propertiesFileName = propertiesFileName;
        this.propertiesFileParamsName = propertiesFileParamsName;
        this.startSheetIndex = startSheetIndex;
        this.sheetNum = sheetNum;
        //判断所有参数都不为空，已经通过@Parameters获取数据
        if (Objects.nonNull(propertiesFileName) && !propertiesFileName.isEmpty() &&
                Objects.nonNull(propertiesFileParamsName) && !propertiesFileParamsName.isEmpty() &&
                startSheetIndex >= 0 && sheetNum > 0) {
            // 变量都不为空，
            logger.info("testng.xml中parameter标签的value值,已经通过注解 @Parameters 成功获取");
        }else {
            logger.error("testng.xml中parameter标签的value值,已经通过注解 @Parameters 获取失败！");
            throw new RuntimeException("testng.xml中parameter标签的value值,已经通过注解 @Parameters 获取失败！");
        }

    }
    /**
     * @ Description //这个空的@test方法是为了执行 @BeforeClass 的  @Parameters获取参数 ，后面使用初始化参数
     * @ Param[]
     * @ return void
     **/
    @Step("初始化参数：从 testng.xml 中读取配置")
    @Test
    public void startUp(){

    }
}
