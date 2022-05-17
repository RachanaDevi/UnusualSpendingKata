package unusual.spending.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceUnitTest {

    @Test
    void shouldEquateTwoPrices() {
        Price price = Price.from(10.012);
        Price otherPrice = Price.from(10.012);

        assertThat(price).isEqualTo(otherPrice);
    }

    @Test
    void shouldReturnPriceWithZero() {
        Price zeroPrice = Price.zero();
        Price expectedPrice = Price.from(0.00);

        assertThat(zeroPrice).isEqualTo(expectedPrice);
    }

    @Test
    void shouldAddPrices() {
        Price price = Price.from(100.01);
        Price otherPrice = Price.from(3020.0);

        Price expectedPrice = Price.from(3120.01);
        assertThat(price.add(otherPrice)).isEqualTo(expectedPrice);
    }

    @Test
    void shouldReturnPercentageDifference() {
        Price price = Price.from(510.00);
        Price otherPrice = Price.from(1000.0);

        assertThat(price.percentageDifferenceWith(otherPrice)).isEqualTo(-49.0);
    }
}