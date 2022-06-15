package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.Price;
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

class CategoryPaymentsUnitTest {

    @Test
    void shouldAddPaymentsAndCreateMapOfCategoryAndPayments() {
        CategoryPayments categoryPayments = new CategoryPayments();
        categoryPayments.addPayment(new Payment(Price.from(100.0), "description", GOLF, Month.MARCH));
        categoryPayments.addPayment(new Payment(Price.from(150.0), "description", GOLF, Month.APRIL));
        categoryPayments.addPayment(new Payment(Price.from(120.0), "description", RESTAURANT, Month.APRIL));

        Payments golfPayments = PaymentsFixture.categoryWise(GOLF)
                .monthAndPrice(Month.MARCH, Price.from(100.0))
                .monthAndPrice(Month.APRIL, Price.from(150.0))
                .payments();

        Payments restaurantPayments = PaymentsFixture.categoryWise(RESTAURANT)
                .monthAndPrice(Month.APRIL, Price.from(120.0))
                .payments();

        CategoryPayments expectedMapping = CategoryPayments.from(Map.of(
                GOLF, golfPayments,
                RESTAURANT, restaurantPayments)
        );

        assertThat(categoryPayments).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedMapping);
    }

    @Test
    void shouldReturnEmptyCategoryPaymentsMapGivenNoPaymentsWereMade() {
        CategoryPayments categoryPayments = new CategoryPayments();

        CategoryPayments expectedMapping = CategoryPayments.from(Collections.emptyMap());

        assertEquals(expectedMapping, categoryPayments);
    }


    @Test
    void shouldReturnPriceForGivenCategory() {
        CategoryPayments categoryPayments = new CategoryPayments();
        categoryPayments.addPayment(new Payment(Price.from(100.0), "description", GOLF, Month.MARCH));
        categoryPayments.addPayment(new Payment(Price.from(150.0), "description", GOLF, Month.APRIL));
        categoryPayments.addPayment(new Payment(Price.from(120.0), "description", RESTAURANT, Month.APRIL));
        categoryPayments.addPayment(new Payment(Price.from(1020.0), "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(Price.from(250.0), categoryPayments.totalPriceForCategory(GOLF));
    }

    @Test
    void shouldReturnPriceAsZeroIfCategoryIsNotPresent() {
        CategoryPayments categoryPayments = new CategoryPayments();
        categoryPayments.addPayment(new Payment(Price.from(100.0), "description", GOLF, Month.MARCH));
        categoryPayments.addPayment(new Payment(Price.from(150.0), "description", GOLF, Month.APRIL));
        categoryPayments.addPayment(new Payment(Price.from(1020.0), "description", ENTERTAINMENT, Month.JUNE));

        assertEquals(Price.zero(), categoryPayments.totalPriceForCategory(RESTAURANT));
    }

    @Test
    void shouldReturnCategoryPaymentsMappingWithCommonCategories() {
        CategoryPayments categoryPayments = new CategoryPayments();
        categoryPayments.addPayment(new Payment(Price.from(100.0), "description", GOLF, Month.MARCH));
        categoryPayments.addPayment(new Payment(Price.from(100.0), "description", ENTERTAINMENT, Month.MARCH));

        CategoryPayments otherCategoryPayments = new CategoryPayments();
        otherCategoryPayments.addPayment(new Payment(Price.from(150.0), "description", GOLF, Month.APRIL));
        otherCategoryPayments.addPayment(new Payment(Price.from(150.0), "description", RESTAURANT, Month.APRIL));

        Set<Category> expectedCategorySet = Set.of(GOLF);

        Set<Category> categoryIntersection = categoryPayments.categoryIntersection(otherCategoryPayments);

        assertThat(categoryIntersection).isEqualTo(expectedCategorySet);
    }

    @Test
    void shouldReturnEmptyCategoryPaymentsMappingWithNoCommonCategories() {
        CategoryPayments categoryPayments = new CategoryPayments();
        categoryPayments.addPayment(new Payment(Price.from(100.0), "description", ENTERTAINMENT, Month.MARCH));

        CategoryPayments otherCategoryPayments = new CategoryPayments();
        otherCategoryPayments.addPayment(new Payment(Price.from(150.0), "description", RESTAURANT, Month.APRIL));

        Set<Category> categoryPaymentsMappingIntersection = categoryPayments.categoryIntersection(otherCategoryPayments);

        assertThat(categoryPaymentsMappingIntersection).isEqualTo(Collections.emptySet());
    }


    private static final Clock mockClock = MockClock.fixedClock(18, Month.APRIL, 1999);

    @Test
    void shouldReturnMapOfCategoryAndUnusualSpendingIfUserHasUnusualSpending() {
        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(Price.from(50.0), Category.GOLF, Month.MARCH)
                .addPayment(Price.from(100.0), Category.RESTAURANT, Month.MARCH)
                .addPayment(Price.from(100.0), Category.ENTERTAINMENT, Month.MARCH)
                .payments(), mockClock);

        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(Price.from(1060.0), Category.GOLF, Month.APRIL)
                .addPayment(Price.from(1570.0), Category.RESTAURANT, Month.APRIL)
                .addPayment(Price.from(1060.0), Category.ENTERTAINMENT, Month.APRIL)
                .payments(), mockClock);

        CategoryPayments categoryPayments = currentMonthPayments.categoryToPaymentsMapping();
        CategoryPayments otherCategoryPayments = previousMonthPayments.categoryToPaymentsMapping();

        assertEquals(Map.of(Category.GOLF, Price.from(1060.0), Category.ENTERTAINMENT, Price.from(1060.0), Category.RESTAURANT, Price.from(1570.0)),
                categoryPayments.unusualSpendingsComparedWith(otherCategoryPayments));
    }

    @Test
    void shouldReturnEmptyMapIfUserDoesNotHaveUnusualSpending() {
        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(Price.from(50.0), Category.GOLF, Month.MARCH)
                .addPayment(Price.from(50.0), Category.GOLF, Month.MARCH).payments(), mockClock);

        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(Price.from(5.0), Category.GOLF, Month.APRIL).payments(), mockClock);


        CategoryPayments categoryPayments = currentMonthPayments.categoryToPaymentsMapping();
        CategoryPayments otherCategoryPayments = previousMonthPayments.categoryToPaymentsMapping();

        assertEquals(Collections.emptyMap(), categoryPayments.unusualSpendingsComparedWith(otherCategoryPayments));
    }

    @Test
    void shouldReturnEmptyMapIfTheUserSpentSameAmountOfMoneyInBothMonths() {
        CurrentMonthPayments currentMonthPayments = new CurrentMonthPayments(PaymentsFixture.instance()
                .addPayment(Price.from(50.0), Category.GOLF, Month.APRIL).payments(), mockClock);

        PreviousMonthPayments previousMonthPayments = new PreviousMonthPayments(PaymentsFixture.instance()
                .addPayment(Price.from(50.0), Category.GOLF, Month.MARCH).payments(), mockClock);

        Payments payments = mock(Payments.class);
        when(payments.currentMonthPayments()).thenReturn(currentMonthPayments);
        when(payments.previousMonthPayments()).thenReturn(previousMonthPayments);

        User user = new User(10L, payments);

        assertEquals(Collections.emptyMap(), user.unusualSpendings());
    }
}
