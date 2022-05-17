package unusual.spending.model.payments;

import org.junit.jupiter.api.Test;
import unusual.spending.CategoryPaymentsMapping;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.Price;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentsUnitTest {

    @Test
    void shouldReturnCategoryAndPaymentsMapping() {
        Payments previousMonthPayments = PaymentsFixture.monthWise(Month.MARCH)
                .priceAndCategory(Category.GOLF, Price.from(300.0))
                .priceAndCategory(Category.GOLF, Price.from(100.0))
                .payments();

        Payments currentMonthPayments = PaymentsFixture.monthWise(Month.APRIL)
                .priceAndCategory(Category.GOLF, Price.from(150.0))
                .priceAndCategory(Category.GOLF, Price.from(160.0))
                .priceAndCategory(Category.ENTERTAINMENT, Price.from(200.0))
                .priceAndCategory(Category.RESTAURANT, Price.from(250.0))
                .payments();

        Payments allPayments = previousMonthPayments.addAll(currentMonthPayments);

        Payments golfPayments = PaymentsFixture.categoryWise(Category.GOLF)
                .monthAndPrice(Month.APRIL, Price.from(160.0))
                .monthAndPrice(Month.APRIL, Price.from(150.0))
                .monthAndPrice(Month.MARCH, Price.from(300.0))
                .monthAndPrice(Month.MARCH, Price.from(100.0))
                .payments();

        Map<Category, Payments> expectedCategoryPaymentsMapping = Map.of(
                Category.GOLF, golfPayments,
                Category.RESTAURANT, PaymentsFixture.categoryWise(Category.RESTAURANT).monthAndPrice(Month.APRIL, Price.from(250.0)).payments(),
                Category.ENTERTAINMENT, PaymentsFixture.categoryWise(Category.ENTERTAINMENT).monthAndPrice(Month.APRIL, Price.from(200.0)).payments()
        );
        assertThat(CategoryPaymentsMapping.from(expectedCategoryPaymentsMapping))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(allPayments.categoryToPaymentsMapping());
    }

    @Test
    void shouldReturnEmptyCategoryAndPaymentsMappingForGivenMonthWhereNoPaymentsWhereMade() {
        Payments otherMonthsPayments = PaymentsFixture.categoryWise(Category.GOLF)
                .payments();

        assertEquals(CategoryPaymentsMapping.from(Collections.emptyMap()), otherMonthsPayments.categoryToPaymentsMapping());
    }

    @Test
    void shouldAddPaymentInPayments() {
        Payments payments = new Payments(new ArrayList<>());

        payments.add(new Payment(Price.from(100.0), "description", Category.GOLF, Month.MARCH));

        assertEquals(payments.stream().count(), 1);
    }

    @Test
    void shouldAddPaymentsWhenPaymentsWasMadeFromPayments() {
        Payments payments = new Payments(new Payments(Collections.emptyList()));

        payments.add(new Payment(Price.from(100.0), "description", Category.GOLF, Month.MARCH));

        assertEquals(payments.stream().count(), 1);
    }

    @Test
    void shouldAddTwoPayments() {
        Payments marchPayments = PaymentsFixture.monthWise(Month.MARCH)
                .priceAndCategory(Category.GOLF, Price.from(100.0))
                .payments();
        Payments aprilPayments = PaymentsFixture.monthWise(Month.APRIL)
                .priceAndCategory(Category.ENTERTAINMENT, Price.from(200.0))
                .priceAndCategory(Category.RESTAURANT, Price.from(250.0))
                .payments();
        System.out.println(marchPayments.addAll(aprilPayments));

        Payments expectedPayments = PaymentsFixture.instance()
                .addPayment(Price.from(100.0), Category.GOLF, Month.MARCH)
                .addPayment(Price.from(200.0), Category.ENTERTAINMENT, Month.APRIL)
                .addPayment(Price.from(250.0), Category.RESTAURANT, Month.APRIL)
                .payments();

        System.out.println(expectedPayments);

        assertThat(marchPayments.addAll(aprilPayments)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPayments);
    }
}