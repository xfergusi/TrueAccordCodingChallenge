import APIResponces.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to gether the important bits of information that are required for the coding challenge
 */
public final class OutputInformationGetter {

    /**
     * Simply returns if there is a valid payment plan number
     * @return boolean true if payment plan number if valid
     */
    public static boolean determineIfInPaymentPlan(Integer paymentPlanNumber) {
        return paymentPlanNumber != null;
    }


    /**
     * Takes the amount given and returns a remaining amount minus the payments
     * @param jsonData contains json info from api
     * @param paymentPlanId id we are currently looking at
     * @param amount we are subtracting each payment from this amount given
     * @return amount - all the payments
     */
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

    /**
     * finds the next payment date given the vaiables below
     * @param startDateString when the payments are scheduled to start
     * @param mostRecentPayment self explanatory
     * @param paymentFrequency how often the the payment is required
     * @param amountRemaining if this amount is 0, we return null
     * @return the next payment date
     */
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
