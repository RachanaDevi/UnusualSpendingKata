package unusual.spending;

import org.junit.jupiter.api.Test;
import unusual.spending.model.Payment;
import unusual.spending.model.Payments;
import unusual.spending.model.User;

import java.util.List;

class TriggersUnusualSpendingEmailUnitTest {

    @Test
    void shouldSendEmailGivenUserHasUnusualSpending() {
        TriggersUnusualSpendingEmail triggersUnusualSpendingEmail = new TriggersUnusualSpendingEmail();
        User user = new User(10L,
                new Payments(
                        List.of(new Payment(10.0, "description", "golf", "March"),
                                new Payment(10.0, "description", "golf", "April"))
                ));
        triggersUnusualSpendingEmail.trigger(user);
    }
}