package org.labs;

import org.labs.items.Spoon;
import org.labs.items.Waiter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static AtomicInteger portionsAmount;
    private static Waiter[] waiters;
    private static Spoon[] spoons;
    private static Programmer[] programmers;

    private static BlockingQueue<Waiter> waiterBlockingQueue;

    private static final Object lock = new Object();

    static class Programmer extends Thread {

        private final Spoon leftSpoon;
        private final Spoon rightSpoon;

        private int portionsConsumed = 0;

        Programmer(String programmerName, Spoon leftSpoon, Spoon rightSpoon) {
            super(programmerName);
            this.leftSpoon = leftSpoon;
            this.rightSpoon = rightSpoon;
        }

        private synchronized void printMessage(String message) throws InterruptedException {
            int ms = (int) (Math.random() * 100);
            System.out.println("Programmer_" + Thread.currentThread().getName() + " " + message + " for: " + ms + "ms");
            Thread.sleep(ms);
        }

        public void trashTalk() throws InterruptedException {
            printMessage("is trash-talking");
        }

        public boolean requestPortion() throws InterruptedException {
            Waiter waiter = waiterBlockingQueue.take();
            boolean portionTaken = waiter.tryTakePortion();
            waiterBlockingQueue.put(waiter);
            return portionTaken;
        }

        public void takeSpoons() throws InterruptedException {
            synchronized (leftSpoon) {
                printMessage("takes left spoon");
                synchronized (rightSpoon) {
                    printMessage("takes right spoon");
                }
            }
        }

        public void eat() throws InterruptedException {
            synchronized (lock) {
                if (portionsAmount.get() > 0) {
                    printMessage("is eating");
                    portionsAmount.getAndDecrement();
                    this.portionsConsumed++;
                }
            }
        }

        public void putSpoons() throws InterruptedException {
            printMessage("releasing spoons");
        }

        @Override
        public void run() {
            try {
                while (portionsAmount.get() > 1) {
                    trashTalk();
                    boolean portionTaken = requestPortion();
                    if (!portionTaken) continue;
                    takeSpoons();
                    eat();
                    putSpoons();
                }
                System.out.println("Done " + this.getName() + " with consumed: " + this.portionsConsumed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final int DEFAULT_PORTIONS_AMOUNT = 200;
    private static final int DEFAULT_WAITERS_AMOUNT = 2;
    private static final int DEFAULT_PROGRAMMERS_AMOUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        Waiter.portionsAmount = DEFAULT_PORTIONS_AMOUNT;

        portionsAmount = new AtomicInteger(DEFAULT_PORTIONS_AMOUNT);
        waiters = new Waiter[DEFAULT_WAITERS_AMOUNT];
        spoons = new Spoon[DEFAULT_PROGRAMMERS_AMOUNT];
        programmers = new Programmer[DEFAULT_PROGRAMMERS_AMOUNT];
        waiterBlockingQueue = new ArrayBlockingQueue<>(DEFAULT_WAITERS_AMOUNT);

        for (int i = 0; i < DEFAULT_WAITERS_AMOUNT; i++) {
            waiters[i] = new Waiter();
            waiterBlockingQueue.put(waiters[i]);
        }
        for (int i = 0; i < DEFAULT_PROGRAMMERS_AMOUNT; i++) {
            spoons[i] = new Spoon();
        }

        for (int i = 0; i < DEFAULT_PROGRAMMERS_AMOUNT; i++) {
            Spoon leftSpoon = spoons[i];
            Spoon rightSpoon = spoons[(i + 1) % DEFAULT_PROGRAMMERS_AMOUNT];

            Programmer currentProgrammer = i == DEFAULT_PROGRAMMERS_AMOUNT - 1
                    ? new Programmer(String.valueOf(i), rightSpoon, leftSpoon)
                    : new Programmer(String.valueOf(i), leftSpoon, rightSpoon);
            currentProgrammer.start();
            programmers[i] = currentProgrammer;
        }

        for (int i = 0; i < DEFAULT_PROGRAMMERS_AMOUNT; i++) {
            programmers[i].join();
        }
        int sumPortionsFromProgrammer = 0;
        for (int i = 0; i < DEFAULT_PROGRAMMERS_AMOUNT; i++) {
            sumPortionsFromProgrammer += programmers[i].portionsConsumed;
        }
        System.out.println("Food left: " + portionsAmount);
        System.out.println("Total consumed food: " + sumPortionsFromProgrammer);
    }
}