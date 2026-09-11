package Digital_Wallet_Management_System;

public class ReceiptService {

    private static final WalletDAO walletDAO = new WalletDAO();
    private static final UserDAO userDAO = new UserDAO();

    private ReceiptService() {
        // Prevent object creation
    }

    public static TransactionReceipt createReceipt(
            Transaction transaction,
            User user) {

        if(transaction == null || user == null) {
            return null;
        }

        Wallet wallet =
                walletDAO.findWalletByUserId(
                        user.getUserId()
                );

        double availableBalance = 0.0;

        if(wallet != null) {
            availableBalance = wallet.getBalance();
        }

        String from = "N/A";
        String to = "N/A";

        if(transaction.getSenderUserId() != null) {

            User sender =
                    userDAO.findUserById(
                            transaction.getSenderUserId()
                    );

            if(sender != null) {
                from = sender.getName();
            }
        }

        if(transaction.getReceiverUserId() != null) {

            User receiver =
                    userDAO.findUserById(
                            transaction.getReceiverUserId()
                    );

            if(receiver != null) {
                to = receiver.getName();
            }
        }

        return new TransactionReceipt(
                transaction.getTransactionId(),
                TransactionStore.getTransactionDateTime(
                        transaction.getTransactionId()
                ),
                transaction.getType().name(),
                transaction.getStatus().name(),
                from,
                to,
                transaction.getAmount(),
                availableBalance
        );
    }

    public static void printReceipt(
            TransactionReceipt receipt) {

        if(receipt == null) {
            System.out.println("Unable to generate transaction receipt.");
            return;
        }

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                 TRANSACTION RECEIPT");
        System.out.println("========================================================");

        System.out.println(
                "Transaction ID : "
                + receipt.getTransactionId()
        );

        System.out.println(
                "Date & Time    : "
                + receipt.getDateTime()
        );

        System.out.println(
                "Type           : "
                + receipt.getType()
        );

        System.out.println(
                "Status         : "
                + receipt.getStatus()
        );

        System.out.println("--------------------------------------------------------");

        if(!receipt.getFrom().equals("N/A")) {
            System.out.println(
                    "From           : "
                    + receipt.getFrom()
            );
        }

        if(!receipt.getTo().equals("N/A")) {
            System.out.println(
                    "To             : "
                    + receipt.getTo()
            );
        }

        System.out.println(
                "Amount         : "
                + AmountFormatter.formatWithRupee(
                        receipt.getAmount()
                )
        );

        System.out.println(
                "Available Balance : "
                + AmountFormatter.formatWithRupee(
                        receipt.getAvailableBalance()
                )
        );

        System.out.println("========================================================");
    }
}
