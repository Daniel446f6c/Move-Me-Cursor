package com.danield.movemecursor;

import java.util.Random;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

/**
 * Handles the mouse movement. Cursor moves every 4-8s by a few pixel.
 * @author Daniel D
 */
public class CursorMover implements Runnable {

    @Override
    public void run() {
        Random rnd = new Random();
        Point mousePos;
        try {
            Robot robot = new Robot();
            try { 
                while (true)
                {
                    mousePos = MouseInfo.getPointerInfo().getLocation();
                    robot.mouseMove((int)mousePos.getX() + rnd.nextInt(-22, 23), (int)mousePos.getY() + rnd.nextInt(-22, 23));
                    Thread.sleep(rnd.nextInt(4000, 8001));
                }
            } catch (InterruptedException e) { /* Exit and Terminate Thread */ }
        } catch (AWTException ignored) {}
    }

}