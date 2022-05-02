package unusual.spending.model;

import unusual.spending.CategoryPaymentsMapping;

import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Payments {

    private final List<Payment> paymentList;

    public Payments(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void add(Payment payment) {
        this.paymentList.add(payment);
    }

    public CategoryPaymentsMapping categoryToPaymentsMapping(Month month) {
        Payments monthPayments = totalPaymentsMadeIn(month);
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        for (Payment payment : monthPayments.stream().toList()) {
            categoryPaymentsMapping.addPayment(payment);
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
