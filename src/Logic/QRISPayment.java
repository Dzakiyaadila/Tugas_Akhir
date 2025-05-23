package Logic;

import java.time.LocalDateTime;

public class QRISPayment extends payment {

    public QRISPayment(String id, double amount) {
        super(id, amount, "QRIS");
    }

    @Override
    public void pembayaran() {
        System.out.println("Memproses pembayaran QRIS untuk ID: " + id);
        this.status = "Completed";
        this.paymentDate = LocalDateTime.now();
    }
}
