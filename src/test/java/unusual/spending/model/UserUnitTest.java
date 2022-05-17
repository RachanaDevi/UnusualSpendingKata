package unusual.spending.model;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.payments.CurrentMonthPayments;
import unusual.spending.model.payments.Payments;
import unusual.spending.model.payments.PreviousMonthPayments;

import java.time.Clock;
import java.time.Month;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnMapOfCategoryAndUnusualSpendingIfUserHasUnusualSpending() {
        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(100.0, Category.RESTAURANT, Month.MARCH)
                .addPayment(100.0, Category.ENTERTAINMENT, Month.MARCH)
                .payments(), mockClock);

        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(1060.0, Category.GOLF, Month.APRIL)
                .addPayment(1570.0, Category.RESTAURANT, Month.APRIL)
                .addPayment(1060.0, Category.ENTERTAINMENT, Month.APRIL)
                .payments(), mockClock);

        Payments payments = mock(Payments.class);
        when(payments.currentMonthPayments()).thenReturn(currentMonthPayments);
        when(payments.previousMonthPayments()).thenReturn(previousMonthPayments);

        User user = new User(10L, payments);

        assertEquals(Map.of(Category.GOLF, 1060.0, Category.ENTERTAINMENT, 1060.0, Category.RESTAURANT, 1570.0), user.unusualSpendings());
    }

    @Test
    void shouldReturnEmptyMapIfUserDoesNotHaveUnusualSpending() {
        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(50.0, Category.GOLF, Month.MARCH).payments(), mockClock);

        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(5.0, Category.GOLF, Month.APRIL).payments(), mockClock);

        Payments payments = mock(Payments.class);
        when(payments.currentMonthPayments()).thenReturn(currentMonthPayments);
        when(payments.previousMonthPayments()).thenReturn(previousMonthPayments);

        User user = new User(10L, payments);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }

    @Test
    void shouldReturnEmptyMapIfTheUserSpentSameAmountOfMoneyInBothMonths() {
        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.APRIL).payments(), mockClock);

        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH).payments(), mockClock);

        Payments payments = mock(Payments.class);
        when(payments.currentMonthPayments()).thenReturn(currentMonthPayments);
        when(payments.previousMonthPayments()).thenReturn(previousMonthPayments);

        User user = new User(10L, payments);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }
}