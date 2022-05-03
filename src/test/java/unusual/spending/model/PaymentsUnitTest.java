package unusual.spending.model;

import org.junit.jupiter.api.Test;
import unusual.spending.CategoryPaymentsMapping;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentsUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnCategoryAndPaymentsMappingForCurrentMonth() {
        Month currentMonth = LocalDate.now(mockClock).getMonth();

        Payments previousMonthPayments = PaymentsFixture.monthWise(Month.MARCH)
                .priceAndCategory(Category.GOLF, 300.0)
                .priceAndCategory(Category.GOLF, 100.0)
                .payments();

        Payments currentMonthPayments = PaymentsFixture.monthWise(Month.APRIL)
                .priceAndCategory(Category.GOLF, 150.0)
                .priceAndCategory(Category.GOLF, 160.0)
                .priceAndCategory(Category.ENTERTAINMENT, 200.0)
                .priceAndCategory(Category.RESTAURANT, 250.0)
                .payments();
        Payments allPayments = previousMonthPayments.addAll(currentMonthPayments);

        Payments golfPaymentsInCurrentMonth = PaymentsFixture.categoryWise(Category.GOLF)
                .monthAndPrice(Month.APRIL, 160.0)
                .monthAndPrice(Month.APRIL, 150.0)
                .payments();

        Map<Category, Payments> expectedCategoryPaymentsMapping = Map.of(
                Category.GOLF, golfPaymentsInCurrentMonth,
                Category.RESTAURANT, PaymentsFixture.categoryWise(Category.RESTAURANT).monthAndPrice(Month.APRIL, 250.0).payments(),
                Category.ENTERTAINMENT, PaymentsFixture.categoryWise(Category.ENTERTAINMENT).monthAndPrice(Month.APRIL, 200.0).payments()
        );
        assertThat(CategoryPaymentsMapping.from(expectedCategoryPaymentsMapping))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(allPayments.categoryToPaymentsMapping(currentMonth));
    }

    @Test
    void shouldReturnCategoryAndPaymentsMappingForPreviousMonth() {
        Month previousMonth = LocalDate.now(mockClock).getMonth().minus(1L);
        Payments previousMonthPayments = PaymentsFixture.monthWise(Month.MARCH)
                .priceAndCategory(Category.GOLF, 100.0)
                .priceAndCategory(Category.GOLF, 300.0)
                .payments();
        Payments currentMonthPayments = PaymentsFixture.monthWise(Month.APRIL)
                .priceAndCategory(Category.GOLF, 150.0)
                .priceAndCategory(Category.GOLF, 150.0)
                .priceAndCategory(Category.ENTERTAINMENT, 200.0)
                .priceAndCategory(Category.RESTAURANT, 250.0)
                .payments();
        Payments allPayments = previousMonthPayments.addAll(currentMonthPayments);

        Map<Category, Payments> expectedCategoryPaymentsMapping = Map.of(Category.GOLF,
                PaymentsFixture.categoryWise(Category.GOLF)
                        .monthAndPrice(Month.MARCH, 300.0)
                        .monthAndPrice(Month.MARCH, 100.0)
                        .payments()
        );

        assertThat(CategoryPaymentsMapping.from(expectedCategoryPaymentsMapping))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(allPayments.categoryToPaymentsMapping(previousMonth));
    }

    @Test
    void shouldReturnEmptyCategoryAndPaymentsMappingForGivenMonthWhereNoPaymentsWhereMade() {
        Month noPaymentsMonth = Month.DECEMBER;
        Payments otherMonthsPayments = PaymentsFixture.categoryWise(Category.GOLF)
                .monthAndPrice(Month.MARCH, 100.0)
                .monthAndPrice(Month.APRIL, 150.0)
                .payments();

        assertEquals(CategoryPaymentsMapping.from(Collections.emptyMap()), otherMonthsPayments.categoryToPaymentsMapping(noPaymentsMonth));
    }

    @Test
    void shouldAddPaymentInPayments() {
        Payments payments = new Payments(new ArrayList<>());

        payments.add(new Payment(100.0, "description", Category.GOLF, Month.MARCH));

        assertEquals(payments.stream().count(), 1);
    }

    @Test
    void shouldAddTwoPayments() {
        Payments marchPayments = PaymentsFixture.monthWise(Month.MARCH)
                .priceAndCategory(Category.GOLF, 100.0)
                .payments();
        Payments aprilPayments = PaymentsFixture.monthWise(Month.APRIL)
                .priceAndCategory(Category.ENTERTAINMENT, 200.0)
                .priceAndCategory(Category.RESTAURANT, 250.0)
                .payments();
        System.out.println(marchPayments.addAll(aprilPayments));

        Payments expectedPayments = PaymentsFixture.instance()
                .addPayment(100.0, Category.GOLF, Month.MARCH)
                .addPayment(200.0, Category.ENTERTAINMENT, Month.APRIL)
                .addPayment(250.0, Category.RESTAURANT, Month.APRIL)
                .payments();

        System.out.println(expectedPayments);

        assertThat(marchPayments.addAll(aprilPayments)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPayments);
    }
}