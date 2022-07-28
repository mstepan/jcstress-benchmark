package org.max.jcstress.lb;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZ_Result;

/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t LoadBalancerAddWithOneDuplicateTest

RUN RESULTS:
  Interesting tests: No matches.
  Failed tests: No matches.
  Error tests: No matches.
  All remaining tests: 1 matching test results.
 */

@JCStressTest
@Outcome(id = "true, false, true", expect = ACCEPTABLE)
@Outcome(id = "false, true, true", expect = ACCEPTABLE)
@State
public class LoadBalancerAddWithOneDuplicateTest {

    private final InMemoryLoadBalancer lb = new InMemoryLoadBalancer(10);

    @Actor
    public void actor1(ZZZ_Result r) {
        r.r1 = lb.register("127.0.0.1");
    }

    @Actor
    public void actor2(ZZZ_Result r) {
        r.r2 = lb.register("127.0.0.1");
    }

    @Actor
    public void actor3(ZZZ_Result r) {
        r.r3 = lb.register("127.0.0.2");
    }
}
