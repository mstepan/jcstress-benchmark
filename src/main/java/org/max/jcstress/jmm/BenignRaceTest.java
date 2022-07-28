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
        java -jar target/jcstress.jar -t BenignRaceTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.

 */

@JCStressTest
@Outcome(id = "-1", expect = ACCEPTABLE, desc = "ref is NULL")
@Outcome(id = "13", expect = ACCEPTABLE, desc = "Fully init value should be 12")
@State
public class BenignRaceTest {


    MyObject ref;

    @Actor
    public void actor1(I_Result r) {
        MyObject temp = get();
        r.r1 = (temp == null) ? -1 : temp.val;
    }

    @Actor
    public void actor2(I_Result r) {
        MyObject temp = get();
        r.r1 = (temp == null) ? -1 : temp.val;
    }

    /*
    This only works if class is safely initialized (i.e. has only final fields, like 'MyObject'),
    and 'ref' field is read only once. Both conditions are critical for the race to be benign.

    Note: on x86 you won't find any problems even if you violate both rules, so to detect any inconsistent behaviour
    such as ref != null, but value == 0, you should use cpu with weaker memory guarantees: ARM or Power.
     */
    MyObject get() {
        MyObject temp = ref; // IMPORTANT: read 'ref' only once
        if (temp == null) {
            temp = new MyObject(13);
            ref = temp;
        }

        return temp;
    }

//    MyObject getWrongWay() {
//        if (ref == null) {
//            ref = new MyObject(13);
//        }
//
//        return ref; // WARNING: read 2-nd time
//    }

    static final class MyObject {

        // IMPORTANT: 'val' should be final
        final int val;

        public MyObject(int val) {
            this.val = val;
        }
    }
}
