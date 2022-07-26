package org.max.jcstress.state;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIII_Result;
import static org.openjdk.jcstress.util.UnsafeHolder.UNSAFE;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t IndependentReadsAndWritesTest

  RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "0, 0, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 0, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "0, 0, 1, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 0, 1, 1", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 1, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 1, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 0, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "1, 0, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 0, 1, 0", expect = FORBIDDEN, desc = "Inconsistent order of updates")
@Outcome(id = "1, 0, 1, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 1, 0", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 1, 1", expect = ACCEPTABLE)
@State
public class IndependentReadsAndWritesTest {

    int x;

    int y;

    @Actor
    public void actor1() {
        UNSAFE.fullFence();
        x = 1;
        UNSAFE.fullFence();
    }

    @Actor
    public void actor() {
        UNSAFE.fullFence();
        y = 1;
        UNSAFE.fullFence();
    }

    @Actor
    public void actor3(IIII_Result r) {
        UNSAFE.loadFence();
        r.r1 = x;
        UNSAFE.loadFence();
        r.r2 = y;
        UNSAFE.loadFence();
    }

    @Actor
    public void actor4(IIII_Result r) {
        UNSAFE.loadFence();
        r.r3 = y;
        UNSAFE.loadFence();
        r.r4 = x;
        UNSAFE.loadFence();
    }

}
