import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class OutputInformationGetterTest {

    JsonData jsonData = new JsonData();

    OutputInformationGetterTest() throws IOException, ParseException {
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse("[  {    \"amount\": 123.46,    \"id\": 0  },  {    \"amount\": 100,    \"id\": 1  },  {    \"amount\": 4920.34,    \"id\": 2  },  {    \"amount\": 12938,    \"id\": 3  },  {    \"amount\": 9238.02,    \"id\": 4  }]\n");
        jsonData.DebtsJsonArray = (JSONArray) obj;
        obj = parser.parse("[  {    \"amount_to_pay\": 102.5,    \"debt_id\": 0,    \"id\": 0,    \"installment_amount\": 51.25,    \"installment_frequency\": \"WEEKLY\",    \"start_date\": \"2020-09-28\"  },  {    \"amount_to_pay\": 100,    \"debt_id\": 1,    \"id\": 1,    \"installment_amount\": 25,    \"installment_frequency\": \"WEEKLY\",    \"start_date\": \"2020-08-01\"  },  {    \"amount_to_pay\": 4920.34,    \"debt_id\": 2,    \"id\": 2,    \"installment_amount\": 1230.085,    \"installment_frequency\": \"BI_WEEKLY\",    \"start_date\": \"2020-01-01\"  },  {    \"amount_to_pay\": 4312.67,    \"debt_id\": 3,    \"id\": 3,    \"installment_amount\": 1230.085,    \"installment_frequency\": \"WEEKLY\",    \"start_date\": \"2020-08-01\"  }]\n");
        jsonData.paymentPlansJsonArray = (JSONArray) obj;
        obj = parser.parse("[  {    \"amount\": 51.25,    \"date\": \"2020-09-29\",    \"payment_plan_id\": 0  },  {    \"amount\": 51.25,    \"date\": \"2020-10-29\",    \"payment_plan_id\": 0  },  {    \"amount\": 25,    \"date\": \"2020-08-08\",    \"payment_plan_id\": 1  },  {    \"amount\": 25,    \"date\": \"2020-08-08\",    \"payment_plan_id\": 1  },  {    \"amount\": 4312.67,    \"date\": \"2020-08-08\",    \"payment_plan_id\": 2  },  {    \"amount\": 1230.085,    \"date\": \"2020-01-02\",    \"payment_plan_id\": 3  },  {    \"amount\": 1230.085,    \"date\": \"2020-01-15\",    \"payment_plan_id\": 3  },  {    \"amount\": 1230.085,    \"date\": \"2020-02-14\",    \"payment_plan_id\": 3  }]\n");
        jsonData.paymentsJsonArray = (JSONArray) obj;

    }

    @org.junit.jupiter.api.Test
    void determineIfInPaymentPlan() {
        Integer paymentPlan = 1;
        assertTrue(OutputInformationGetter.determineIfInPaymentPlan(paymentPlan));
        paymentPlan = null;
        assertFalse(OutputInformationGetter.determineIfInPaymentPlan(paymentPlan));
    }

    @org.junit.jupiter.api.Test
    void determineRemainingAmount() throws JsonProcessingException {
        double remainingAmount = OutputInformationGetter.determineRemainingAmount(jsonData, 0, 123.46);
        assertEquals(20.959999999999994, remainingAmount);
        remainingAmount = OutputInformationGetter.determineRemainingAmount(jsonData, null, 1000);
        assertEquals(1000, remainingAmount);
    }

    @org.junit.jupiter.api.Test
    void determineNextPaymentDueDate() {
        String startDateString = "2020-08-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate mostRecentPayment = LocalDate.parse("2020-02-14", formatter);
        String paymentFrequency = "WEEKLY";
        double amountRemaining = 9247.745000000003;
        String nextPaymentDate = OutputInformationGetter.determineNextPaymentDueDate(startDateString, mostRecentPayment,
                paymentFrequency, amountRemaining);
        assertEquals("2020-08-08", nextPaymentDate);

        startDateString = "2020-01-01";
        mostRecentPayment = LocalDate.parse("2020-08-08", formatter);
        paymentFrequency = "BI_WEEKLY";
        amountRemaining = 607.6700000000001;
        nextPaymentDate = OutputInformationGetter.determineNextPaymentDueDate(startDateString, mostRecentPayment,
                paymentFrequency, amountRemaining);
        assertEquals("2020-08-22", nextPaymentDate);

        startDateString = "2020-01-01";
        mostRecentPayment = LocalDate.parse("2020-08-08", formatter);
        paymentFrequency = "BI_WEEKLY";
        amountRemaining = 0;
        nextPaymentDate = OutputInformationGetter.determineNextPaymentDueDate(startDateString, mostRecentPayment,
                paymentFrequency, amountRemaining);
        assertNull(nextPaymentDate);





    }
}