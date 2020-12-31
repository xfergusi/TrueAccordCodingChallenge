import java.io.IOException;

public class JsonData {
    public String paymentPlansJsonString;
    public String paymentsJsonString;

    public JsonData() throws IOException {
        paymentPlansJsonString = JSONTools.getJsonString(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans");
        paymentsJsonString = JSONTools.getJsonString(
                "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments");

    }
}
