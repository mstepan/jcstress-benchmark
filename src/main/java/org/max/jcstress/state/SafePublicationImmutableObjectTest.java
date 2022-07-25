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
