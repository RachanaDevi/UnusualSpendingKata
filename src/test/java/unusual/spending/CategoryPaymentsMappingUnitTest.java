package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.payments.Payments;

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
    void shouldReturnPriceForGivenCategory() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(120.0, "description", RESTAURANT, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(1020.0, "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(250.0, categoryPaymentsMapping.totalPriceForCategory(GOLF));
    }

    @Test
    void shouldReturnPriceAsZeroIfCategoryIsNotPresent() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        categoryPaymentsMapping.addPayment(new Payment(1020.0, "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(0.0, categoryPaymentsMapping.totalPriceForCategory(RESTAURANT));
    }

    @Test
    void shouldReturnCategoryPaymentsMappingWithCommonCategories() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", GOLF, Month.MARCH));
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", ENTERTAINMENT, Month.MARCH));

        CategoryPaymentsMapping otherCategoryPaymentsMapping = new CategoryPaymentsMapping();
        otherCategoryPaymentsMapping.addPayment(new Payment(150.0, "description", GOLF, Month.APRIL));
        otherCategoryPaymentsMapping.addPayment(new Payment(150.0, "description", RESTAURANT, Month.APRIL));

        Set<Category> expectedCategorySet = Set.of(GOLF);

        Set<Category> categoryIntersection = categoryPaymentsMapping.categoryIntersection(otherCategoryPaymentsMapping);

        assertThat(categoryIntersection).isEqualTo(expectedCategorySet);
    }

    @Test
    void shouldReturnEmptyCategoryPaymentsMappingWithNoCommonCategories() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        categoryPaymentsMapping.addPayment(new Payment(100.0, "description", ENTERTAINMENT, Month.MARCH));

        CategoryPaymentsMapping otherCategoryPaymentsMapping = new CategoryPaymentsMapping();
        otherCategoryPaymentsMapping.addPayment(new Payment(150.0, "description", RESTAURANT, Month.APRIL));

        Set<Category> categoryPaymentsMappingIntersection = categoryPaymentsMapping.categoryIntersection(otherCategoryPaymentsMapping);

        assertThat(categoryPaymentsMappingIntersection).isEqualTo(Collections.emptySet());
    }
}
