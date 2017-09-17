package com.alexanderageychenko.ecometer.tools;

/**
 * Created by alexanderageychenko on 27.12.16.
 */

public abstract class TestTools {
    static public void pause(Boolean[] stop) {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean isStop = true;

            for (Boolean b : stop)
                isStop = isStop & b;

            if (isStop) break;
        }
        for (int i = 0; i < stop.length; ++i) {
            stop[i] = false;
        }
    }
}
