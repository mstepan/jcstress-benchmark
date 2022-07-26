package org.max.jcstress.jmm;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t ReadAfterReadTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "Both reads returned 0")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "Both reads returned 1")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "First read returns 0, second 1")
@Outcome(id = "1, 0", expect = ACCEPTABLE_INTERESTING, desc = "First read returns 1, second 0 (like back in time)")
@State
public class ReadAfterReadTest {

    int x;

    @Actor
    public void actor1() {
        x = 1;
    }

    @Actor
    public void actor2(II_Result r) {
        r.r1 = x;
        r.r2 = x;
    }
}
