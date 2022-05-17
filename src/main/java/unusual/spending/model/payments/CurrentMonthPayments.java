package unusual.spending.model.payments;

import java.time.Clock;

import static java.time.LocalDate.now;

public class CurrentMonthPayments extends Payments {

    public CurrentMonthPayments(Payments payments) {
        super(payments.totalPaymentsMadeIn(now(Clock.systemDefaultZone()).getMonth()));
    }

    // only for testing
    public CurrentMonthPayments(Payments payments, Clock clock) {
        super(payments.totalPaymentsMadeIn(now(clock).getMonth()));
    }
}
