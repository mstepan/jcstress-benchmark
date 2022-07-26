package org.max.jcstress.jmm;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t VolatileTrickyCaseTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.

 */

@JCStressTest
@Outcome(id = "-1", expect = ACCEPTABLE, desc = "User ref is NULL/")
@Outcome(id = "13", expect = ACCEPTABLE, desc = "User ref properly initialized.")
@Outcome(id = "0", expect = ACCEPTABLE_INTERESTING, desc = "ref != null, but value == 0.")
@State
public class VolatileTrickyCaseTest {

    static final class User {
        volatile int value;

        User() {
            this.value = 13;
        }
    }

    User ref;

    @Actor
    public void actor1() {
        ref = new User();
    }

    @Actor
    public void actor2(I_Result r) {
        User temp = this.ref;
        r.r1 = (temp == null) ? -1 : temp.value;
    }
}
