package HW2;

public class Transfer {
    private String recipient;
    private float amount;

    public Transfer(String recipient, float amount) {
        this.recipient = recipient;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "To: " + recipient + ", Amount: $" + String.format("%.2f", amount);
    }
}
