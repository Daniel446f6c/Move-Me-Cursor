package com.danield.movemecursor;

import javafx.concurrent.Task;

import java.util.Random;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

/**
 * Handles the Cursor movement. Cursor moves every 4-10 seconds by a few pixels.
 */
public class CursorMoverTask extends Task<Void> {

    @Override
    public Void call() {
        Random rnd = new Random();
        Point mousePos;
        try {
            Robot robot = new Robot();
            try { 
                while (!isCancelled())
                {
                    mousePos = MouseInfo.getPointerInfo().getLocation();
                    robot.mouseMove((int)mousePos.getX() + rnd.nextInt(-22, 23), (int)mousePos.getY() + rnd.nextInt(-22, 23));
                    Thread.sleep(rnd.nextInt(4000, 10001));
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        } catch (AWTException ignored) {}
        return null;
    }
}