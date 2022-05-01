package unusual.spending;

import unusual.spending.model.User;

public class TriggersUnusualSpendingEmail {

    public void trigger(User user) {
        // sends an email, will add that feature later
        if (!user.unusualSpending().isEmpty()) {
            // send email
        }
    }
}
