package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.model.Payment;
import unusual.spending.model.Payments;

import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static unusual.spending.model.Category.*;

class CategoryPaymentsMappingTest {

    @Test
    void shouldAddPaymentsAndCreateMapOfCategoryAndPayments() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(120.0, "description", RESTAURANT, Month.APRIL));

        Payments golfPayments = new Payments(List.of(new Payment(100.0, "description", GOLF, Month.MARCH),
                new Payment(150.0, "description", GOLF, Month.APRIL)));
        Payments restaurantPayments = new Payments(List.of(new Payment(120.0, "description", RESTAURANT, Month.APRIL)));
        CategoryPaymentsMapping expectedMapping = CategoryPaymentsMapping.from(Map.of(
                GOLF, golfPayments,
                RESTAURANT, restaurantPayments)
        );

        assertEquals(expectedMapping, categoryPaymentsMapping);
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
