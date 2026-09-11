package Digital_Wallet_Management_System;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionStore {

    private static List<Transaction> transactions =
            new ArrayList<>();

    private static Map<String, Long> transactionTimestamps =
            new HashMap<>();

    public static void addTransaction(Transaction transaction) {

        transactions.add(transaction);

        transactionTimestamps.put(
                transaction.getTransactionId(),
                System.currentTimeMillis()
        );
    }

    public static List<Transaction> getTransactionsByUserId(
            String userId) {

        List<Transaction> userTransactions =
                new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (userId.equals(transaction.getSenderUserId()) ||
                userId.equals(transaction.getReceiverUserId())) {

                userTransactions.add(transaction);
            }
        }

        return userTransactions;
    }

    public static Transaction getTransactionById(
            String transactionId) {

        for (Transaction transaction : transactions) {

            if (transaction.getTransactionId()
                    .equals(transactionId)) {

                return transaction;
            }
        }

        return null;
    }

    public static List<Transaction> getTransactionsByType(
            String userId,
            String type) {

        List<Transaction> matchingTransactions =
                new ArrayList<>();

        for (Transaction transaction :
                getTransactionsByUserId(userId)) {

            if (transaction.getType().name()
                    .equalsIgnoreCase(type)) {

                matchingTransactions.add(transaction);
            }
        }

        return matchingTransactions;
    }

    public static List<Transaction> getTransactionsByStatus(
            String userId,
            String status) {

        List<Transaction> matchingTransactions =
                new ArrayList<>();

        for (Transaction transaction :
                getTransactionsByUserId(userId)) {

            if (transaction.getStatus().name()
                    .equalsIgnoreCase(status)) {

                matchingTransactions.add(transaction);
            }
        }

        return matchingTransactions;
    }

    public static List<Transaction> getTransactionsByAmount(
            String userId,
            double amount) {

        List<Transaction> matchingTransactions =
                new ArrayList<>();

        for (Transaction transaction :
                getTransactionsByUserId(userId)) {

            if (transaction.getAmount() == amount) {

                matchingTransactions.add(transaction);
            }
        }

        return matchingTransactions;
    }

    // Calculate today's successful transfer amount
    // made by a particular user
    public static double getTodaysSuccessfulTransferAmount(
            String userId) {

        double totalAmount = 0.0;

        LocalDate today =
                LocalDate.now(ZoneId.systemDefault());

        for (Transaction transaction : transactions) {

            if (transaction.getType() != TransactionType.TRANSFER) {

                continue;
            }

            if (transaction.getStatus() != TransactionStatus.SUCCESS) {

                continue;
            }

            if (!userId.equals(
                    transaction.getSenderUserId())) {

                continue;
            }

            Long timestamp =
                    transactionTimestamps.get(
                            transaction.getTransactionId()
                    );

            if (timestamp == null) {
                continue;
            }

            LocalDate transactionDate =
                    java.time.Instant
                            .ofEpochMilli(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

            if (transactionDate.equals(today)) {

                totalAmount += transaction.getAmount();
            }
        }

        return totalAmount;
    }

    public static int getTransactionCount() {

        return transactions.size();
    }
 // ========================================================
 // GET FORMATTED TRANSACTION DATE & TIME
 // ========================================================

 public static String getTransactionDateTime(
         String transactionId)
 {
     Long timestamp =
             transactionTimestamps.get(transactionId);

     if(timestamp == null)
     {
         return "N/A";
     }

     DateTimeFormatter formatter =
             DateTimeFormatter.ofPattern(
                     "dd-MM-yyyy HH:mm:ss"
             );

     return Instant.ofEpochMilli(timestamp)
             .atZone(ZoneId.systemDefault())
             .format(formatter);
 }
//========================================================
//GET LATEST TRANSACTIONS FOR USER
//========================================================

public static List<Transaction> getLatestTransactions(
      String userId,
      int limit)
{
  List<Transaction> userTransactions =
          getTransactionsByUserId(userId);

  List<Transaction> latestTransactions =
          new ArrayList<>();


  // Start from the end because newer transactions
  // are added at the end of the list.

  for(int i = userTransactions.size() - 1;
      i >= 0 && latestTransactions.size() < limit;
      i--)
  {
      latestTransactions.add(
              userTransactions.get(i)
      );
  }


  return latestTransactions;
}
//========================================================
//GENERATE TRANSACTION ID
//========================================================

private static int transactionCounter = 1000;

public static String generateTransactionId()
{
 transactionCounter++;

 return "TXN" + transactionCounter;
}
public static long getTransactionTimestamp(String transactionId)
{
    Long timestamp = transactionTimestamps.get(transactionId);

    if(timestamp == null)
    {
        return 0;
    }

    return timestamp;
}
}