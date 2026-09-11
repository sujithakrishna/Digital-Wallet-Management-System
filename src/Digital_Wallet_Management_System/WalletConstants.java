package Digital_Wallet_Management_System;

public class WalletConstants {

    private WalletConstants() {
        // Prevent object creation
    }

    public static final double MAX_TRANSFER_AMOUNT = 50000.00;

    public static final double DAILY_TRANSFER_LIMIT = 100000.00;

    public static final long TRANSACTION_CANCELLATION_WINDOW =
            5 * 60 * 1000;
}