package org.max.jcstress.state;

import java.util.concurrent.atomic.AtomicIntegerArray;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.III_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t VolatileArrayTest

On x86 everything is working as expected:
RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.

On POWER architecture will fail.
 */

@JCStressTest
@Outcome(id = "1, 1, 1", expect = ACCEPTABLE, desc = "Volatile array is working as expected.")
@State
public class VolatileArrayTest {

    volatile int[] arr = new int[] {0, 0, 0};

    @Actor
    public void actor1() {
        int[] a = arr;
        a[0] = 1;
        a[1] = 1;
        a[2] = 1;
    }

    @Arbiter
    public void observer(III_Result r) {
        int[] a = arr;
        r.r1 = a[0];
        r.r2 = a[1];
        r.r3 = a[2];
    }

}
