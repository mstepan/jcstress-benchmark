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

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIII_Result;
import static org.openjdk.jcstress.util.UnsafeHolder.UNSAFE;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t IndependentReadsAndWritesTest

  RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "0, 0, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 0, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "0, 0, 1, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 0, 1, 1", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 1, 0", expect = ACCEPTABLE)
@Outcome(id = "0, 1, 1, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 0, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "1, 0, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 0, 1, 0", expect = FORBIDDEN, desc = "Inconsistent order of updates")
@Outcome(id = "1, 0, 1, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 0, 0", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 0, 1", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 1, 0", expect = ACCEPTABLE)
@Outcome(id = "1, 1, 1, 1", expect = ACCEPTABLE)
@State
public class IndependentReadsAndWritesTest {

    int x;

    int y;

    @Actor
    public void actor1() {
        UNSAFE.fullFence();
        x = 1;
        UNSAFE.fullFence();
    }

    @Actor
    public void actor() {
        UNSAFE.fullFence();
        y = 1;
        UNSAFE.fullFence();
    }

    @Actor
    public void actor3(IIII_Result r) {
        UNSAFE.loadFence();
        r.r1 = x;
        UNSAFE.loadFence();
        r.r2 = y;
        UNSAFE.loadFence();
    }

    @Actor
    public void actor4(IIII_Result r) {
        UNSAFE.loadFence();
        r.r3 = y;
        UNSAFE.loadFence();
        r.r4 = x;
        UNSAFE.loadFence();
    }

}
