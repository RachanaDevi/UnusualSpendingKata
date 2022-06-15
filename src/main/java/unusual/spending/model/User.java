package unusual.spending.model;

import unusual.spending.CategoryPayments;
import unusual.spending.model.payments.Payments;

import java.util.Map;

public class User {
    private final Long id;
    private final Payments payments;

    public User(Long id, Payments payments) {
        this.id = id;
        this.payments = payments;
    }

    public Map<Category, Price> unusualSpendings() {
        CategoryPayments currentMonthCategoryPayments = payments.currentMonthPayments().categoryToPaymentsMapping();
        CategoryPayments previousMonthCategoryPayments = payments.previousMonthPayments().categoryToPaymentsMapping();

        return currentMonthCategoryPayments.unusualSpendingsComparedWith(previousMonthCategoryPayments);
    }
}
