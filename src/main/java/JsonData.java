import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;


/**
 * This class is used to gather and store the Json data from the three tables where data is stored.
 * With this class, we only have to talk to the API once, saving time.
 */
public class JsonData {

    public JSONArray paymentPlansJsonArray;
    public JSONArray paymentsJsonArray;
    public JSONArray DebtsJsonArray;

    public JsonData() throws IOException, ParseException {
        this.paymentPlansJsonArray = JSONTools.createJsonArray(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans");
        this.paymentsJsonArray = JSONTools.createJsonArray(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments");
        this.DebtsJsonArray = JSONTools.createJsonArray(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts");
    }

}
