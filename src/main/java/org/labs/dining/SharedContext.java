package org.labs.dining;

import org.labs.dining.items.Spoon;
import org.labs.dining.items.Waiter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SharedContext {

    private static Waiter[] waiters;
    private static Spoon[] spoons;
    private static BlockingQueue<Waiter> waiterBlockingQueue;

    public static Spoon getSpoon(int atIndex) {
        return spoons[atIndex];
    }

    public static BlockingQueue<Waiter> getWaiterBlockingQueue() {
        return waiterBlockingQueue;
    }

    public static void initialize(
            int portionsAmount,
            int waitersAmount,
            int spoonsAmount
    ) throws InterruptedException {
        Waiter.setPortionsAmount(portionsAmount);

        SharedContext.waiters = new Waiter[waitersAmount];
        SharedContext.spoons = new Spoon[spoonsAmount];
        SharedContext.waiterBlockingQueue = new ArrayBlockingQueue<>(waitersAmount);

        for (int i = 0; i < waitersAmount; i++) {
            SharedContext.waiters[i] = new Waiter();
            SharedContext.waiterBlockingQueue.put(SharedContext.waiters[i]);
        }
        for (int i = 0; i < spoonsAmount; i++) {
            SharedContext.spoons[i] = new Spoon();
        }
    }
}
