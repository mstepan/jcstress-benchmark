package org.max.jcstress.lock;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * Pure spin lock implementation, only busy spinning wait is used, no blocking/waiting.
 */
public final class SpinLock {

    private static final VarHandle VH_BUSY;

    private volatile boolean busy;

    static {
        try {
            VH_BUSY = MethodHandles.lookup().findVarHandle(SpinLock.class, "busy", boolean.class);
        }
        catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void lock() {
        while (true) {
            if (VH_BUSY.compareAndSet(this, false, true)) {
                break;
            }
        }
    }

    public void unlock() {
        VH_BUSY.setRelease(this, false);
    }

}
