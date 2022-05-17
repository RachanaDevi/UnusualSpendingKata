package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.User;
import unusual.spending.model.payments.CurrentMonthPayments;
import unusual.spending.model.payments.Payments;
import unusual.spending.model.payments.PreviousMonthPayments;

import java.time.Clock;
import java.time.Month;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

        CategoryPaymentsMapping categoryPaymentsMapping = currentMonthPayments.categoryToPaymentsMapping();
        CategoryPaymentsMapping otherCategoryPaymentsMapping = previousMonthPayments.categoryToPaymentsMapping();

        assertEquals(Map.of(Category.GOLF, 1060.0, Category.ENTERTAINMENT, 1060.0, Category.RESTAURANT, 1570.0),
                categoryPaymentsMapping.unusualSpendingsComparedWith(otherCategoryPaymentsMapping));
    }

    @Test
    void shouldReturnEmptyMapIfUserDoesNotHaveUnusualSpending() {
        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(50.0, Category.GOLF, Month.MARCH)
                .addPayment(50.0, Category.GOLF, Month.MARCH).payments(), mockClock);

        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(5.0, Category.GOLF, Month.APRIL).payments(), mockClock);


        CategoryPaymentsMapping categoryPaymentsMapping = currentMonthPayments.categoryToPaymentsMapping();
        CategoryPaymentsMapping otherCategoryPaymentsMapping = previousMonthPayments.categoryToPaymentsMapping();

        assertEquals(Collections.emptyMap(), categoryPaymentsMapping.unusualSpendingsComparedWith(otherCategoryPaymentsMapping));
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
