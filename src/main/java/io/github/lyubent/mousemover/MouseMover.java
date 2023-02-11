package io.github.lyubent.mousemover;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MouseMover {

    private Robot robot;
    private static final double MOVEMENT_SPAN = 50;
    private static final Logger logger = LogManager.getLogger(MouseMover.class);

    public void run() {
        try {
            Platform.startup(() -> robot = new Robot());
            Instant start = Instant.now();
            // workaround - enables usage of var inside lambda as value is never reassigned - thus its effectively final
            AtomicInteger iterations = new AtomicInteger(0);

            // run for two hours
            while (Duration.between(start, Instant.now()).compareTo(Duration.of(2, ChronoUnit.HOURS)) < 0) {
                logger.trace("Updating mouse position");
                Platform.runLater(() -> {
                    double newMouseX = iterations.get() % 2 == 0 ? robot.getMouseX() + MOVEMENT_SPAN
                                                                 : robot.getMouseX() - MOVEMENT_SPAN;
                    robot.mouseMove(new Point2D(newMouseX, robot.getMouseY()));
                });
                iterations.getAndIncrement();
                // todo - this cannot be on the main thread, use an executor
                Thread.sleep(Duration.of(5, ChronoUnit.SECONDS).toMillis());
            }
        } catch (Exception ex) {
            logger.error("Caught unrecoverable exception. Terminating application", ex);
        }
    }
}
