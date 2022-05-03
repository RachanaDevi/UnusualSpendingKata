package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.Payment;
import unusual.spending.model.Payments;

import java.time.Month;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static unusual.spending.model.Category.*;

class CategoryPaymentsMappingUnitTest {

    @Test
    void shouldAddPaymentsAndCreateMapOfCategoryAndPayments() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(120.0, "description", RESTAURANT, Month.APRIL));

        Payments golfPayments = PaymentsFixture.categoryWise(GOLF)
                .monthAndPrice(Month.MARCH, 100.0)
                .monthAndPrice(Month.APRIL, 150.0)
                .payments();

        Payments restaurantPayments = PaymentsFixture.categoryWise(RESTAURANT)
                .monthAndPrice(Month.APRIL, 120.0)
                .payments();

        CategoryPaymentsMapping expectedMapping = CategoryPaymentsMapping.from(Map.of(
                GOLF, golfPayments,
                RESTAURANT, restaurantPayments)
        );

        assertThat(categoryPaymentsMapping).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedMapping);
    }

    @Test
    void shouldReturnEmptyCategoryPaymentsMapGivenNoPaymentsWereMade() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();

        CategoryPaymentsMapping expectedMapping = CategoryPaymentsMapping.from(Collections.emptyMap());

        assertEquals(expectedMapping, categoryPaymentsMapping);
    }

    @Test
    void shouldReturnAllCategoriesPresentAfterPayments() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(120.0, "description", RESTAURANT, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(1020.0, "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(Set.of(GOLF, RESTAURANT, ENTERTAINMENT), categoryPaymentsMapping.categoriesSet());
    }

    @Test
    void shouldReturnNoCategoriesGivenNoPayments() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();

        assertEquals(Collections.emptySet(), categoryPaymentsMapping.categoriesSet());
    }

    @Test
    void shouldReturnPriceForGivenCategory() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(120.0, "description", RESTAURANT, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(1020.0, "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(250.0, categoryPaymentsMapping.priceForCategory(GOLF));
    }

    @Test
    void shouldReturnPriceAsZeroIfCategoryIsNotPresent() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(1020.0, "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(0.0, categoryPaymentsMapping.priceForCategory(RESTAURANT));
    }
}
