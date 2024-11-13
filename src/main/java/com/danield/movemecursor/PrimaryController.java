package com.danield.movemecursor;

import javafx.scene.control.Button;

import java.io.IOException;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML private Button btnStart;

    Thread thread = new Thread(new CursorMover());

    @FXML
    private void startButtonPressed() throws IOException {
        
        if (thread.isAlive()) {
            thread.interrupt();
            btnStart.setText("Start");
        }
        else {
            thread = new Thread(new CursorMover());
            thread.start();
            btnStart.setText("Stop");
        }

    }
    
}
