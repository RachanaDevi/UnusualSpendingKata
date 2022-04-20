package unusual.spending.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Payments {

    private final List<Payment> paymentList;

    public Payments() {
        paymentList = new ArrayList<>();
    }

    public Payments(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Double currentMonth() {
        Month currentMonth = LocalDate.now(Clock.systemDefaultZone()).getMonth();
        return totalPaymentsMadeIn(currentMonth);
    }

    public Double previousMonth() {
        Month previousMonth = LocalDate.now(Clock.systemDefaultZone()).getMonth().minus(1L);
        return totalPaymentsMadeIn(previousMonth);
    }

    private Double totalPaymentsMadeIn(Month currentMonth) {
        return paymentList.stream()
                .filter(payment -> payment.month().equals(currentMonth))
                .map(Payment::price)
                .reduce(Double::sum)
                .get();
    }
}
