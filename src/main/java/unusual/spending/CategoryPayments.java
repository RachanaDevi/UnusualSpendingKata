package unusual.spending;

import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.Price;
import unusual.spending.model.payments.Payments;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryPayments {
    private static final int FIFTY_PERCENT = 50;
    private final Map<Category, Payments> categoryPaymentsMap;

    public CategoryPayments() {
        categoryPaymentsMap = new HashMap<>();
    }

    private CategoryPayments(Map<Category, Payments> categoryPaymentsMap) {
        this.categoryPaymentsMap = categoryPaymentsMap;
    }

    public static CategoryPayments from(Map<Category, Payments> categoryPaymentsMap) {
        return new CategoryPayments(categoryPaymentsMap);
    }

    public void addPayment(Payment payment) {
        if (categoryPaymentsMap.containsKey(payment.category())) {
            categoryPaymentsMap.get(payment.category()).add(payment);
        } else {
            categoryPaymentsMap.put(payment.category(), new Payments(new ArrayList<>() {{
                add(payment);
            }}));
        }
    }

    public Price totalPriceForCategory(Category category) {
        if (!this.categoryPaymentsMap.containsKey(category)) return Price.zero();
        return this.categoryPaymentsMap.get(category)
                .stream().map(Payment::price)
                .reduce(Price::add).get();
    }
/*
1. better name for fifty_
2. UnusualSpending().among(a, b) -> Map<Category, Price>
3. UnusualSpending.of(a, b)      -> UnusualSpending
 */
    public Map<Category, Price> unusualSpendingsComparedWith(CategoryPayments otherCategoryPayments) {
        Set<Category> categoryIntersection = this.categoryIntersection(otherCategoryPayments);
        Map<Category, Price> unusualCategoryPrices = new HashMap<>();
        for (Category category : categoryIntersection) {
            Price currentMonthPrice = this.totalPriceForCategory(category);
            Price previousMonthPrice = otherCategoryPayments.totalPriceForCategory(category);
            if (currentMonthPrice.percentageDifferenceWith(previousMonthPrice) > FIFTY_PERCENT) {
                unusualCategoryPrices.put(category, currentMonthPrice);
            }
        }
        return unusualCategoryPrices;
    }

    public Set<Category> categoryIntersection(CategoryPayments otherCategoryPayments) {
        Set<Category> categoryIntersection = new HashSet<>(this.categoriesSet());
        categoryIntersection.retainAll(otherCategoryPayments.categoriesSet());
        return categoryIntersection;
    }

    private Set<Category> categoriesSet() {
        return Stream.of(this.categoryPaymentsMap).flatMap(map -> map.keySet().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryPayments that = (CategoryPayments) o;
        return Objects.equals(categoryPaymentsMap, that.categoryPaymentsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryPaymentsMap);
    }
}
