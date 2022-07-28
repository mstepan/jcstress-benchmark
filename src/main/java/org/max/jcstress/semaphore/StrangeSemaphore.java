package org.max.jcstress.semaphore;

import java.util.concurrent.ThreadLocalRandom;

public final class StrangeSemaphore {

    private final int boundary;

    public StrangeSemaphore(int boundary) {
        checkBoundary(boundary, 0, 128);
        this.boundary = boundary;
    }

    private void checkBoundary(int value, int from, int to) {
        if (value < from || value > to) {
            throw new IllegalArgumentException("value should be in range [" + from + "; " + to + "], value = " + value);
        }
    }

    public void execute(Runnable r) {
        synchronized (Integer.valueOf(ThreadLocalRandom.current().nextInt(boundary))) {
            r.run();
        }
    }

}
