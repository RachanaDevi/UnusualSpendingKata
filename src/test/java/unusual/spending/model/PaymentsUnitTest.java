package unusual.spending.model;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentsUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnCategoryAndPaymentsMappingForCurrentMonth() {
        Month currentMonth = LocalDate.now(mockClock).getMonth();
        Payments payments = new Payments(
                List.of(
                        new Payment(100.0, "description", Category.GOLF, Month.MARCH),
                        new Payment(300.0, "description", Category.GOLF, Month.MARCH),
                        new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                        new Payment(200.0, "description", Category.ENTERTAINMENT, Month.APRIL),
                        new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL)
                ));

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

        assertEquals(expectedCategoryPaymentsMapping, payments.categoryToPaymentsMapping(currentMonth));
    }

    @Test
    void shouldReturnCategoryAndPaymentsMappingForPreviousMonth() {
        Month previousMonth = LocalDate.now(mockClock).getMonth().minus(1L);
        Payments payments = new Payments(List.of(
                new Payment(100.0, "description", Category.GOLF, Month.MARCH),
                new Payment(300.0, "description", Category.GOLF, Month.MARCH),
                new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                new Payment(150.0, "description", Category.GOLF, Month.APRIL),
                new Payment(200.0, "description", Category.ENTERTAINMENT, Month.APRIL),
                new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL)
        ));

        Payments golfPaymentsInCurrentMonth = new Payments(List.of(
                new Payment(100.0, "description", Category.GOLF, Month.MARCH),
                new Payment(300.0, "description", Category.GOLF, Month.MARCH)
        ));

        Map<Category, Payments> expectedCategoryPaymentsMapping = Map.of(
                Category.GOLF, golfPaymentsInCurrentMonth
        );

        assertEquals(expectedCategoryPaymentsMapping, payments.categoryToPaymentsMapping(previousMonth));
    }
}