package io.github.lyubent.mousemover;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class MouseMover {

    private Robot robot;
    private static final double MOVEMENT_SPAN = 50;
    private static final Logger logger = LogManager.getLogger(MouseMover.class);

    public void run() {
        try {
            Platform.startup(() -> robot = new Robot());
            Instant start = Instant.now();
            Instant lastUpdate = Instant.now();
            int iterations = 0;

            // run for two hours
            while (Duration.between(start, Instant.now()).compareTo(Duration.of(2, ChronoUnit.HOURS)) < 0) {
                // have five seconds passed - avoids usage of Thread.sleep
                if (Duration.between(lastUpdate, Instant.now()).compareTo(Duration.of(5, ChronoUnit.SECONDS)) < 0) {
                    logger.trace("Skipping update, not enough time has passed.");
                    continue;
                }

                logger.trace("Updating mouse position");
                lastUpdate = Instant.now();

                int tempIteration = iterations;
                Platform.runLater(() -> {
                    double newMouseX = tempIteration % 2 == 0 ? robot.getMouseX() + MOVEMENT_SPAN
                                                              : robot.getMouseX() - MOVEMENT_SPAN;
                    robot.mouseMove(new Point2D(newMouseX, robot.getMouseY()));
                });
                iterations++;
            }
        } catch (Exception ex) {
            logger.error("Caught unrecoverable exception. Terminating application", ex);
        }
    }
}
