package org.max.jcstress.state;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t ProgramOrderTest

java -jar target/jcstress.jar -t ProgramOrderTest ==>
  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, 0  1,162,474,952   55.55%  Acceptable  Not initialized yet.
    0, 1      3,299,938    0.16%  Acceptable  x only initialized
    1, 0         16,563   <0.01%   Forbidden  y was initialized before x.
    1, 1    926,986,019   44.29%  Acceptable  Both x & y initialized.

java -XX:-EliminateLocks -jar target/jcstress.jar -t ProgramOrderTest ==>
RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results. Use -v to print them.
 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "Not initialized yet.")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "x only initialized")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "Both x & y initialized.")
@Outcome(id = "1, 0", expect = FORBIDDEN, desc = "y was initialized before x.")
@State
public class ProgramOrderTest {

    private static final VarHandle VH_X, VH_Y;

    static {
        try {
            VH_X = MethodHandles.lookup().findVarHandle(ProgramOrderTest.class, "x", int.class);
            VH_Y = MethodHandles.lookup().findVarHandle(ProgramOrderTest.class, "y", int.class);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    int x;

    int y;

    @Actor
    public void actor1() {
        synchronized (this) {
            x = 1;
        }

        synchronized (this) {
            y = 1;
        }
    }

    @Actor
    public void actor2(II_Result r) {
        r.r1 = (int)VH_Y.getOpaque(this);
        r.r2 = (int)VH_X.getOpaque(this);
    }
}
