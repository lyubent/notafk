package io.github.lyubent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotAfk {

    private static final Logger logger = LogManager.getLogger(NotAfk.class);

    public static void main(String[] args) {
        logger.info("Starting application");
        logger.info("Application completed, exiting.");
        System.exit(0);
    }
}
