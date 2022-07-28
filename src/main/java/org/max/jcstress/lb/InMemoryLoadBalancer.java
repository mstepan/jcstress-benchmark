package org.max.jcstress.lb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.regex.Pattern;


public class InMemoryLoadBalancer {

    private final List<String> registeredAddresses = new ArrayList<>();

    /**
     * For better performance will use 'StampedLock' insteadof 'ReadWriteLock'
     */
    private final StampedLock lock = new StampedLock();

    private final int maxPoolSize;

    public InMemoryLoadBalancer(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    public boolean register(String address) {
        checkNotNull(address);
        checkValidAddress(address);

        long readStamp = lock.readLock();
        try {
            if (registeredAddresses.contains(address)) {
                return false;
            }
            if (registeredAddresses.size() == maxPoolSize) {
                throw new IllegalStateException("Pool size exhausted");
            }
        }
        finally {
            lock.unlockRead(readStamp);
        }

        long writeStamp = lock.writeLock();
        try {
            if (registeredAddresses.contains(address)) {
                return false;
            }
            if (registeredAddresses.size() == maxPoolSize) {
                throw new IllegalStateException("Pool size exhausted");
            }

            registeredAddresses.add(address);
        }
        finally {
            lock.unlockWrite(writeStamp);
        }

        return true;
    }

    private static final Pattern ADDRESS_REGEXP = Pattern.compile("[\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}");

    private void checkValidAddress(String address) {
        if (!ADDRESS_REGEXP.matcher(address).matches()) {
            throw new IllegalArgumentException("Incorrect 'address' detected: " + address);
        }
    }

    private void checkNotNull(String address) {
        if (address == null) {
            throw new IllegalArgumentException("'address' can't be null");
        }
    }
}

