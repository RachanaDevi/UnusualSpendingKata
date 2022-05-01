package unusual.spending.model;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Payments {

    private final List<Payment> paymentList;

    public Payments(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Set<Category> categoriesUsedInMonth(Month month) {
        return Stream.of(this.categoryToPaymentsMapping(month)).flatMap(map -> map.keySet().stream()).collect(Collectors.toSet());
    }

    public Map<Category, Payments> categoryToPaymentsMapping(Month month) {
        HashMap<Category, Payments> categoryPaymentsMapping = new HashMap<>();
        Payments monthPayments = totalPaymentsMadeIn(month);
        for (Payment payment : monthPayments.stream().toList()) {
            if (categoryPaymentsMapping.containsKey(payment.category())) {
                categoryPaymentsMapping.get(payment.category()).paymentList.add(payment);
            } else {
                categoryPaymentsMapping.put(payment.category(), new Payments(new ArrayList<>() {{
                    add(payment);
                }}));
            }
        }
        return categoryPaymentsMapping;
    }

    public Stream<Payment> stream() {
        return this.paymentList.stream();
    }

    private Payments totalPaymentsMadeIn(Month currentMonth) {
        List<Payment> paymentsMadeInMonth = paymentList
                .stream()
                .filter(payment -> payment.madeInMonth(currentMonth))
                .collect(Collectors.toList());
        return new Payments(paymentsMadeInMonth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payments payments = (Payments) o;
        return Objects.equals(paymentList, payments.paymentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentList);
    }
}
