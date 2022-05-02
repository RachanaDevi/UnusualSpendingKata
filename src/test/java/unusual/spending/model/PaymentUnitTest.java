package unusual.spending.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Month;

class PaymentUnitTest {

    @Test
    void shouldReturnTrueIfPaymentWasMadeInParticularMonth() {
        Payment payment = new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL);

        Assertions.assertTrue(payment.madeInMonth(Month.APRIL));
    }

    @Test
    void shouldReturnFalseIfPaymentWasNotMadeInParticularMonth() {
        Payment payment = new Payment(250.0, "description", Category.RESTAURANT, Month.APRIL);

        Assertions.assertFalse(payment.madeInMonth(Month.MAY));
    }
}