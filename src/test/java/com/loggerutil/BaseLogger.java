package com.loggerutil;

import org.apache.log4j.Logger;

/**
 * @Author： Athena
 * @Desc： 所有类继承此基类，拥有统一 static logger，支持 static/非 static 方法中使用
 */
public class BaseLogger {
    // 使用 static logger，方便 static 方法中调用
    protected static final Logger logger = Logger.getLogger("AutoLogger");
}
