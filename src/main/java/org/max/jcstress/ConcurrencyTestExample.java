package org.max.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.*;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t ConcurrencyTestExample

        Results across all configurations:

          RESULT      SAMPLES     FREQ       EXPECT  DESCRIPTION
            1, 1  170,469,222   35.45%  Interesting  Both actors came up with the same value: atomicity failure.
            1, 2  155,847,800   32.41%   Acceptable  actor1 incremented, then actor2.
            2, 1  154,610,722   32.15%   Acceptable  actor2 incremented, then actor1.
 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "1, 1", expect = ACCEPTABLE_INTERESTING, desc = "Both actors came up with the same value: atomicity failure.")
@Outcome(id = "1, 2", expect = ACCEPTABLE, desc = "actor1 incremented, then actor2.")
@Outcome(id = "2, 1", expect = ACCEPTABLE, desc = "actor2 incremented, then actor1.")
@State
public class ConcurrencyTestExample {

    int v;

    @Actor
    public void actor1(II_Result r) {
        r.r1 = ++v; // record result from actor1 to field r1
    }

    @Actor
    public void actor2(II_Result r) {
        r.r2 = ++v; // record result from actor2 to field r2
    }

}
