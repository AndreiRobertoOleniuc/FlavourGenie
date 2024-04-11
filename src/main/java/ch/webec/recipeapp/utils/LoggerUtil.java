package ch.webec.recipeapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logInfo(String message, Object... args) {
        logger.info(message, args);
    }

    public static void logError(String message, Object... args) {
        logger.error(message, args);
    }

    public static void logWarn(String message, Object... args) {
        logger.warn(message, args);
    }
}
