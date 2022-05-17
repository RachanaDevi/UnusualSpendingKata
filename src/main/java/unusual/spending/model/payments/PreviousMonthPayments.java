package unusual.spending.model.payments;

import java.time.Clock;
import java.time.LocalDate;

public class PreviousMonthPayments extends Payments {

    public PreviousMonthPayments(Payments payments) {
        super(payments.totalPaymentsMadeIn(LocalDate.now(Clock.systemDefaultZone()).getMonth().minus(1L)));
    }

    // only for testing
    public PreviousMonthPayments(Payments payments, Clock clock) {
        super(payments.totalPaymentsMadeIn(LocalDate.now(clock).getMonth().minus(1L)));
    }
}
