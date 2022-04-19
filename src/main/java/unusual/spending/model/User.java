package unusual.spending.model;

public class User {
    private final Long id;
    private final Payments payments;

    public User(Long id, Payments payments) {
        this.id = id;
        this.payments = payments;
    }

    public boolean hasUnusualSpending() {
        return (((payments.currentMonth() - payments.previousMonth()) * 100) / payments.previousMonth()) > 50;
    }
}
