package org.max.jcstress.lock;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t CriticalSectionUsingStringTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
@Outcome(id = "2", expect = ACCEPTABLE, desc = "Both updates completed successfully.")
@Outcome(id = "0", expect = FORBIDDEN, desc = "No updates detected.")
@Outcome(id = "1", expect = FORBIDDEN, desc = "Single update detected.")
@State
public class CriticalSectionUsingStringTest {

    int value;

    @Actor
    public void actor1() {
        synchronized ("my-lock") {
            ++value;
        }
    }

    @Actor
    public void actor2() {
        synchronized ("my-lock") {
            ++value;
        }
    }

    @Arbiter
    public void observer(I_Result r) {
        synchronized ("my-lock") {
            r.r1 = value;
        }
    }

}
