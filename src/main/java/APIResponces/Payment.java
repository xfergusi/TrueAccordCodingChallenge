package APIResponces;

import java.time.LocalDateTime;

public class Payment {
      public int id;
      public int debt_id;
      public float amount_to_pay;
      public String installment_frequency;
      public float installment_amount;
      public LocalDateTime start_date;
}
