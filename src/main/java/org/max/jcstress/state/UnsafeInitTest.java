package org.max.jcstress.state;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IBF_Result;
import org.openjdk.jcstress.infra.results.IB_Result;
import org.openjdk.jcstress.infra.results.IF_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.IZ_Result;
import org.openjdk.jcstress.infra.results.I_Result;
import org.openjdk.jcstress.infra.results.ZI_Result;
import org.openjdk.jcstress.infra.results.Z_Result;

/*
Typical initialization anti-pattern, set some value and then set boolean flag to true. Both fields are volatile.

    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t UnsafeInitTest

  Results across all configurations:

      RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    false, 0  1,156,448,079   57.45%  Acceptable  Not initialized at all.
  false, 133      4,646,787    0.23%   Forbidden  No default case provided, assume Forbidden
   true, 133    851,984,686   42.32%  Acceptable  Fully initialized.

 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "true, 133", expect = ACCEPTABLE, desc = "Fully initialized.")
@Outcome(id = "false, 0", expect = ACCEPTABLE, desc = "Not initialized at all.")
@State
public class UnsafeInitTest {

    volatile boolean completed;

    volatile int value;

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
