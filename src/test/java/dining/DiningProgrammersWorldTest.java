package dining;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.labs.dining.DiningProgrammersWorld;
import org.labs.dining.items.Programmer;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiningProgrammersWorldTest {

    private static final int SMALL_PORTIONS_AMOUNT_TO_SIMULATE = 1000;
    private static final int MEDIUM_PORTIONS_AMOUNT_TO_SIMULATE = 10_000;
    private static final int BIG_PORTIONS_AMOUNT_TO_SIMULATE = 100_000;
    private static final int LARGE_PORTIONS_AMOUNT_TO_SIMULATE = 1_000_000;

    private static final int WAITERS_AMOUNT_TO_SIMULATE = 2;

    private static final int PROGRAMMERS_AMOUNT_TO_SIMULATE = 5;

    public record DiningProgrammersWorldArgument(int portionsAmount, int waitersAmount, int programmersAmount){}

    private static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of(new DiningProgrammersWorldArgument(
                        SMALL_PORTIONS_AMOUNT_TO_SIMULATE,
                        WAITERS_AMOUNT_TO_SIMULATE,
                        PROGRAMMERS_AMOUNT_TO_SIMULATE
                    )
                ),
                Arguments.of(new DiningProgrammersWorldArgument(
                        MEDIUM_PORTIONS_AMOUNT_TO_SIMULATE,
                        WAITERS_AMOUNT_TO_SIMULATE,
                        PROGRAMMERS_AMOUNT_TO_SIMULATE
                    )
                ),
                Arguments.of(new DiningProgrammersWorldArgument(
                        BIG_PORTIONS_AMOUNT_TO_SIMULATE,
                        WAITERS_AMOUNT_TO_SIMULATE,
                        PROGRAMMERS_AMOUNT_TO_SIMULATE
                    )
                )
        );
    }

    @Test
    public void testNoDeadlocks() throws InterruptedException {
        new DiningProgrammersWorld().startDining(
                LARGE_PORTIONS_AMOUNT_TO_SIMULATE,
                WAITERS_AMOUNT_TO_SIMULATE,
                PROGRAMMERS_AMOUNT_TO_SIMULATE
        );
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    public void testNoRaceCondition(DiningProgrammersWorldArgument argument) throws InterruptedException {
        DiningProgrammersWorld diningProgrammersWorld = new DiningProgrammersWorld();
        diningProgrammersWorld.startDining(
                argument.portionsAmount(),
                argument.waitersAmount(),
                argument.programmersAmount()
        );

        Programmer[] programmers = diningProgrammersWorld.getProgrammers();

        int consumedPortionsSum = Arrays.stream(programmers)
                .map(Programmer::getPortionsConsumed)
                .reduce(0, Integer::sum);

        assertEquals(argument.portionsAmount(), consumedPortionsSum);
    }
}
