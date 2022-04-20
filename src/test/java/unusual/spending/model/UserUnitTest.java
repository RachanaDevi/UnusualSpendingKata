package unusual.spending.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.List;

class UserUnitTest {

    @Test
    void shouldReturnTrueIfUserHasUnusualSpending() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(100.0, "description", Category.GOLF, Month.APRIL),
                new Payment(60.0, "description", Category.GOLF, Month.APRIL))));

        Assertions.assertTrue(user.hasUnusualSpending());
    }

    @Test
    void shouldReturnFalseIfUserDoesNotHaveUnusualSpending() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(5.0, "description", Category.GOLF, Month.APRIL))));

        Assertions.assertFalse(user.hasUnusualSpending());
    }

    @Test
    void shouldReturnFalseIfTheUserSpentSameAmountOfMoneyInBothMonths() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", Category.GOLF, Month.MARCH),
                new Payment(50.0, "description", Category.GOLF, Month.APRIL))));

        Assertions.assertFalse(user.hasUnusualSpending());
    }
}