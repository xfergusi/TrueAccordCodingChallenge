import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TrueAccordCodingChallengeTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void main() throws IOException, ParseException {
        String[] args = null;
        TrueAccordCodingChallenge.main(args);
        String answer =
                "{\"amount\":123.46,\"id\":0,\"is_in_payment_plan\":true,\"remaining_amount\":20.959999999999994,\"next_payment_due_date\":\"2020-11-05\"}\r\n" +
                        "{\"amount\":100.0,\"id\":1,\"is_in_payment_plan\":true,\"remaining_amount\":50.0,\"next_payment_due_date\":\"2020-08-15\"}\r\n" +
                        "{\"amount\":4920.34,\"id\":2,\"is_in_payment_plan\":true,\"remaining_amount\":607.6700000000001,\"next_payment_due_date\":\"2020-08-22\"}\r\n" +
                        "{\"amount\":12938.0,\"id\":3,\"is_in_payment_plan\":true,\"remaining_amount\":9247.745000000003,\"next_payment_due_date\":\"2020-08-08\"}\r\n" +
                        "{\"amount\":9238.02,\"id\":4,\"is_in_payment_plan\":false,\"remaining_amount\":9238.02,\"next_payment_due_date\":null}";
        assertEquals(answer, outputStreamCaptor.toString().trim());
    }
}