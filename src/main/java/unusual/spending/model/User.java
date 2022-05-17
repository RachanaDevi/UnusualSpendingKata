package unusual.spending.model;

import unusual.spending.CategoryPaymentsMapping;
import unusual.spending.model.payments.Payments;

import java.util.Map;

public class User {
    private final Long id;
    private final Payments payments;

    public User(Long id, Payments payments) {
        this.id = id;
        this.payments = payments;
    }

    public Map<Category, Double> unusualSpendings() {
        CategoryPaymentsMapping currentMonthCategoryPaymentsMapping = payments.currentMonthPayments().categoryToPaymentsMapping();
        CategoryPaymentsMapping previousMonthCategoryPaymentsMapping = payments.previousMonthPayments().categoryToPaymentsMapping();

        return currentMonthCategoryPaymentsMapping.unusualSpendingsComparedWith(previousMonthCategoryPaymentsMapping);
    }
}
