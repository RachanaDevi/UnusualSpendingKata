package unusual.spending;

import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.Payments;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryPaymentsMapping {
    private final Map<Category, Payments> categoryPaymentsMap;

    public CategoryPaymentsMapping() {
        categoryPaymentsMap = new HashMap<>();
    }

    private CategoryPaymentsMapping(Map<Category, Payments> categoryPaymentsMap) {
        this.categoryPaymentsMap = categoryPaymentsMap;
    }

    public static CategoryPaymentsMapping from(Map<Category, Payments> categoryPaymentsMap) {
        return new CategoryPaymentsMapping(categoryPaymentsMap);
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

    public Double totalPriceForCategory(Category category) {
        if (!this.categoryPaymentsMap.containsKey(category)) return 0.0;
        return this.categoryPaymentsMap.get(category)
                .stream().map(Payment::price)
                .reduce(Double::sum).get();
    }

    public Set<Category> categoryIntersection(CategoryPaymentsMapping otherCategoryPaymentsMapping) {
        Set<Category> categoryIntersection = new HashSet<>(this.categoriesSet());
        categoryIntersection.retainAll(otherCategoryPaymentsMapping.categoriesSet());
        return categoryIntersection;
    }

    private Set<Category> categoriesSet() {
        return Stream.of(this.categoryPaymentsMap).flatMap(map -> map.keySet().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryPaymentsMapping that = (CategoryPaymentsMapping) o;
        return Objects.equals(categoryPaymentsMap, that.categoryPaymentsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryPaymentsMap);
    }
}
