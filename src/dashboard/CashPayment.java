package dashboard;

import java.time.LocalDateTime;

public class CashPayment extends payment {

    public CashPayment(String id, double amount) {
        super(id, amount, "Cash");
    }

    @Override
    public void pembayaran() {
        System.out.println("Memproses pembayaran CASH untuk ID: " + id);
        this.status = "Completed";
        this.paymentDate = LocalDateTime.now();
    }
}
