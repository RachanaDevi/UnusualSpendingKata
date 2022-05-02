package unusual.spending.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;

import java.time.Clock;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnTrueIfUserHasUnusualSpending() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(100.0, "description", Category.RESTAURANT, Month.MARCH),
                new Payment(100.0, "description", Category.ENTERTAINMENT, Month.MARCH),
                new Payment(1060.0, "description", Category.GOLF, Month.APRIL),
                new Payment(1570.0, "description", Category.RESTAURANT, Month.APRIL),
                new Payment(1060.0, "description", Category.ENTERTAINMENT, Month.APRIL)
        )), mockClock);

        assertEquals(Map.of(Category.GOLF, 1060.0, Category.ENTERTAINMENT, 1060.0, Category.RESTAURANT, 1570.0), user.unusualSpendings());
    }

    @Test
    void shouldReturnFalseIfUserDoesNotHaveUnusualSpending() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(5.0, "description", Category.GOLF, Month.APRIL))), mockClock);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }

    @Test
    void shouldReturnFalseIfTheUserSpentSameAmountOfMoneyInBothMonths() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(50.0, "description", Category.GOLF, Month.APRIL))), mockClock);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }
}