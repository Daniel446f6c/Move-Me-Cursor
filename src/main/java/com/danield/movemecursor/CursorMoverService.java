package com.danield.movemecursor;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;

/**
 * Manages the lifecycle of the CursorMoverTask.
 */
public class CursorMoverService {

    private Task<Void> cursorMoverTask;
    private final ReadOnlyBooleanWrapper running = new ReadOnlyBooleanWrapper(false);

    /**
     * Starts the background task.
     */
    public void start() {
        if (running.get()) {
            return;
        }
        cursorMoverTask = new CursorMoverTask();
        cursorMoverTask.setOnSucceeded(e -> running.set(false));
        cursorMoverTask.setOnCancelled(e -> running.set(false));
        cursorMoverTask.setOnFailed(e -> running.set(false));
        running.set(true);
        new Thread(cursorMoverTask).start();
    }

    /**
     * Stops (cancels) the currently running task.
     */
    public void stop() {
        if (cursorMoverTask != null && running.get()) {
            cursorMoverTask.cancel(true);
        }
    }

    // --- JavaFX Properties ---
    public boolean isRunning() {
        return running.get();
    }

    public ReadOnlyBooleanProperty runningProperty() {
        return running.getReadOnlyProperty();
    }
}