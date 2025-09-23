package org.labs.dining.items;

public class Waiter {

    private static final Object waitersLock = new Object();

    private static int portionsAmount;

    public static void setPortionsAmount(int portionsAmount) {
        Waiter.portionsAmount = portionsAmount;
    }

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
