package org.labs.items;

public class Waiter {

    public static int portionsAmount;

    private static final Object waitersLock = new Object();

    public boolean tryTakePortion() {
        synchronized (waitersLock) {
            if (portionsAmount > 0) {
                portionsAmount--;
                return true;
            }
            return false;
        }
    }
}
