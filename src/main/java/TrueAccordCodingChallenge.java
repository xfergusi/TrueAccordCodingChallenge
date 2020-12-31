
import APIResponces.Debt;
import APIResponces.Payment;
import APIResponces.PaymentPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            jsonOutputInformation.is_in_payment_plan = OutputInformationGetter.determineIfInPaymentPlan(paymentPlanNumber);
            jsonOutputInformation.remaining_amount = OutputInformationGetter.determineRemainingAmount(
                    jsonData,
                    paymentPlanNumber,
                    jsonOutputInformation.amount);
            jsonOutputInformation.next_payment_due_date = determineNextPaymentDueDate(
                    determineStartDate(debt.id, jsonData),
                    determineMostRecentPayment(jsonData, paymentPlanNumber),
                    determinePaymentFrequency(debt.id, jsonData),
                    jsonOutputInformation.remaining_amount);

            System.out.println(JSONTools.createJsonOutput(jsonOutputInformation));

        }

    }

    private static boolean determineIfInPaymentPlan(Integer paymentPlanNumber) {
        return paymentPlanNumber != null;
    }

    private static String determineNextPaymentDueDate(String startDateString, LocalDate mostRecentPayment,
                                                      String paymentFrequency, double amountRemaining) {
        if (startDateString == null || amountRemaining <= 0) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);

        if(mostRecentPayment.isBefore(startDate)) {
            mostRecentPayment=startDate;
        }

        return (paymentFrequency.equals("WEEKLY")) ?
                mostRecentPayment.plusWeeks(1).toString() : mostRecentPayment.plusWeeks(2).toString();

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
