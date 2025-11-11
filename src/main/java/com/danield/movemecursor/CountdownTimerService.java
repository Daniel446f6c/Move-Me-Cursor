package com.danield.movemecursor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages a countdown timer and reports the time remaining.
 */
public class CountdownTimerService {

    private ScheduledExecutorService scheduler;
    private Runnable onFinishedCallback;
    private final ReadOnlyStringWrapper timeLeft = new ReadOnlyStringWrapper("00:00:00");

    /**
     * Starts the countdown.
     * @param duration How long the timer should run.
     */
    public void start(Duration duration) {
        if (scheduler != null && !scheduler.isShutdown()) {
            return;
        }

        final Instant endTime = Instant.now().plus(duration);
        scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable timerTask = () -> {
            Duration remaining = Duration.between(Instant.now(), endTime);
            if (remaining.isNegative() || remaining.isZero()) {
                Platform.runLater(() -> {
                    stop();
                    if (onFinishedCallback != null) {
                        onFinishedCallback.run();
                    }
                });
            }
            else {
                Platform.runLater(() -> timeLeft.set(formatDuration(remaining)));
            }
        };

        scheduler.scheduleAtFixedRate(timerTask, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the countdown timer and cleans up the thread.
     */
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        scheduler = null;
        timeLeft.set("00:00:00");
    }

    // --- JavaFX Properties and Callbacks ---
    public ReadOnlyStringProperty timeLeftProperty() {
        return timeLeft.getReadOnlyProperty();
    }

    public void setOnFinished(Runnable onFinishedCallback) {
        this.onFinishedCallback = onFinishedCallback;
    }

    /**
     * Helper to format a duration into HH:MM:SS.
     * @return <code>String</code>-i.e.: <b>01:29:59</b>
     */
    private String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }
}