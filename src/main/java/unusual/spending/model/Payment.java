package unusual.spending.model;

import java.time.Month;
import java.util.Objects;

public class Payment {

    private final Price price;
    private final String description;
    private final Category category;
    private final Month month;

    public Payment(Price price,
                   String description,
                   Category category,
                   Month month) {
        this.price = price;
        this.description = description;
        this.category = category;
        this.month = month;
    }

    public boolean madeInMonth(Month month) {
        return this.month.equals(month);
    }

    public Price price() {
        return price;
    }

    public Category category() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(price, payment.price) && Objects.equals(description, payment.description) && Objects.equals(category, payment.category) && Objects.equals(month, payment.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, description, category, month);
    }
}
