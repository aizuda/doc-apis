package com.docapis.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * log utils
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class LogUtils {

    private static final Logger LOGGER = Logger.getGlobal();

    static {
        try {
            FileHandler fileHandler = new FileHandler(DocContext.getLogFile().getAbsolutePath());
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message, Object... args) {
        LOGGER.info(String.format(message, args));
    }

    public static void warn(String message, Object... args) {
        LOGGER.warning(String.format(message, args));
    }

    public static void error(String message, Object... args) {
        LOGGER.severe(String.format(message, args));
    }

    public static void error(String message, Throwable e) {
        LOGGER.log(Level.SEVERE, message, e);
    }
}
