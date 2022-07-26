package org.max.jcstress.jmm;

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
        java -jar target/jcstress.jar -t MemoryBarrierWrongObjectsTest

  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, 0    810,359,284   43.85%  Acceptable  'x' & 'y' were not initialized yet.
    0, 1      3,655,046    0.20%  Acceptable  'x' read before init, 'y' read after init
    1, 0        437,524    0.02%   Forbidden  'x' read before init, 'y' read after init
    1, 1  1,033,456,498   55.93%  Acceptable  BOTH initialized.

 */

@JCStressTest
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "'x' & 'y' were not initialized yet.")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "BOTH initialized.")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "'x' read before init, 'y' read after init")
@Outcome(id = "1, 0", expect = FORBIDDEN, desc = "'x' read before init, 'y' read after init")
@State
public class MemoryBarrierWrongObjectsTest {

    int x;
    int y;

    @Actor
    public void actor1() {
        x = 1;
        synchronized (new Object()) {
        }
        y = 1;

    }

    @Actor
    public void actor2(II_Result r) {
        r.r1 = y;
        synchronized (new Object()) {
        }
        r.r2 = x;
    }
}
