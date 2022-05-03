package unusual.spending.model;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;

import java.time.Clock;
import java.time.Month;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnTrueIfUserHasUnusualSpending() {
        User user = new User(10L, PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(100.0, Category.RESTAURANT, Month.MARCH)
                .addPayment(100.0, Category.ENTERTAINMENT, Month.MARCH)
                .addPayment(1060.0, Category.GOLF, Month.APRIL)
                .addPayment(1570.0, Category.RESTAURANT, Month.APRIL)
                .addPayment(1060.0, Category.ENTERTAINMENT, Month.APRIL).payments(), mockClock);

        assertEquals(Map.of(Category.GOLF, 1060.0, Category.ENTERTAINMENT, 1060.0, Category.RESTAURANT, 1570.0), user.unusualSpendings());
    }

    @Test
    void shouldReturnFalseIfUserDoesNotHaveUnusualSpending() {
        User user = new User(10L, PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(5.0, Category.GOLF, Month.APRIL).payments(), mockClock);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }

    @Test
    void shouldReturnFalseIfTheUserSpentSameAmountOfMoneyInBothMonths() {
        User user = new User(10L, PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(50.0, Category.GOLF, Month.APRIL).payments(), mockClock);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }
}