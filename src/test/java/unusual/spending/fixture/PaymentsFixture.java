package unusual.spending.fixture;

import unusual.spending.model.Category;
import unusual.spending.model.Payment;
import unusual.spending.model.payments.Payments;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Fixture to add all payments
According to month
According to category
Just normal payments
 */
public class PaymentsFixture {

    private final List<Payment> payments;
    private final String description;

    private PaymentsFixture() {
        payments = new ArrayList<>();
        this.description = "description";
    }

    public static PaymentsFixture instance() {
        return new PaymentsFixture();
    }

    public PaymentsFixture addPayment(Double price, Category category, Month month) {
        this.payments.add(new Payment(price, this.description, category, month));
        return this;
    }

    public Payments payments() {
        return new Payments(payments);
    }

    public static PaymentsCategoryFixture categoryWise(Category category) {
        return new PaymentsCategoryFixture(category);
    }

    public static PaymentsMonthFixture monthWise(Month month) {
        return new PaymentsMonthFixture(month);
    }

    public static class PaymentsMonthFixture {
        private final Month month;
        private final Map<Category, List<Double>> categoryPriceMap;
        private final String description;

        private PaymentsMonthFixture(Month month) {
            this.month = month;
            this.categoryPriceMap = new HashMap<>();
            this.description = "description";
        }

        public PaymentsMonthFixture priceAndCategory(Category category, Double price) {
            if (!categoryPriceMap.containsKey(category)) {
                categoryPriceMap.put(category, new ArrayList<>() {{
                    add(price);
                }});
            } else {
                categoryPriceMap.get(category).add(price);
            }
            return this;
        }

        public Payments payments() {
            List<Payment> payments = new ArrayList<>();
            categoryPriceMap.forEach((category, prices) -> prices
                    .stream()
                    .map(price -> new Payment(price,
                            this.description,
                            category,
                            this.month))
                    .forEach(payments::add));
            return new Payments(payments);
        }
    }

    public static class PaymentsCategoryFixture {
        private final Category category;
        private final Map<Month, List<Double>> monthPriceMap;
        private final String description;

        public PaymentsCategoryFixture(Category category) {
            this.description = "description";
            this.monthPriceMap = new HashMap<>();
            this.category = category;
        }

        public PaymentsCategoryFixture monthAndPrice(Month month, Double price) {
            if (!monthPriceMap.containsKey(month)) {
                monthPriceMap.put(month, new ArrayList<>() {{
                    add(price);
                }});
            } else {
                monthPriceMap.get(month).add(price);
            }
            return this;
        }

        public Payments payments() {
            List<Payment> payments = new ArrayList<>();
            monthPriceMap.forEach((month, prices) -> prices.stream()
                    .map(price -> new Payment(price, this.description, this.category, month)
                    ).forEach(payments::add));
            return new Payments(payments);
        }
    }
}
