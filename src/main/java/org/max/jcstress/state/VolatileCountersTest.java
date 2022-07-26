package org.max.jcstress.state;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t VolatileCountersTest

  Results across all configurations:
  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
      10    227,277,620    7.26%   Forbidden  No default case provided, assume Forbidden
      11        379,786    0.01%   Forbidden  No default case provided, assume Forbidden
      12        436,470    0.01%   Forbidden  No default case provided, assume Forbidden
      13        452,795    0.01%   Forbidden  No default case provided, assume Forbidden
      14        467,437    0.01%   Forbidden  No default case provided, assume Forbidden
      15        488,708    0.02%   Forbidden  No default case provided, assume Forbidden
      16        543,384    0.02%   Forbidden  No default case provided, assume Forbidden
      17        603,934    0.02%   Forbidden  No default case provided, assume Forbidden
      18        750,857    0.02%   Forbidden  No default case provided, assume Forbidden
      19        724,703    0.02%   Forbidden  No default case provided, assume Forbidden
       2              2   <0.01%   Forbidden  No default case provided, assume Forbidden
      20  2,896,597,337   92.58%  Acceptable  Both actors came up with the same value: atomicity failure.
       3             20   <0.01%   Forbidden  No default case provided, assume Forbidden
       4             63   <0.01%   Forbidden  No default case provided, assume Forbidden
       5            167   <0.01%   Forbidden  No default case provided, assume Forbidden
       6            850   <0.01%   Forbidden  No default case provided, assume Forbidden
       7          3,730   <0.01%   Forbidden  No default case provided, assume Forbidden
       8         16,374   <0.01%   Forbidden  No default case provided, assume Forbidden
       9         54,995   <0.01%   Forbidden  No default case provided, assume Forbidden

 */

@JCStressTest
@Outcome(id = "20", expect = ACCEPTABLE, desc = "Both actors came up with the same value: atomicity failure.")
@State
public class VolatileCountersTest {

    int val;

    @Actor
    public void actor1() {
        for (int i = 0; i < 10; ++i) {
            ++val;
        }
    }

    @Actor
    public void actor2() {
        for (int i = 0; i < 10; ++i) {
            ++val;
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = val;
    }

}
