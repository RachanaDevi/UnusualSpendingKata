package unusual.spending.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {
    private final Long id;
    private final Payments payments;

    public User(Long id, Payments payments) {
        this.id = id;
        this.payments = payments;
    }

    public Map<Category, Double> unusualSpending() {
        Map<Category, Double> unusualCategoryPrices = new HashMap<>();
        Set<Category> currentMonthCategories = Stream.of(payments.currentMonthCategoryPaymentsMapping()).flatMap(map -> map.keySet().stream()).collect(Collectors.toSet());
        Set<Category> previousMonthCategories = Stream.of(payments.previousMonthCategoryPaymentsMapping()).flatMap(map -> map.keySet().stream()).collect(Collectors.toSet());
        Set<Category> categoryIntersection = new HashSet<>(currentMonthCategories);
        categoryIntersection.retainAll(previousMonthCategories);
        if (categoryIntersection.isEmpty()) {
            return Collections.emptyMap();
        }
        for (Category category : currentMonthCategories) {
            Double currentMonthPrice = payments.currentMonthCategoryPaymentsMapping().get(category)
                    .stream().map(Payment::price)
                    .reduce(Double::sum).get();

            Double previousMonthPrice = payments.previousMonthCategoryPaymentsMapping().get(category)
                    .stream().map(Payment::price)
                    .reduce(Double::sum).get();
            if ((((currentMonthPrice - previousMonthPrice) * 100) / previousMonthPrice) > 50) {
                unusualCategoryPrices.put(category, currentMonthPrice);
            }
        }
        return unusualCategoryPrices;
    }
}
