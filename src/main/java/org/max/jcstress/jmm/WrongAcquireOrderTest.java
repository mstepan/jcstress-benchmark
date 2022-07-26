package org.max.jcstress.jmm;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t WrongAcquireOrderTest


 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "Not initialized yet.")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "Fully initialized.")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "x = 0, y = 1")
@Outcome(id = "1, 0", expect = ACCEPTABLE_INTERESTING, desc = "x = 1, y = 0")
@State
public class WrongAcquireOrderTest {

    int x;
    volatile int y;

    @Actor
    public void actor1() {
        x = 1;
        y = 1;
    }

    @Actor
    public void actorWrong2(II_Result r) {
        r.r1 = x;
        r.r2 = y;
    }

}
