package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingUtil {
    private static final Level DEFAULT_LEVEL = Level.INFO;

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(DEFAULT_LEVEL);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(DEFAULT_LEVEL);
        handler.setFormatter(new SimpleFormatter());

        logger.addHandler(handler);
        logger.setUseParentHandlers(false);

        return logger;
    }
}