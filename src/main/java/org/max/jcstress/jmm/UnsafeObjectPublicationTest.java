package org.max.jcstress.jmm;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t UnsafeObjectPublicationTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.

 */

@JCStressTest
@Outcome(id = "-1", expect = ACCEPTABLE, desc = "ref is NULL")
@Outcome(id = "4", expect = ACCEPTABLE, desc = "Fully init value should be 4")
@State
public class UnsafeObjectPublicationTest {

    int x = 1;

    // non volatile reference, so race will happen
    MyObject ref;

    @Actor
    public void actorPublisher() {
        ref = new MyObject(x);
    }

    @Actor
    public void actorConsumer(I_Result r) {
        MyObject temp = this.ref;
        if (temp == null) {
            r.r1 = -1;
        }
        else {
            r.r1 = (temp.x1 + temp.x2 + temp.x3 + temp.x4);
        }
    }

    static final class MyObject {
        int x1, x2, x3, x4;

        MyObject(int x) {
            this.x1 = x;
            this.x2 = x;
            this.x3 = x;
            this.x4 = x;
        }
    }

}
