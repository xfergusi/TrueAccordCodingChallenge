import APIResponces.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

}
