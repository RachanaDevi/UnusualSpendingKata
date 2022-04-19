package unusual.spending.model;

import java.time.Month;
import java.util.Objects;

public class Payment {

    private final Double price;
    private final String description;
    private final String category;
    private final Month month;

    public Payment(Double price,
                   String description,
                   String category,
                   Month month) {
        this.price = price;
        this.description = description;
        this.category = category;
        this.month = month;
    }

    public Month month() {
        return month;
    }

    public Double price() {
        return price;
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
