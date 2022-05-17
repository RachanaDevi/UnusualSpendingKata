package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.Price;
import unusual.spending.model.payments.Payments;
import unusual.spending.model.User;

import java.time.Month;
import java.util.List;

class TriggersUnusualSpendingEmailUnitTest {

    @Test
    void shouldSendEmailGivenUserHasUnusualSpending() {
        TriggersUnusualSpendingEmail triggersUnusualSpendingEmail = new TriggersUnusualSpendingEmail();
        User user = new User(10L,
                new Payments(
                        List.of(new Payment(Price.from(10.0), "description", Category.GOLF, Month.MARCH),
                                new Payment(Price.from(10.0), "description", Category.GOLF, Month.APRIL))
                ));
        triggersUnusualSpendingEmail.trigger(user);

    }
}