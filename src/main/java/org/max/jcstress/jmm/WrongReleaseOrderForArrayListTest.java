package org.max.jcstress.jmm;

import java.util.ArrayList;
import java.util.List;
import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t WrongReleaseOrderForArrayListTest


Before fix:
  RESULT      SAMPLES     FREQ       EXPECT  DESCRIPTION
      -1  866,184,265   79.79%   Acceptable  List is NULL
       0    2,953,103    0.27%  Interesting  List is EMPTY
      13  216,454,184   19.94%   Acceptable  List has value 13

After fix:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.

 */

@JCStressTest
// These are the test outcomes.
@Outcome(id = "-1", expect = ACCEPTABLE, desc = "List is NULL")
@Outcome(id = "13", expect = ACCEPTABLE, desc = "List has value 13")
@Outcome(id = "0", expect = ACCEPTABLE_INTERESTING, desc = "List is EMPTY")
@State
public class WrongReleaseOrderForArrayListTest {

    volatile List<Integer> list;

    @Actor
    public void actorWrong1() {
        list = new ArrayList<>();
        list.add(13);
    }

//    @Actor
//    public void actorFixed1() {
//        List<Integer> data = new ArrayList<>();
//        data.add(13);
//        list = data;
//    }

    @Actor
    public void actor2(I_Result r) {
        List<Integer> temp = list;
        if (temp == null) {
            r.r1 = -1;
        }
        else {
            if (temp.isEmpty()) {
                r.r1 = 0;
            }
            else {
                r.r1 = temp.get(0);
            }
        }

    }

}
