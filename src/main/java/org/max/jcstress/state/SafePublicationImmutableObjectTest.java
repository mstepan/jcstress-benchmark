package org.max.jcstress.state;

import java.util.concurrent.atomic.AtomicReference;
import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IZ_Result;

/*
Typical initialization anti-pattern, set some value and then set boolean flag to true. Both fields are volatile.

    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t SafePublicationImmutableObjectTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "133, true", expect = ACCEPTABLE, desc = "Fully initialized.")
@Outcome(id = "0, false", expect = ACCEPTABLE, desc = "Not initialized at all.")
@Outcome(id = "133, false", expect = FORBIDDEN, desc = "Partially initialized.")
@Outcome(id = "0, true", expect = FORBIDDEN, desc = "Partially: incorrect init order detected")
@State
public class SafePublicationImmutableObjectTest {

    final AtomicReference<ValueAndInitStatus> stateRef = new AtomicReference<>(new ValueAndInitStatus(0, false));

    @Actor
    public void actor1() {
        stateRef.set(new ValueAndInitStatus(133, true));
    }

    @Actor
    public void actor2(IZ_Result r) {
        ValueAndInitStatus state = stateRef.get();
        r.r1 = state.value;
        r.r2 = state.flag;
    }

    static class ValueAndInitStatus {
        final int value;
        final boolean flag;

        ValueAndInitStatus(int value, boolean flag) {
            this.value = value;
            this.flag = flag;
        }
    }
}
