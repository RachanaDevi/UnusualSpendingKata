package unusual.spending.model;

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
        String currentMonth = "April";
        return totalPaymentsMadeIn(currentMonth);
    }


    public Double previousMonth() {
//        String previousMonth = LocalDate.now().getMonth().minus(1L).toString();
        String previousMonth = "March";
        return totalPaymentsMadeIn(previousMonth);
    }

    private Double totalPaymentsMadeIn(String currentMonth) {
        return paymentList.stream()
                .filter(payment -> payment.month().equals(currentMonth))
                .map(Payment::price)
                .reduce(Double::sum)
                .get();
    }
}
