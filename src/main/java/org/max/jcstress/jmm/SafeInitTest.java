package org.max.jcstress.jmm;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZI_Result;

/*
Typical initialization anti-pattern, set some value and then set boolean flag to true. Both fields are volatile.

    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t SafeInitTest


RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
@Outcome(id = "true, 0", expect = FORBIDDEN, desc = "Violated safe publication for volatile release-acquire field.")
@Outcome(expect = ACCEPTABLE, desc = "All other cases valid")
@State
public class SafeInitTest {

    volatile boolean completed;

    int value;

    @Actor
    public void actor1() {
        value = 133;
        completed = true;
    }

    @Actor
    public void actor2(ZI_Result r) {
        r.r1 = completed;
        r.r2 = value;
    }
}
