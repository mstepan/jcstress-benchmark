package org.max.jcstress.lock;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t SpinLockTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "2, 2", expect = ACCEPTABLE, desc = "Atomicity and memory visibility working as expected.")
@State
public class SpinLockTest {

    final SpinLock lock = new SpinLock();

    int x;

    int y;

    @Actor
    public void actor1() {
        lock.lock();
        try {
            ++x;
            ++y;
        }
        finally {
            lock.unlock();
        }
    }

    @Actor
    public void actor2() {
        lock.lock();
        try {
            ++x;
            ++y;
        }
        finally {
            lock.unlock();
        }
    }

    @Arbiter
    public void arbiter(II_Result r) {
        r.r1 = x;
        r.r2 = y;
    }

}
