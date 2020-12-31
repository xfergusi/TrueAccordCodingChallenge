
import APIResponces.Debt;
import APIResponces.Payment;
import APIResponces.PaymentPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TrueAccordCodingChallenge {

    static public void main(String[] args) throws IOException, ParseException {

        String allDebptsJson = getJsonString("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts");

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(allDebptsJson);
        JSONArray array = (JSONArray)obj;
        String paymentPlansJsonString = getJsonString(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans");
        String paymentsJsonString = getJsonString(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments");
        for(Object jsonObject : array) {

            JSONOutputInformation jsonOutputInformation = new JSONOutputInformation();
            ObjectMapper om = new ObjectMapper();
            Debt debt = om.readValue((jsonObject.toString()), Debt.class);
            jsonOutputInformation.id = debt.id;
            jsonOutputInformation.amount = debt.amount;
            jsonOutputInformation.payment_plan = determineIfInPaymentPlan(debt.id, paymentPlansJsonString);
            jsonOutputInformation.remaining_amount = determineRemainingAmount(
                    paymentsJsonString,
                    jsonOutputInformation.payment_plan,
                    jsonOutputInformation.amount);
            jsonOutputInformation.next_payment_due_date = determineNextPaymentDueDate(
                    determineStartDate(debt.id, paymentPlansJsonString),
                    determineMostRecentPayment(paymentsJsonString, jsonOutputInformation.payment_plan),
                    determinePaymentFrequency(debt.id, paymentPlansJsonString))
            ;
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            String jsonString = mapper.writeValueAsString(jsonOutputInformation);
            System.out.println(jsonString);

        }

    }

    private static String determineNextPaymentDueDate(String startDateString, LocalDate mostRecentPayment, String paymentFrequency)
            throws ParseException, JsonProcessingException {
        if (startDateString == null) {
            return null;
        }

        System.out.println(startDateString);
        System.out.println(mostRecentPayment);
        System.out.println(paymentFrequency);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);

        if(mostRecentPayment.isBefore(startDate)) {
            mostRecentPayment=startDate;
        }

        return (paymentFrequency.equals("WEEKLY")) ?
                mostRecentPayment.plusWeeks(1).toString() : mostRecentPayment.plusWeeks(2).toString();

    }



    private static String determinePaymentFrequency(int id, String paymentsJsonString) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(paymentsJsonString);
        JSONArray array = (JSONArray)obj;
        for(Object jsonObject : array) {
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                return paymentPlan.installment_frequency;
            }
        }
        return null;
    }
    private static LocalDate determineMostRecentPayment(String paymentsJsonString, Integer paymentPlanId)
            throws IOException, ParseException {
        if(paymentPlanId == null) {
            return null;
        }
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(paymentsJsonString);
        JSONArray array = (JSONArray)obj;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateReturn = LocalDate.parse("1800-01-01", formatter);
        for(Object jsonObject : array) {
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


    private static String determineStartDate(int id, String paymentPlanInfo) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(paymentPlanInfo);
        JSONArray array = (JSONArray)obj;
        for(Object jsonObject : array) {
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                return paymentPlan.start_date;
            }
        }
        return null;
    }

    private static Integer determineIfInPaymentPlan(int id, String paymentPlanInfo) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(paymentPlanInfo);
        JSONArray array = (JSONArray)obj;
        for(Object jsonObject : array) {
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                return paymentPlan.id;
            }
        }
       return null;
    }

    private static String getJsonString(String urlString) throws IOException {
        URL url = new URL(urlString);
        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();

        return inline.toString();

    }


    private static double determineRemainingAmount(String paymentsInfo, Integer paymentPlanId, double amount)
            throws ParseException, JsonProcessingException {
        if(paymentPlanId == null) {
            return amount;
        }
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(paymentsInfo);
        JSONArray array = (JSONArray)obj;
        for(Object jsonObject : array) {
            ObjectMapper om = new ObjectMapper();
            Payment payment = om.readValue((jsonObject.toString()), Payment.class);
            if(payment.payment_plan_id == paymentPlanId) {
                amount -= payment.amount;
            }
        }
        return amount;
    }




}
