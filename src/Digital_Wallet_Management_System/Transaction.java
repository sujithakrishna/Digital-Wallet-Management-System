package Digital_Wallet_Management_System;

public class Transaction {

    private String transactionId;
    private String senderUserId;
    private String receiverUserId;
    private TransactionType type;
    private double amount;
    private TransactionStatus status;

    public Transaction(String transactionId,
                       String senderUserId,
                       String receiverUserId,
                       TransactionType type,
                       double amount,
                       TransactionStatus status) {

        this.transactionId = transactionId;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.type = type;
        this.amount = amount;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
