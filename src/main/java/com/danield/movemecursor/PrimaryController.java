package com.danield.movemecursor;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

/**
 * Provides the logic behind the {@code PrimaryView.fxml} View.
 * @author Daniel D
 */
public class PrimaryController {

    @FXML private Button btnStart;
    private Thread bgWorker = new Thread();

    /**
     * Event handler bind to the Start Button's {@code onAction} event.
     * @param event : the event
     */
    public void startButtonPressed(ActionEvent event) {
        
        if (bgWorker.isAlive()) {
            bgWorker.interrupt();
            btnStart.setText("Start");
        }
        else {
            bgWorker = new Thread(new CursorMover());
            bgWorker.start();
            btnStart.setText("Stop");
        }

    }
    
}
