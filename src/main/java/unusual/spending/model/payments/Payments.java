package unusual.spending.model.payments;

import unusual.spending.CategoryPayments;
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

    public CategoryPayments categoryToPaymentsMapping() {
        CategoryPayments categoryPayments = new CategoryPayments();
        for (Payment payment : this.paymentList) {
            categoryPayments.addPayment(payment);
        }
        return categoryPayments;
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
}
