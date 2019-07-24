package bbt.com.paypaldemo.model;

public class PaymentRequest {
    private String nonce = "";
    private double amount;

    public PaymentRequest(String nonce, double amount) {
        this.nonce = nonce;
        this.amount = amount;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
