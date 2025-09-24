package org.labs.dining;

import org.labs.dining.items.Programmer;
import org.labs.dining.items.Spoon;

public class DiningProgrammersWorld {

    private static final int DEFAULT_PORTIONS_AMOUNT = 1_000_000;
    private static final int DEFAULT_WAITERS_AMOUNT = 2;
    private static final int DEFAULT_PROGRAMMERS_AMOUNT = 5;

    private Programmer[] programmers;

    public Programmer[] getProgrammers() {
        return programmers;
    }

    public void startDining(
            Integer portionsAmount,
            Integer waitersAmount,
            Integer programmersAmount
    ) throws InterruptedException {
        int portionsAmountToInitialize = portionsAmount == null ? DEFAULT_PORTIONS_AMOUNT : portionsAmount;
        int waitersAmountToInitialize = waitersAmount == null ? DEFAULT_WAITERS_AMOUNT : waitersAmount;
        int programmersAmountToInitialize = programmersAmount == null ? DEFAULT_PROGRAMMERS_AMOUNT : programmersAmount;

        SharedContext.initialize(portionsAmountToInitialize, waitersAmountToInitialize, programmersAmountToInitialize);
        programmers = new Programmer[programmersAmountToInitialize];

        for (int i = 0; i < programmersAmountToInitialize; i++) {
            Spoon leftSpoon = SharedContext.getSpoon(i);
            Spoon rightSpoon = SharedContext.getSpoon((i + 1) % programmersAmountToInitialize);

            String programmerName = "Programmer_" + i;
            Programmer currentProgrammer = i == programmersAmountToInitialize - 1
                    ? new Programmer(programmerName, rightSpoon, leftSpoon)
                    : new Programmer(programmerName, leftSpoon, rightSpoon);
            currentProgrammer.start();
            programmers[i] = currentProgrammer;
        }

        for (int i = 0; i < programmersAmountToInitialize; i++) {
            programmers[i].join();
        }
    }
}
