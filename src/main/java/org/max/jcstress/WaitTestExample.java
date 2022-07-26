package org.max.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Mode;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.Signal;
import org.openjdk.jcstress.annotations.State;


/*
    How to run this test:
        ./mvnw clean package
        java -jar target/jcstress.jar -t WaitTestExample
 */
@JCStressTest(Mode.Termination)
@Outcome(id = "TERMINATED", expect = ACCEPTABLE, desc = "Wait successfully completed.")
@Outcome(id = "STALE", expect = ACCEPTABLE_INTERESTING, desc = "Test hung up.")
@State
public class WaitTestExample {

    @Actor
    public void actor1() throws InterruptedException {
        synchronized (this) {
            wait();
        }
    }

    @Signal
    public void signal() {
        synchronized (this) {
            notify();
        }
    }

}
