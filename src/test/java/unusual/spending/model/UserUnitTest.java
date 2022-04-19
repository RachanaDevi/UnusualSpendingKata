package unusual.spending.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserUnitTest {

    @Test
    void shouldReturnTrueIfUserHasUnusualSpending() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", "golf", "March"),
                new Payment(50.0, "description", "golf", "March"),
                new Payment(100.0, "description", "golf", "April"),
                new Payment(60.0, "description", "golf", "April"))));

        Assertions.assertTrue(user.hasUnusualSpending());
    }

    @Test
    void shouldReturnFalseIfUserDoesNotHaveUnusualSpending() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", "golf", "March"),
                new Payment(50.0, "description", "golf", "March"),
                new Payment(5.0, "description", "golf", "April"))));

        Assertions.assertFalse(user.hasUnusualSpending());
    }

    @Test
    void shouldReturnFalseIfTheUserSpentSameAmountOfMoneyInBothMonths() {
        User user = new User(10L, new Payments(List.of(
                new Payment(50.0, "description", "golf", "March"),
                new Payment(50.0, "description", "golf", "April"))));

        Assertions.assertFalse(user.hasUnusualSpending());
    }
}