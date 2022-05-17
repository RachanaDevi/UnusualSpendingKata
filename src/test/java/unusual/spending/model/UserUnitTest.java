package unusual.spending.model;

import org.junit.jupiter.api.Test;
import unusual.spending.fixture.MockClock;
import unusual.spending.fixture.PaymentsFixture;
import unusual.spending.model.payments.CurrentMonthPayments;
import unusual.spending.model.payments.Payments;
import unusual.spending.model.payments.PreviousMonthPayments;

import java.time.Clock;
import java.time.Month;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserUnitTest {
}