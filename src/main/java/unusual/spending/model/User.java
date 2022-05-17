package unusual.spending.model;

import unusual.spending.CategoryPaymentsMapping;
import unusual.spending.model.payments.Payments;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        return unusualSpendingDifferenceBetween(currentMonthCategoryPaymentsMapping, previousMonthCategoryPaymentsMapping);
    }

    private static Map<Category, Double> unusualSpendingDifferenceBetween(CategoryPaymentsMapping currentMonthCategoryPaymentsMapping,
                                                                          CategoryPaymentsMapping previousMonthCategoryPaymentsMapping
    ) {
        Set<Category> categoryIntersection = currentMonthCategoryPaymentsMapping.categoryIntersection(previousMonthCategoryPaymentsMapping);
        Map<Category, Double> unusualCategoryPrices = new HashMap<>();
        for (Category category : categoryIntersection) {
            Double currentMonthPrice = currentMonthCategoryPaymentsMapping.totalPriceForCategory(category);
            Double previousMonthPrice = previousMonthCategoryPaymentsMapping.totalPriceForCategory(category);

            if ((((currentMonthPrice - previousMonthPrice) * 100) / previousMonthPrice) > 50) {
                unusualCategoryPrices.put(category, currentMonthPrice);
            }
        }
        return unusualCategoryPrices;
    }
}
