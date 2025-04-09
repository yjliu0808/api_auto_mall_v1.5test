package com.loggerutil;

import org.apache.log4j.Logger;

/**
 * @Author: Athena
 * @Description: 通用日志工具类，支持根据调用类自动归类日志记录。
 * 可用于 static/非 static 场景，支持 info、warn、error 级别输出。
 */
public class BaseLogger {
    public static void logDebug(String content) {
        log(Level.DEBUG, content, null);
    }


    public static void logInfo(String content) {
        log(Level.INFO, content, null);
    }

    public static void logWarn(String content) {
        log(Level.WARN, content, null);
    }
    public static void logWarn(String content, Throwable t) {
        log(Level.WARN, content, t);
    }

    public static void logError(String content) {
        log(Level.ERROR, content, null);
    }

    public static void logError(String content, Throwable t) {
        log(Level.ERROR, content, t);
    }

    private static void log(Level level, String content, Throwable t) {
        if (content != null && !content.trim().isEmpty()) {
            String className = getCallerClassName();
            Logger logger = Logger.getLogger(className);

            switch (level) {
                case INFO:
                    logger.info(content);
                    break;
                case WARN:
                    logger.warn(content);
                    break;
                case ERROR:
                    if (t != null) {
                        logger.error(content, t);
                    } else {
                        logger.error(content);
                    }
                    break;
            }
        }
    }
    private enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    /**
     * 获取调用者的类名，用于动态创建对应类的 Logger
     */
    private static String getCallerClassName() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            String className = element.getClassName();
            if (!className.equals(BaseLogger.class.getName()) &&
                    !className.contains("java.lang.Thread")) {
                return className;
            }
        }
        return BaseLogger.class.getName();
    }
}
