import APIResponces.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class OutputInformationGetter {

    public static boolean determineIfInPaymentPlan(Integer paymentPlanNumber) {
        return paymentPlanNumber != null;
    }

    public static double determineRemainingAmount(JsonData jsonData, Integer paymentPlanId, double amount)
            throws JsonProcessingException {
        if(paymentPlanId == null) {
            return amount;
        }
        for(Object jsonObject : jsonData.paymentsJsonArray) {
            ObjectMapper om = new ObjectMapper();
            Payment payment = om.readValue((jsonObject.toString()), Payment.class);
            if(payment.payment_plan_id == paymentPlanId) {
                amount -= payment.amount;
            }
        }
        return amount;
    }

    public static String determineNextPaymentDueDate(String startDateString, LocalDate mostRecentPayment,
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
}
