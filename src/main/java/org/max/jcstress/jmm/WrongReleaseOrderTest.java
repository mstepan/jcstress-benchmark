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
        java -jar target/jcstress.jar -t WrongReleaseOrderTest

Initial version:
  RESULT        SAMPLES     FREQ       EXPECT  DESCRIPTION
    0, 0  1,098,106,576   54.47%   Acceptable  Not initialized yet.
    0, 1        881,178    0.04%  Interesting  y not set, but x = 1
    1, 0      8,715,934    0.43%    Forbidden  y = 1, but x is 0
    1, 1    908,314,744   45.05%   Acceptable  Fully initialized.

  Fixe version:
  RESULT      SAMPLES     FREQ       EXPECT  DESCRIPTION
    0, 0  836,927,535   46.00%   Acceptable  Not initialized yet.
    0, 1    2,019,278    0.11%  Interesting  y not set, but x = 1
    1, 0            0    0.00%    Forbidden  y = 1, but x is 0
    1, 1  980,299,779   53.88%   Acceptable  Fully initialized.

 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "Not initialized yet.")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "Fully initialized.")
@Outcome(id = "0, 1", expect = ACCEPTABLE_INTERESTING, desc = "y not set, but x = 1")
@Outcome(id = "1, 0", expect = FORBIDDEN, desc = "y = 1, but x is 0")
@State
public class WrongReleaseOrderTest {

    int x;
    volatile int y;

    @Actor
    public void actorWrong1() {
        y = 1; // write to 'x' should precede write to 'y'
        x = 1;
    }

//    @Actor
//    public void actorFixed1() {
//        x = 1;
//        y = 1;
//    }

    @Actor
    public void actor2(II_Result r) {
        r.r1 = y;
        r.r2 = x;
    }

}
