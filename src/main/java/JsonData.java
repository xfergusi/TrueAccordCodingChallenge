import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;

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
