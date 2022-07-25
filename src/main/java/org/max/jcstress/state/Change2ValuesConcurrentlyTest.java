/*
 * Copyright (c) 2017, Red Hat Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

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
