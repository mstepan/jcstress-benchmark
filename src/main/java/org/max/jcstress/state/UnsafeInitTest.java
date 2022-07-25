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
import org.openjdk.jcstress.infra.results.IBF_Result;
import org.openjdk.jcstress.infra.results.IB_Result;
import org.openjdk.jcstress.infra.results.IF_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.IZ_Result;
import org.openjdk.jcstress.infra.results.I_Result;

/*
Typical initialization anti-pattern, set some value and then set boolean flag to true. Both fields are volatile.

    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t UnsafeInitTest

  Results across all configurations:

      RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, false  1,020,771,519   56.22%  Acceptable  Not initialized at all.
     0, true      1,220,821    0.07%   Forbidden  Incorrect init order detected
  133, false      2,022,608    0.11%  Acceptable  Partially initialized.
   133, true    791,545,244   43.60%  Acceptable  Fully initialized.

 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "133, true", expect = ACCEPTABLE, desc = "Fully initialized.")
@Outcome(id = "0, false", expect = ACCEPTABLE, desc = "Not initialized at all.")
@Outcome(id = "133, false", expect = ACCEPTABLE, desc = "Partially initialized.")
@Outcome(id = "0, true", expect = FORBIDDEN, desc = "Incorrect init order detected")
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
    public void actor2(IZ_Result r) {
        r.r1 = value;
        r.r2 = completed;
    }
}
