package Logic;

import java.time.LocalDateTime;

public abstract class payment {
    protected String id;
    protected double amount;
    protected String paymentMethod;
    protected String status;
    protected LocalDateTime paymentDate;

    public payment(String id, double amount, String paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "Pending";
        this.paymentDate = null;
    }

    public abstract void pembayaran();

    public void displayPaymentInfo() {
        System.out.println("Payment ID: " + id);
        System.out.println("Amount: " + amount);
        System.out.println("Method: " + paymentMethod);
        System.out.println("Status: " + status);
        System.out.println("Date: " + (paymentDate != null ? paymentDate : "Not paid yet"));
    }
}
