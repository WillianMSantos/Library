package com.library.libraries.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerFacade {

    private final Logger logger;

    public LoggerFacade(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String msg, Object... args) {
        logger.info(msg, args);
    }

    public void warn(String msg, Object... args) {
        logger.warn(msg, args);
    }

    public void error(String msg, Object... args) {
        logger.error(msg, args);
    }
}
