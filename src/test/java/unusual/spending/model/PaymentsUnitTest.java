package unusual.spending.model;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsUnitTest {

    @Test
    void shouldReturnPaymentForCurrentMonth() {
        Payments payments = new Payments(List.of(new Payment(150.0, "description", "golf", Month.APRIL)));

        assertEquals(payments.currentMonth(), 150.0, 0);
    }

    @Test
    void shouldReturnPaymentForPreviousMonth() {
        Payments payments = new Payments(List.of(new Payment(100.0, "description", "golf", Month.MARCH)));

        assertEquals(payments.previousMonth(), 100.0, 0);
    }
}