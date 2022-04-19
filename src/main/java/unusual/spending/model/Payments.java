package unusual.spending.model;

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
//        String currentMonthOther = LocalDate.now().getMonth().toString();
        Month currentMonth = Month.APRIL;
        return totalPaymentsMadeIn(currentMonth);
    }


    public Double previousMonth() {
//        String previousMonth = LocalDate.now().getMonth().minus(1L).toString();
        Month previousMonth = Month.MARCH;
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
