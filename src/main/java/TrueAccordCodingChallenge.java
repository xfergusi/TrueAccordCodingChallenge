
import APIResponces.Debt;
import APIResponces.Payment;
import APIResponces.PaymentPlan;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The class is built to the requirement found at this page
 * https://gist.github.com/jeffling/2dd661ff8398726883cff09839dc316c
 *
 * How I spent my time:
 *      I learned a bit about working with JSON. This took most of my time.
 *      I first got something working. As you can see from my commits, once I got things working, I then refactored and tested
 *      I would probably redesign a bit if I had more time. I would also create more tests.
 *      I made several things public when I should I made them private, this was for ease of coding.
 */
public class TrueAccordCodingChallenge {

    static public void main(String[] args) throws IOException, ParseException {

        JsonData jsonData = new JsonData();

        for(Object jsonObject : jsonData.DebtsJsonArray) {

            JSONOutputInformation jsonOutputInformation = new JSONOutputInformation();
            ObjectMapper om = new ObjectMapper();
            Debt debt = om.readValue((jsonObject.toString()), Debt.class);
            jsonOutputInformation.id = debt.id;
            jsonOutputInformation.amount = debt.amount;
            Integer paymentPlanNumber = determinePaymentPlanNumber(debt.id, jsonData);
            jsonOutputInformation.is_in_payment_plan =
                    OutputInformationGetter.determineIfInPaymentPlan(paymentPlanNumber);
            jsonOutputInformation.remaining_amount =
                    OutputInformationGetter.determineRemainingAmount(
                            jsonData,
                            paymentPlanNumber,
                            debt.amount);
            jsonOutputInformation.next_payment_due_date =
                    OutputInformationGetter.determineNextPaymentDueDate(
                            determineStartDate(debt.id, jsonData),
                            determineMostRecentPayment(jsonData, paymentPlanNumber),
                            determinePaymentFrequency(debt.id, jsonData),
                            jsonOutputInformation.remaining_amount);

            System.out.println(JSONTools.createJsonOutput(jsonOutputInformation));

        }

    }

    private static String determinePaymentFrequency(int id, JsonData jsonData) throws IOException {
        for(Object jsonObject : jsonData.paymentPlansJsonArray) {
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                return paymentPlan.installment_frequency;
            }
        }
        return null;
    }
    private static LocalDate determineMostRecentPayment(JsonData jsonData, Integer paymentPlanId)
            throws IOException {
        if(paymentPlanId == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateReturn = LocalDate.parse("1800-01-01", formatter);
        for(Object jsonObject : jsonData.paymentsJsonArray) {
            ObjectMapper om = new ObjectMapper();
            Payment payment = om.readValue((jsonObject.toString()), Payment.class);
            if(payment.payment_plan_id == paymentPlanId) {
                LocalDate date = LocalDate.parse(payment.date, formatter);
                if(date.isAfter(dateReturn)) {
                    dateReturn = date;
                }
            }
        }
        return dateReturn;
    }


    private static String determineStartDate(int id, JsonData jsonData) throws IOException {
        for(Object jsonObject : jsonData.paymentPlansJsonArray) {
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                return paymentPlan.start_date;
            }
        }
        return null;
    }

    private static Integer determinePaymentPlanNumber(int id, JsonData jsonData) throws IOException {
        for(Object jsonObject : jsonData.paymentPlansJsonArray) {
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                return paymentPlan.id;
            }
        }
       return null;
    }


}
