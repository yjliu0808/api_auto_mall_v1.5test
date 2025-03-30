package com.loggerutil;

import org.apache.log4j.Logger;

/**
 * @Author： Athena
 * @Desc： 所有类继承此基类，拥有统一 static logger，支持 static/非 static 方法中使用
 */
public class BaseLogger {
    // 使用 static logger，方便 static 方法中调用
    protected static final Logger logger = Logger.getLogger("AutoLogger");
    /**
     * @ Description //日志输出前做内容判空
     * @ Param[content]
     * @ return void
     **/
    protected static void logInfo(String content) {
        if (content != null) {
            String clean = content.trim(); // 去除前后空白字符（含换行）
            if (!clean.isEmpty()) {
                logger.info(content);
            }
        }
    }


}
