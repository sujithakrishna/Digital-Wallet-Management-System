package Digital_Wallet_Management_System;

public class Wallet {

    private String walletId;
    private String userId;
    private double balance;
    private String status;

    public Wallet(String walletId, String userId) {

        this.walletId = walletId;
        this.userId = userId;
        this.balance = 0.0;
        this.status = "ACTIVE";
    }

    public String getWalletId() {
        return walletId;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}