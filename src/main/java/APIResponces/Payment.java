package APIResponces;

import java.time.LocalDateTime;

public class Payment {
      int id;
      int debt_id;
      float amount_to_pay;
      String installment_frequency;
      float installment_amount;
      LocalDateTime start_date;
}
