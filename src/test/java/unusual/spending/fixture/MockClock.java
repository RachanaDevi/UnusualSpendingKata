package unusual.spending.fixture;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import static java.time.Clock.fixed;


public final class MockClock {

    private MockClock() {
    }

    public static Clock fixedClock(int dayOfMonth, Month month, int year) {
        return fixed(LocalDate.of(year, month, dayOfMonth).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }
}
