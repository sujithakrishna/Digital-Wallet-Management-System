package Digital_Wallet_Management_System;

import java.util.List;

public class TransactionStatisticsService {

    private TransactionStatisticsService() {
        // Prevent object creation
    }

    public static double getTotalDeposited(String userId) {

        double totalDeposited = 0.0;

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        for (Transaction transaction : transactions) {

            if (transaction.getType() == TransactionType.DEPOSIT
                    && transaction.getStatus() == TransactionStatus.SUCCESS) {

                totalDeposited += transaction.getAmount();
            }
        }

        return totalDeposited;
    }

    public static double getTotalWithdrawn(String userId) {

        double totalWithdrawn = 0.0;

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        for (Transaction transaction : transactions) {

            if (transaction.getType() == TransactionType.WITHDRAWAL
                    && transaction.getStatus() == TransactionStatus.SUCCESS) {

                totalWithdrawn += transaction.getAmount();
            }
        }

        return totalWithdrawn;
    }

    public static double getTotalTransferred(String userId) {

        double totalTransferred = 0.0;

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        for (Transaction transaction : transactions) {

            if (transaction.getType() == TransactionType.TRANSFER
                    && transaction.getStatus() == TransactionStatus.SUCCESS
                    && userId.equals(transaction.getSenderUserId())) {

                totalTransferred += transaction.getAmount();
            }
        }

        return totalTransferred;
    }

    public static double getTotalReceived(String userId) {

        double totalReceived = 0.0;

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        for (Transaction transaction : transactions) {

            if (transaction.getType() == TransactionType.TRANSFER
                    && transaction.getStatus() == TransactionStatus.SUCCESS
                    && userId.equals(transaction.getReceiverUserId())) {

                totalReceived += transaction.getAmount();
            }
        }

        return totalReceived;
    }

    public static int getSuccessfulTransactionCount(String userId) {

        int count = 0;

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        for (Transaction transaction : transactions) {

            if (transaction.getStatus() == TransactionStatus.SUCCESS) {
                count++;
            }
        }

        return count;
    }

    public static int getCancelledTransactionCount(String userId) {

        int count = 0;

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        for (Transaction transaction : transactions) {

            if (transaction.getStatus() == TransactionStatus.CANCELLED) {
                count++;
            }
        }

        return count;
    }

    public static double getAverageTransactionAmount(String userId) {

        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(userId);

        double totalAmount = 0.0;
        int successfulTransactionCount = 0;

        for (Transaction transaction : transactions) {

            if (transaction.getStatus() == TransactionStatus.SUCCESS) {

                totalAmount += transaction.getAmount();
                successfulTransactionCount++;
            }
        }

        if (successfulTransactionCount == 0) {
            return 0.0;
        }

        return totalAmount / successfulTransactionCount;
    }
}