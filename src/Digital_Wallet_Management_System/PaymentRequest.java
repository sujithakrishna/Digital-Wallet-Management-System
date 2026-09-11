package Digital_Wallet_Management_System;

public class PaymentRequest {

    private String requestId;
    private String requesterUserId;
    private String payerUserId;
    private double amount;
    private String status;

    public PaymentRequest(String requestId,
                          String requesterUserId,
                          String payerUserId,
                          double amount,
                          String status) {

        this.requestId = requestId;
        this.requesterUserId = requesterUserId;
        this.payerUserId = payerUserId;
        this.amount = amount;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequesterUserId() {
        return requesterUserId;
    }

    public String getPayerUserId() {
        return payerUserId;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}