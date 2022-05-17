package unusual.spending.model;

import java.util.Objects;

public class Price {

    private static final Price PRICE_ZERO = new Price(0.0);
    private final Double value;

    private Price(Double value) {
        this.value = value;
    }

    public static Price from(Double value) {
        return new Price(value);
    }

    public static Price zero() {
        return PRICE_ZERO;
    }

    public Price add(Price otherPrice) {
        return new Price(this.value + otherPrice.value);
    }

    public Double percentageDifferenceWith(Price otherPrice) {
        return (((this.value - otherPrice.value) * 100) / otherPrice.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
