import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private Booking booking;
    private double amount;
    private String method;
    private String status;
    private LocalDateTime transactionDate;

    public Payment(Booking booking) {
        this.booking = booking;
        this.paymentId = "PAY" + System.currentTimeMillis();
        this.amount = booking.calculateTotalPrice();
        this.status = "Pending";
        this.transactionDate = LocalDateTime.now();
    }

    public boolean processPayment() {
        // Simulate payment processing
        status = "Completed";
        return true;
    }

    public boolean validatePaymentDetails() {
        // Implement payment validation
        return true;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    // Getters
    public String getPaymentId() { return paymentId; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public LocalDateTime getTransactionDate() { return transactionDate; }

    // Setters
    public void setMethod(String method) { this.method = method; }
}
