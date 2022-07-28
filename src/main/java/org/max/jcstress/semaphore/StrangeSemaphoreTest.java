package org.max.jcstress.semaphore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class StrangeSemaphoreTest {

    private static final int SEMAPHORE_CAPACITY = 2;
    private static final AtomicInteger THREADS_IN_SEMAPHORE = new AtomicInteger();
    private static final StrangeSemaphore SEMAPHORE = new StrangeSemaphore(SEMAPHORE_CAPACITY);

    public static void main(String[] args) throws Exception {

        final int threadsCount = Runtime.getRuntime().availableProcessors() * 2;
        final int iterationsCount = 100_000;

        final CountDownLatch allCompleted = new CountDownLatch(threadsCount);
        final ExecutorService pool = Executors.newFixedThreadPool(threadsCount);

        for (int i = 0; i < threadsCount; ++i) {
            pool.execute(() -> {

                try {
                    for (int it = 0; it < iterationsCount && !Thread.currentThread().isInterrupted(); ++it) {
                        SEMAPHORE.execute(() -> {
                            try {
                                checkSemaphoreCapacity(THREADS_IN_SEMAPHORE.incrementAndGet());
                            }
                            finally {
                                checkSemaphoreCapacity(THREADS_IN_SEMAPHORE.decrementAndGet());
                            }
                        });
                    }
                }
                finally {
                    allCompleted.countDown();
                }
            });
        }

        allCompleted.await();
        pool.shutdownNow();

        System.out.println("All completed.");
    }

    private static void checkSemaphoreCapacity(int val) {
        if (val < 0 || val > SEMAPHORE_CAPACITY) {
            throw new IllegalStateException("SEMAPHORE_CAPACITY violated, " +
                                                "expected in range [0; " + SEMAPHORE_CAPACITY + "], " +
                                                "but found: " + val);
        }
    }

}
