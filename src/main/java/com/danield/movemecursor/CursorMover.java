package com.danield.movemecursor;

import java.util.Random;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class CursorMover implements Runnable {

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Random rnd = new Random();
            Point mousePos;
            try { 
                while (true)
                {
                    mousePos = MouseInfo.getPointerInfo().getLocation();
                    robot.mouseMove((int)mousePos.getX() + rnd.nextInt(-30, 31), (int)mousePos.getY() + rnd.nextInt(-30, 31));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {}
        } catch (AWTException e) {}
    }

}