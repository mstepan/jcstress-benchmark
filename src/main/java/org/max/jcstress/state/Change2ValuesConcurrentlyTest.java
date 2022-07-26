package org.max.jcstress.state;

import java.util.concurrent.atomic.AtomicStampedReference;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

/**
 * How to run this test:
 *      ./mvnw clean package
 *      java -jar target/jcstress.jar -t Change2ValuesConcurrentlyTest
 */
@JCStressTest
@Outcome(id = "2, 2", expect = ACCEPTABLE, desc = "Both x & y were incremented by one properly.")
@State
public class Change2ValuesConcurrentlyTest {

    final Counters counter = new Counters();

    @Actor
    public void actor1() {
        counter.inc();
    }

    @Actor
    public void actor2() {
        counter.inc();
    }

    @Arbiter
    public void arbiter(II_Result r) {
        Counters.CounterState lastState = counter.getState();
        r.r1 = lastState.x;
        r.r2 = lastState.y;
    }

    static class Counters {

        final AtomicStampedReference<CounterState> state = new AtomicStampedReference<>(new CounterState(0, 0), 0);

        void inc() {
            final int[] timeStamp = new int[1];
            CounterState prev, nextState;

            do {
                prev = state.get(timeStamp);
                nextState = new CounterState(prev.x + 1, prev.y + 1);
            }
            while (!state.compareAndSet(prev, nextState, timeStamp[0], timeStamp[0] + 1));
        }

        CounterState getState() {
            return state.getReference();
        }

        static final class CounterState {
            final int x;
            final int y;

            CounterState(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
    }
}
