package org.labs.dining.items;

import org.labs.dining.SharedContext;

public class Programmer extends Thread {

    private final Spoon leftSpoon;
    private final Spoon rightSpoon;

    private int portionsConsumed = 0;

    public Programmer(String programmerName, Spoon leftSpoon, Spoon rightSpoon) {
        super(programmerName);
        this.leftSpoon = leftSpoon;
        this.rightSpoon = rightSpoon;
    }

    public int getPortionsConsumed() {
        return portionsConsumed;
    }

    private synchronized void printMessage(String message) throws InterruptedException {
        int ms = (int) (Math.random() * 2);
        System.out.println(Thread.currentThread().getName() + " " + message + " for: " + ms + "ms");
        Thread.sleep(ms);
    }

    public void trashTalk() throws InterruptedException {
        printMessage("is trash-talking");
    }

    public boolean requestPortion() throws InterruptedException {
        Waiter waiter = SharedContext.getWaiterBlockingQueue().take();
        boolean portionTaken = waiter.tryTakePortion();
        SharedContext.getWaiterBlockingQueue().put(waiter);
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
        printMessage("is eating");
        this.portionsConsumed++;
    }

    public void putSpoons() throws InterruptedException {
        printMessage("releasing spoons");
    }

    @Override
    public void run() {
        try {
            while (true) {
                trashTalk();
                boolean portionTaken = requestPortion();
                if (!portionTaken) break;
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
