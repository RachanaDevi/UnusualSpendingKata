package unusual.spending.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Payments {

    private final List<Payment> paymentList;
    private final Clock clock;

    public Payments(List<Payment> paymentList) {
        this.paymentList = paymentList;
        clock = Clock.systemDefaultZone();
    }

    Payments(List<Payment> paymentList, Clock clock) {
        this.paymentList = paymentList;
        this.clock = clock;
    }

    public Payments currentMonth() {
        Month currentMonth = LocalDate.now(this.clock).getMonth();
        return totalPaymentsMadeIn(currentMonth);
    }

    public Payments previousMonth() {
        Month previousMonth = LocalDate.now(this.clock).getMonth().minus(1L);
        return totalPaymentsMadeIn(previousMonth);
    }

    public Map<Category, Payments> currentMonthCategoryPaymentsMapping() {
        Payments paymentsInCurrentMonth = currentMonth();
        return categoryToPaymentMapFor(paymentsInCurrentMonth);
    }

    public Map<Category, Payments> previousMonthCategoryPaymentsMapping() {
        Payments paymentsInCurrentMonth = previousMonth();
        return categoryToPaymentMapFor(paymentsInCurrentMonth);
    }

    private HashMap<Category, Payments> categoryToPaymentMapFor(Payments paymentsInCurrentMonth) {
        HashMap<Category, Payments> categoryPaymentsMapping = new HashMap<>();
        for (Payment payment : paymentsInCurrentMonth.stream().toList()) {
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
                .filter(payment -> payment.month().equals(currentMonth))
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
