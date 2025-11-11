package com.danield.movemecursor;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private final CursorMoverService cursorService = new CursorMoverService();
    private final CountdownTimerService timerService = new CountdownTimerService();

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
        // --- Initialize UI Components ---
        choiceboxDurationSelect.getItems().addAll(DURATION_MAP.keySet());
        choiceboxDurationSelect.getSelectionModel().select(0);

        // --- Bind UI Component State ---
        // Bind to Timer Checkbox's state
        choiceboxDurationSelect.visibleProperty().bind(ckbxTimer.selectedProperty());
        lblTimeLeft.visibleProperty().bind(ckbxTimer.selectedProperty());

        // Bind to the CursorMoverService's running state
        btnStart.textProperty().bind(
                Bindings.when(cursorService.runningProperty())
                        .then("Stop")
                        .otherwise("Start")
        );
        ckbxTimer.disableProperty().bind(cursorService.runningProperty());
        choiceboxDurationSelect.disableProperty().bind(cursorService.runningProperty());

        // Bind to the CountdownTimerService's running state
        lblTimeLeft.textProperty().bind(timerService.timeLeftProperty());

        // When the timer finishes, tell the cursor service to stop
        timerService.setOnFinished(cursorService::stop);
    }

    /**
     * Event handler bind to the Start/Stop Button
     */
    public void startButtonPressed(ActionEvent event) {
        if (cursorService.isRunning()) {
            cursorService.stop();
            timerService.stop();
        }
        else {
            cursorService.start();
            if (ckbxTimer.isSelected()) {
                timerService.start(getDurationInMinutes());
            }
        }
    }

    /**
     * Helper to get the selected duration.
     * @return <code>Duration</code> in minutes.
     */
    private Duration getDurationInMinutes() {
        return Duration.ofMinutes(DURATION_MAP.get(choiceboxDurationSelect.getValue()));
    }
}
