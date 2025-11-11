package com.danield.movemecursor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Provides the logic behind the {@code PrimaryView.fxml} View.
 */
public class PrimaryController {

    // --- UI Components ---
    @FXML private Button btnStart;
    @FXML private CheckBox ckbxTimer;
    @FXML private ChoiceBox<String> choiceboxDurationSelect;
    @FXML private Label lblTimeLeft;

    // --- Business Logic Services ---
    private Task<Void> cursorMoverTask;
    private ScheduledExecutorService timerScheduler;

    // --- Constants ---
    private final Map<String, Long> DURATION_MAP = new LinkedHashMap<>();

    public PrimaryController() {
        DURATION_MAP.put("1min", 1L);
        DURATION_MAP.put("30min", 30L);
        DURATION_MAP.put("45min", 45L);
        DURATION_MAP.put("60min", 60L);
        DURATION_MAP.put("1h15min", 75L);
        DURATION_MAP.put("1h30min", 90L);
        DURATION_MAP.put("1h45min", 105L);
        DURATION_MAP.put("2h", 120L);
        DURATION_MAP.put("2h30min", 150L);
        DURATION_MAP.put("3h", 180L);
        DURATION_MAP.put("3h30min", 210L);
        DURATION_MAP.put("4h", 240L);
    }

    @FXML
    public void initialize() {
        // --- UI Components ---
        ckbxTimer.setSelected(false);
        choiceboxDurationSelect.setVisible(false);
        lblTimeLeft.setVisible(false);
        choiceboxDurationSelect.getItems().addAll(DURATION_MAP.keySet());
        choiceboxDurationSelect.getSelectionModel().select(0);
    }

    /**
     * Event handler bind to the Timer Checkbox.
     */
    public void timerCheckboxPressed(ActionEvent event) {
        choiceboxDurationSelect.setVisible(!choiceboxDurationSelect.isVisible());
        lblTimeLeft.setVisible(!lblTimeLeft.isVisible());
    }

    /**
     * Event handler bind to the Start/Stop Button
     */
    public void startButtonPressed(ActionEvent event) {
        if (cursorMoverTask != null && cursorMoverTask.isRunning()) {
            cursorMoverTask.cancel(true);
        }
        else {
            cursorMoverTask = new CursorMoverTask();
            cursorMoverTask.setOnSucceeded(e -> onTaskStop());
            cursorMoverTask.setOnCancelled(e -> onTaskStop());
            cursorMoverTask.setOnFailed(e -> onTaskStop());
            if (ckbxTimer.isSelected()) {
                startCountdownTimer();
            }
            new Thread(cursorMoverTask).start();
            onTaskStart();
        }
    }

    private void onTaskStart() {
        btnStart.setText("Stop");
        ckbxTimer.setDisable(true);
        choiceboxDurationSelect.setDisable(true);
    }

    private void onTaskStop() {
        btnStart.setText("Start");
        lblTimeLeft.setText("00:00:00");
        ckbxTimer.setDisable(false);
        choiceboxDurationSelect.setDisable(false);
        shutdownTimer();
    }

    private void startCountdownTimer() {
        final Instant endTime = Instant.now().plus(getDurationInMinutes());
        timerScheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable countdownTask = () -> {
            Instant now = Instant.now();
            Duration remaining = Duration.between(now, endTime);

            if (remaining.isNegative() || remaining.isZero()) {
                Platform.runLater(() -> cursorMoverTask.cancel(true));
            } else {
                Platform.runLater(() -> lblTimeLeft.setText(formatDuration(remaining)));
            }
        };

        timerScheduler.scheduleAtFixedRate(countdownTask, 0, 1, TimeUnit.SECONDS);
    }

    private void shutdownTimer() {
        if (timerScheduler != null && !timerScheduler.isShutdown()) {
            timerScheduler.shutdownNow();
            timerScheduler = null;
        }
    }

    /**
     * Helper to format the duration to a pretty String.
     * @return <code>String</code>-i.e.: <b>01:29:59</b>
     */
    private String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

    /**
     * Helper to get the selected duration.
     * @return <code>Duration</code> in minutes.
     */
    private Duration getDurationInMinutes() {
        return Duration.ofMinutes(DURATION_MAP.get(choiceboxDurationSelect.getValue()));
    }
}
