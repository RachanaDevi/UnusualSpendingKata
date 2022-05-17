package unusual.spending.model.payments;

import unusual.spending.CategoryPaymentsMapping;
import unusual.spending.model.Payment;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Payments {

    private final List<Payment> paymentList;

    public Payments(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    protected Payments(Payments payments) {
        this.paymentList = payments.stream().collect(Collectors.toList());
    }

    public void add(Payment payment) {
        this.paymentList.add(payment);
    }

    public Payments addAll(Payments payments) {
        List<Payment> allPayments = new ArrayList<>(this.paymentList);
        allPayments.addAll(payments.paymentList);
        return new Payments(allPayments);
    }

    public CategoryPaymentsMapping categoryToPaymentsMapping() {
        CategoryPaymentsMapping categoryPaymentsMapping = new CategoryPaymentsMapping();
        for (Payment payment : this.paymentList) {
            categoryPaymentsMapping.addPayment(payment);
        }
        return categoryPaymentsMapping;
    }

    public Stream<Payment> stream() {
        return this.paymentList.stream();
    }

    public Payments totalPaymentsMadeIn(Month currentMonth) {
        List<Payment> paymentsMadeInMonth = paymentList
                .stream()
                .filter(payment -> payment.madeInMonth(currentMonth))
                .collect(Collectors.toList());
        return new Payments(paymentsMadeInMonth);
    }

    public CurrentMonthPayments currentMonthPayments() {
        return new CurrentMonthPayments(this);
    }

    public PreviousMonthPayments previousMonthPayments() {
        return new PreviousMonthPayments(this);
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

    @Override
    public String toString() {
        return "Payments{" +
                "paymentList=" + paymentList +
                '}';
    }
}
