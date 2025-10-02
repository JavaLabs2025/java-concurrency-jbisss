package org.labs.dining.items;

import java.util.concurrent.atomic.AtomicInteger;

public class Waiter {

    private static AtomicInteger portionsAmount;

    public static void setPortionsAmount(int portionsAmount) {
        Waiter.portionsAmount = new AtomicInteger(portionsAmount);
    }

    public boolean tryTakePortion() {
        int decrementedPortionsAmount = portionsAmount.getAndDecrement();
        return decrementedPortionsAmount > 0;
    }
}
