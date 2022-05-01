package unusual.spending.model;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;

import java.time.Clock;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentsUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnPaymentForCurrentMonth() {
        Payments payments = new Payments(
                List.of(new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        (new Payment(100.0, "description", Category.GOLF, Month.MARCH))),
                mockClock
        );

        Payments paymentsInCurrentMonth = new Payments(
                List.of(new Payment(150.0, "description", Category.GOLF, Month.APRIL)));

        assertEquals(paymentsInCurrentMonth, payments.currentMonth());
    }

    @Test
    void shouldReturnPaymentForPreviousMonth() {
        Payments payments = new Payments(
                List.of(new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        (new Payment(100.0, "description", Category.GOLF, Month.MARCH))),
                mockClock
        );

        Payments paymentsInPreviousMonth = new Payments(
                List.of(new Payment(100.0, "description", Category.GOLF, Month.MARCH)));

        assertEquals(paymentsInPreviousMonth, payments.previousMonth());
    }

    @Test
    void shouldReturnCategoryAndPaymentsMappingForCurrentMonth() {
        Payments payments = new Payments(
                List.of(
                        new Payment(100.0, "description", Category.GOLF, Month.MARCH),
                        new Payment(300.0, "description", Category.GOLF, Month.MARCH),
                        new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        new Payment(200.0, "description", Category.ENTERTAINMENT, Month.APRIL),
                        new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL)
                ), mockClock);

        Payments golfPaymentsInCurrentMonth = new Payments(
                List.of(
                        new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        new Payment(150.0, "description", Category.GOLF, Month.APRIL)

                ));
        Payments restaurantPaymentsInCurrentMonth = new Payments(
                List.of(
                        new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL)
                ));

        Payments entertainmentPaymentsInCurrentMonth = new Payments(
                List.of(
                        new Payment(200.0, "description", Category.ENTERTAINMENT, Month.APRIL)
                ));
        Map<Category, Payments> expectedCategoryPaymentsMapping = Map.of(
                Category.GOLF, golfPaymentsInCurrentMonth,
                Category.RESTAURANT, restaurantPaymentsInCurrentMonth,
                Category.ENTERTAINMENT, entertainmentPaymentsInCurrentMonth
        );

        assertEquals(expectedCategoryPaymentsMapping, payments.currentMonthCategoryPaymentsMapping());
    }

    @Test
    void shouldReturnCategoryAndPaymentsMappingForPreviousMonth() {
        Payments payments = new Payments(List.of(
                new Payment(100.0, "description", Category.GOLF, Month.MARCH),
                new Payment(300.0, "description", Category.GOLF, Month.MARCH),
                new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                new Payment(200.0, "description", Category.ENTERTAINMENT, Month.APRIL),
                new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL)
        ), mockClock);

        Payments golfPaymentsInCurrentMonth = new Payments(List.of(
                new Payment(100.0, "description", Category.GOLF, Month.MARCH),
                new Payment(300.0, "description", Category.GOLF, Month.MARCH)
        ));

        Map<Category, Payments> expectedCategoryPaymentsMapping = Map.of(
                Category.GOLF, golfPaymentsInCurrentMonth
        );

        assertEquals(expectedCategoryPaymentsMapping, payments.previousMonthCategoryPaymentsMapping());
    }
}