
import APIResponces.Debt;
import APIResponces.PaymentPlan;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class TrueAccordCodingChallenge {

    static public void main(String[] args) throws IOException, ParseException {

        String allDebptsJson = getJsonString("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts");

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(allDebptsJson);
        JSONArray array = (JSONArray)obj;
        String paymentPlanInfo = getJsonString(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans");
        for(Object jsonObject : array) {

            JSONOutput jsonOutput = new JSONOutput();
            ObjectMapper om = new ObjectMapper();
            Debt debt = om.readValue((jsonObject.toString()), Debt.class);
            jsonOutput.id = debt.id;
            jsonOutput.amount = debt.amount;

            jsonOutput.is_in_payment_plan = determineIfInPaymentPlan(debt.id, paymentPlanInfo);
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            String jsonString = mapper.writeValueAsString(jsonOutput);
            System.out.println(jsonString);

        }


//        URL url2 = new URL("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans?debt_id=0");
//        String inline2 = "";
//        Scanner scanner2 = new Scanner(url2.openStream());
//        while (scanner2.hasNext()) {
//            inline2 += scanner2.nextLine();
//        }
//        scanner2.close();
//        System.out.println(inline2);

//
//        JSONParser parse = new JSONParser();
//        JSONObject data_obj = (JSONObject) parse.parse(inline);

//        Debts debts = new Gson().fromJson(inline, Debts.class);

        /*

        System.out.println(con.getInputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
//            Debt debt = new Gson().fromJson(inputLine, Debt.class);
//            content.append(debt.id +"\n");
        }
        in.close();

        System.out.println(content);
*/
    }

    private static boolean determineIfInPaymentPlan(int id, String paymentPlanInfo) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(paymentPlanInfo);
        JSONArray array = (JSONArray)obj;
        for(Object jsonObject : array) {
            JSONOutput jsonOutput = new JSONOutput();
            ObjectMapper om = new ObjectMapper();
            PaymentPlan paymentPlan = om.readValue((jsonObject.toString()), PaymentPlan.class);
            if(paymentPlan.debt_id == id) {
                System.out.println(paymentPlan.id);
                return true;
            }
        }
       return false;
    }

    private static String getJsonString(String urlString) throws IOException {
        URL url = new URL(urlString);
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        scanner.close();
        System.out.println(inline);

        return inline;

    }

}
