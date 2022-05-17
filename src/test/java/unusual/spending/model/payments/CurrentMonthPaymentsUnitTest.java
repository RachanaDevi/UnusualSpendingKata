package unusual.spending.model.payments;

import org.junit.jupiter.api.Test;
import unusual.spending.CategoryPaymentsMapping;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.Category;
import unusual.spending.model.Payment;

import java.time.Clock;
import java.time.Month;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentMonthPaymentsUnitTest {

    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnCurrentPaymentsFromAllPaymentsForCurrentMonth() {
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
        CurrentMonthPayments actualCurrentMonthPayments = new CurrentMonthPayments(allPayments, mockClock);
        CurrentMonthPayments expectedCurrentMonthPayments = new CurrentMonthPayments(currentMonthPayments, mockClock);

        assertThat(actualCurrentMonthPayments)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCurrentMonthPayments);
    }

    @Test
    void shouldReturnCategoryAndPaymentsMappingForCurrentMonth() {
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
        CurrentMonthPayments actualCurrentMonthPayments = new CurrentMonthPayments(allPayments, mockClock);

        CategoryPaymentsMapping expectedCategoryPaymentsMapping = CategoryPaymentsMapping.from(Map.of(
                Category.GOLF, PaymentsFixture.categoryWise(Category.GOLF).monthAndPrice(Month.APRIL, 160.0).monthAndPrice(Month.APRIL, 150.0).payments(),
                Category.RESTAURANT, PaymentsFixture.categoryWise(Category.RESTAURANT).monthAndPrice(Month.APRIL, 250.0).payments(),
                Category.ENTERTAINMENT, PaymentsFixture.categoryWise(Category.ENTERTAINMENT).monthAndPrice(Month.APRIL, 200.0).payments())
        );

        assertThat(actualCurrentMonthPayments.categoryToPaymentsMapping())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCategoryPaymentsMapping);
    }

    @Test
    void shouldItAllowAddingAnyPaymentInCurrentPayments() {
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
        CurrentMonthPayments actualCurrentMonthPayments = new CurrentMonthPayments(allPayments, mockClock);
        actualCurrentMonthPayments.add(new Payment(12.0, "description", Category.GOLF, Month.MARCH));

        assertThat(actualCurrentMonthPayments.stream()).hasSize(5);
    }
}