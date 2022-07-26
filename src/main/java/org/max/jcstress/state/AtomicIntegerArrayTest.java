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
        java -jar target/jcstress.jar -t AtomicIntegerArrayTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
@Outcome(id = "1, 1, 1", expect = ACCEPTABLE, desc = "AtomicIntegerArray is working as expected.")
@State
public class AtomicIntegerArrayTest {

    final AtomicIntegerArray arr = new AtomicIntegerArray(3);

    @Actor
    public void actor1() {
        for (int i = 0; i < arr.length(); ++i) {
            arr.setRelease(i, 1);
        }
    }

    @Arbiter
    public void observer(III_Result r) {
        r.r1 = arr.get(0);
        r.r2 = arr.get(1);
        r.r3 = arr.get(2);
    }

}
