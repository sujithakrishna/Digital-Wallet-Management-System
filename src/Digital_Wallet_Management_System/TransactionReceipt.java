package Digital_Wallet_Management_System;

public class TransactionReceipt {

    private String transactionId;
    private String dateTime;
    private String type;
    private String status;
    private String from;
    private String to;
    private double amount;
    private double availableBalance;

    public TransactionReceipt(String transactionId,
                              String dateTime,
                              String type,
                              String status,
                              String from,
                              String to,
                              double amount,
                              double availableBalance) {

        this.transactionId = transactionId;
        this.dateTime = dateTime;
        this.type = type;
        this.status = status;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.availableBalance = availableBalance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getAmount() {
        return amount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
