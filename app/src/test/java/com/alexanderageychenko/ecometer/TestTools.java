package com.alexanderageychenko.ecometer;

/**
 * Created by vladimiryuyukin on 27.12.16.
 */

public abstract class TestTools {
    static public void pause(Boolean[] stop) {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (stop[0]) {
                break;
            }
        }
        stop[0] = false;
    }
}
