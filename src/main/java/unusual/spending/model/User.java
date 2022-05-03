package unusual.spending.model;

import unusual.spending.CategoryPaymentsMapping;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {
    private final Long id;
    private final Payments payments;

    private final Clock clock;

    public User(Long id, Payments payments) {
        this.id = id;
        this.payments = payments;
        this.clock = Clock.systemDefaultZone();
    }

    User(Long id, Payments payments, Clock clock) {
        this.id = id;
        this.payments = payments;
        this.clock = clock;
    }

    public Map<Category, Double> unusualSpendings() {
        CategoryPaymentsMapping currentMonthCategoryPaymentsMapping = payments.categoryToPaymentsMapping(currentMonth());
        CategoryPaymentsMapping previousMonthCategoryPaymentsMapping = payments.categoryToPaymentsMapping(previousMonth());

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

    private Month currentMonth() {
        return LocalDate.now(this.clock).getMonth();
    }

    private Month previousMonth() {
        return LocalDate.now(this.clock).getMonth().minus(1L);
    }
}
