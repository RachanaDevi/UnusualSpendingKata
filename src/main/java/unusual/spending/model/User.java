package unusual.spending.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

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
        Map<Category, Double> unusualCategoryPrices = new HashMap<>();
        Set<Category> currentMonthCategories = payments.categoriesPaidIn(currentMonth());
        Set<Category> previousMonthCategories = payments.categoriesPaidIn(previousMonth());
        Set<Category> categoryIntersection = new HashSet<>(currentMonthCategories);
        categoryIntersection.retainAll(previousMonthCategories);
        if (categoryIntersection.isEmpty()) {
            return Collections.emptyMap();
        }
        for (Category category : currentMonthCategories) {
            Double currentMonthPrice = payments.categoryToPaymentsMapping(currentMonth()).priceForCategory(category);
            Double previousMonthPrice = payments.categoryToPaymentsMapping(previousMonth()).priceForCategory(category);

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
