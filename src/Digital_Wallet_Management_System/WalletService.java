package Digital_Wallet_Management_System;

import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

import Digital_Wallet_Management_System.exception.InvalidAmountException;
import Digital_Wallet_Management_System.exception.EmptyNameException;
import Digital_Wallet_Management_System.exception.InvalidEmailException;
import Digital_Wallet_Management_System.exception.InvalidMobileException;
import Digital_Wallet_Management_System.exception.PasswordMismatchException;

public class WalletService {

    private static final UserDAO userDAO = new UserDAO();
    private static final WalletDAO walletDAO = new WalletDAO();
    private static final BeneficiaryDAO beneficiaryDAO = new BeneficiaryDAO();

    // ========================================================
    // VIEW PROFILE
    // ========================================================

    public static void viewProfile(User user)
    {
        if(user == null)
        {
            System.out.println("Please login first.");
            return;
        }

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                     USER PROFILE");
        System.out.println("========================================================");

        System.out.println("User ID       : " + user.getUserId());
        System.out.println("Full Name     : " + user.getName());
        System.out.println("Email Address : " + user.getEmail());
        System.out.println("Mobile Number : " + user.getMobileNumber());

        System.out.println("--------------------------------------------------------");
    }


    // ========================================================
    // CHECK BALANCE
    // ========================================================

    public static void checkBalance(User user)
    {
        Wallet wallet =
                walletDAO.findWalletByUserId(user.getUserId());

        if(wallet == null)
        {
            System.out.println("Wallet not found.");
            return;
        }

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                    WALLET DETAILS");
        System.out.println("========================================================");

        System.out.println("Wallet ID : " + wallet.getWalletId());
        System.out.println("Balance   : " + AmountFormatter.formatWithRupee(wallet.getBalance()));
        System.out.println("Status    : " + wallet.getStatus());

        System.out.println("--------------------------------------------------------");
    }


    // ========================================================
    // DEPOSIT MONEY
    // ========================================================

    public static void depositMoney(User user, Scanner sc)
    {
        Wallet wallet =
                walletDAO.findWalletByUserId(user.getUserId());

        if(wallet == null)
        {
            System.out.println("Wallet not found.");
            return;
        }

        if(!"ACTIVE".equals(wallet.getStatus()))
        {
            System.out.println("Wallet is not active.");
            return;
        }

        while(true)
        {
            System.out.print("Enter deposit amount: ₹");

            String input = sc.nextLine();

            try
            {
                double amount = Double.parseDouble(input);

                validateAmount(amount);

                double newBalance =
                        wallet.getBalance() + amount;

                wallet.setBalance(newBalance);

                if (!walletDAO.updateWallet(wallet)) {
                    System.out.println("Unable to update wallet balance in database.");
                    return;
                }


                // CREATE TRANSACTION

                String transactionId =
                        TransactionStore.generateTransactionId();

                Transaction transaction = new Transaction(
                        transactionId,
                        null,
                        user.getUserId(),
                        TransactionType.DEPOSIT,
                        amount,
                        TransactionStatus.SUCCESS
                );

                TransactionStore.addTransaction(transaction);

                AuditLogService.log(
                        user.getUserId(),
                        "DEPOSIT",
                        "Deposited "
                        + AmountFormatter.formatWithRupee(amount)
                        + ". Transaction ID: "
                        + transactionId
                );


                System.out.println();
                System.out.println("Deposit successful!");
                System.out.println("Transaction ID   : " + transactionId);
                System.out.println("Deposited Amount : " + AmountFormatter.formatWithRupee(amount));
                System.out.println("New Balance      : " + AmountFormatter.formatWithRupee(newBalance));
                TransactionReceipt receipt =
                        ReceiptService.createReceipt(
                                transaction,
                                user
                        );

                ReceiptService.printReceipt(receipt);

                break;
            }
            catch(NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
            catch(InvalidAmountException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    // ========================================================
    // WITHDRAW MONEY
    // ========================================================

    public static void withdrawMoney(User user, Scanner sc)
    {
        Wallet wallet =
                walletDAO.findWalletByUserId(user.getUserId());

        if(wallet == null)
        {
            System.out.println("Wallet not found.");
            return;
        }

        if(!"ACTIVE".equals(wallet.getStatus()))
        {
            System.out.println("Wallet is not active.");
            return;
        }

        while(true)
        {
            System.out.print("Enter withdrawal amount: ₹");

            String input = sc.nextLine();

            try
            {
                double amount = Double.parseDouble(input);

                validateAmount(amount);

                if(amount > wallet.getBalance())
                {
                    System.out.println("Insufficient balance.");
                    System.out.println(
                            "Available Balance: "
                            + AmountFormatter.formatWithRupee(wallet.getBalance())
                    );

                    continue;
                }

                double newBalance =
                        wallet.getBalance() - amount;

                wallet.setBalance(newBalance);

                if (!walletDAO.updateWallet(wallet)) {
                    System.out.println("Unable to update wallet balance in database.");
                    return;
                }


                // CREATE TRANSACTION

                String transactionId =
                        TransactionStore.generateTransactionId();

                Transaction transaction = new Transaction(
                        transactionId,
                        user.getUserId(),
                        null,
                        TransactionType.WITHDRAWAL,
                        amount,
                        TransactionStatus.SUCCESS
                );

                TransactionStore.addTransaction(transaction);

                AuditLogService.log(
                        user.getUserId(),
                        "WITHDRAWAL",
                        "Withdrew "
                        + AmountFormatter.formatWithRupee(amount)
                        + ". Transaction ID: "
                        + transactionId
                );


                System.out.println();
                System.out.println("Withdrawal successful!");
                System.out.println("Withdrawn Amount : " + AmountFormatter.formatWithRupee(amount));
                System.out.println("Transaction ID   : " + transactionId);
                System.out.println("New Balance      : " + AmountFormatter.formatWithRupee(newBalance));
                TransactionReceipt receipt =
                        ReceiptService.createReceipt(
                                transaction,
                                user
                        );

                ReceiptService.printReceipt(receipt);

                break;
            }
            catch(NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
            catch(InvalidAmountException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


 // ========================================================
 // TRANSFER MONEY
 // ========================================================

 public static void transferMoney(User sender, Scanner sc)
 {
     Wallet senderWallet =
             walletDAO.findWalletByUserId(sender.getUserId());

     if(senderWallet == null)
     {
         System.out.println("Sender wallet not found.");
         return;
     }


     // ====================================================
     // CHECK SENDER WALLET
     // ====================================================

     if(!"ACTIVE".equals(senderWallet.getStatus()))
     {
         System.out.println("Your wallet is not active.");
         return;
     }


     // ====================================================
     // SELECT BENEFICIARY
     // ====================================================

     List<Beneficiary> beneficiaries;

     try
     {
         beneficiaries =
                 beneficiaryDAO.findByUserId(sender.getUserId());
     }
     catch(SQLException e)
     {
         System.out.println("Unable to load beneficiaries.");
         System.out.println("Database error: " + e.getMessage());
         return;
     }

     if(beneficiaries.isEmpty())
     {
         System.out.println();
         System.out.println("No beneficiaries found.");
         System.out.println("Please add a beneficiary before making a transfer.");
         return;
     }

     System.out.println();
     System.out.println("AVAILABLE BENEFICIARIES");
     System.out.println("------------------------------------------------");

     for(int i = 0; i < beneficiaries.size(); i++)
     {
         Beneficiary beneficiary = beneficiaries.get(i);

         System.out.println(
                 (i + 1) + ". "
                 + beneficiary.getBeneficiaryName()
                 + " | "
                 + beneficiary.getBeneficiaryEmail()
                 + " | ID: "
                 + beneficiary.getBeneficiaryId()
         );
     }

     System.out.println("------------------------------------------------");
     System.out.print("Select beneficiary: ");

     String beneficiaryChoice = sc.nextLine().trim();

     int beneficiaryIndex;

     try
     {
         beneficiaryIndex = Integer.parseInt(beneficiaryChoice) - 1;
     }
     catch(NumberFormatException e)
     {
         System.out.println("Please enter a valid beneficiary number.");
         return;
     }

     if(beneficiaryIndex < 0 || beneficiaryIndex >= beneficiaries.size())
     {
         System.out.println("Invalid beneficiary selection.");
         return;
     }

     Beneficiary selectedBeneficiary =
             beneficiaries.get(beneficiaryIndex);

     User receiver;

     try
     {
         receiver =
                 userDAO.findUserById(
                         selectedBeneficiary.getBeneficiaryUserId()
                 );
     }
     catch(Exception e)
     {
         System.out.println("Unable to load beneficiary account.");
         System.out.println("Database error: " + e.getMessage());
         return;
     }

     if(receiver == null)
     {
         System.out.println("Beneficiary account no longer exists.");
         return;
     }

     System.out.println();
     System.out.println("Transfer To       : " + receiver.getName());
     System.out.println("Beneficiary Email : " + receiver.getEmail());
     System.out.println("Beneficiary ID    : " + selectedBeneficiary.getBeneficiaryId());


     // ====================================================
     // PREVENT SELF TRANSFER
     // ====================================================

     if(receiver.getUserId().equals(sender.getUserId()))
     {
         System.out.println(
                 "You cannot transfer money to yourself."
         );

         return;
     }


     // ====================================================
     // GET RECEIVER WALLET
     // ====================================================

     Wallet receiverWallet =
             walletDAO.findWalletByUserId(
                     receiver.getUserId()
             );


     if(receiverWallet == null)
     {
         System.out.println("Receiver wallet not found.");
         return;
     }


     // ====================================================
     // CHECK RECEIVER WALLET
     // ====================================================

     if(!"ACTIVE".equals(receiverWallet.getStatus()))
     {
         System.out.println(
                 "Receiver wallet is not active."
         );

         return;
     }


     // ====================================================
     // ENTER AMOUNT
     // ====================================================

     while(true)
     {
         System.out.print("Enter transfer amount: ₹");

         String input = sc.nextLine();

         try
         {
             double amount =
                     Double.parseDouble(input);

             validateAmount(amount);

             // ====================================================
             // IDEMPOTENCY KEY
             // ====================================================

             System.out.print("Enter transfer request key: ");

             String requestKey =
                     sc.nextLine().trim();

             if(requestKey.isEmpty())
             {
                 System.out.println(
                         "Request key cannot be empty."
                 );

                 continue;
             }

             if(IdempotencyService.isAlreadyProcessed(
                     requestKey))
             {
                 String existingTransactionId =
                         IdempotencyService.getExistingTransactionId(
                                 requestKey
                         );

                 System.out.println();
                 System.out.println(
                         "This transfer request has already "
                         + "been processed."
                 );

                 System.out.println(
                         "Existing Transaction ID : "
                         + existingTransactionId
                 );

                 return;
             }



             // ====================================================
             // MAXIMUM SINGLE TRANSFER LIMIT
             // ====================================================

             if(amount > WalletConstants.MAX_TRANSFER_AMOUNT)
             {
                 System.out.println();

                 System.out.println(
                         "Maximum transfer limit is "
                         + AmountFormatter.formatWithRupee(WalletConstants.MAX_TRANSFER_AMOUNT)
                 );

                 System.out.println(
                         "Please enter an amount within the allowed limit."
                 );

                 continue;
             }


             // ====================================================
             // DAILY TRANSFER LIMIT
             // ====================================================

             double todaysTransferAmount =
                     TransactionStore.getTodaysSuccessfulTransferAmount(
                             sender.getUserId()
                     );


             double remainingDailyLimit =
                     WalletConstants.DAILY_TRANSFER_LIMIT
                     - todaysTransferAmount;


             System.out.println();

             System.out.println(
                     "Daily transfer limit: "
                     + AmountFormatter.formatWithRupee(WalletConstants.DAILY_TRANSFER_LIMIT)
             );

             System.out.println(
                     "Transferred today: "
                     + AmountFormatter.formatWithRupee(todaysTransferAmount)
             );

             System.out.println(
                     "Remaining daily limit: "
                     + AmountFormatter.formatWithRupee(remainingDailyLimit)
             );


             if(amount > remainingDailyLimit)
             {
                 System.out.println();

                 System.out.println(
                         "Daily transfer limit exceeded."
                 );

                 System.out.println(
                         "You can transfer only "
                         + AmountFormatter.formatWithRupee(remainingDailyLimit)
                         + " more today."
                 );

                 continue;
             }


             // ====================================================
             // BALANCE CHECK
             // ====================================================

             if(amount > senderWallet.getBalance())
             {
                 System.out.println();

                 System.out.println(
                         "Insufficient balance."
                 );

                 System.out.println(
                         "Available Balance: "
                         + AmountFormatter.formatWithRupee(senderWallet.getBalance())
                 );

                 continue;
             }


             // ====================================================
             // TRANSFER CONFIRMATION
             // ====================================================

             System.out.println();

             System.out.println(
                     "================================================"
             );

             System.out.println(
                     "             TRANSFER CONFIRMATION"
             );

             System.out.println(
                     "================================================"
             );

             System.out.println(
                     "Receiver : "
                     + receiver.getName()
             );

             System.out.println(
                     "Email    : "
                     + receiver.getEmail()
             );

             System.out.println(
                     "Amount   : "
                     + AmountFormatter.formatWithRupee(amount)
             );

             System.out.println(
                     "================================================"
             );

             System.out.println("1. Confirm Transfer");
             System.out.println("2. Cancel Transfer");

             System.out.print("Enter your choice: ");

             String confirmation =
                     sc.nextLine();


             // ====================================================
             // CANCEL TRANSFER
             // ====================================================

             if(confirmation.equals("2"))
             {
                 System.out.println();

                 System.out.println(
                         "Transfer cancelled."
                 );

                 return;
             }


             // ====================================================
             // INVALID CONFIRMATION
             // ====================================================

             if(!confirmation.equals("1"))
             {
                 System.out.println();

                 System.out.println(
                         "Invalid choice. Transfer cancelled."
                 );

                 return;
             }


             // ====================================================
             // DEBIT SENDER
             // ====================================================

             double senderNewBalance =
                     senderWallet.getBalance()
                     - amount;

             senderWallet.setBalance(
                     senderNewBalance
             );

             // Persist sender balance to MySQL
             if (!walletDAO.updateWallet(senderWallet)) {
                 System.out.println("Unable to update sender wallet in database.");
                 return;
             }


             // ====================================================
             // CREDIT RECEIVER
             // ====================================================

             double receiverNewBalance =
                     receiverWallet.getBalance()
                     + amount;

             receiverWallet.setBalance(
                     receiverNewBalance
             );

             // Persist receiver balance to MySQL
             if (!walletDAO.updateWallet(receiverWallet)) {
                 System.out.println("Unable to update receiver wallet in database.");
                 return;
             }


             // ====================================================
             // CREATE TRANSACTION
             // ====================================================

             String transactionId =
            	        TransactionStore.generateTransactionId();


             Transaction transaction =
                     new Transaction(
                             transactionId,
                             sender.getUserId(),
                             receiver.getUserId(),
                             TransactionType.TRANSFER,
                             amount,
                             TransactionStatus.SUCCESS
                     );


             TransactionStore.addTransaction(
                     transaction
             );

             // ====================================================
             // SAVE IDEMPOTENCY RECORD
             // ====================================================

             IdempotencyService.recordProcessedRequest(
                     requestKey,
                     transactionId
             );



             AuditLogService.log(
                     sender.getUserId(),
                     "TRANSFER",
                     "Transferred "
                     + AmountFormatter.formatWithRupee(amount)
                     + " to "
                     + receiver.getName()
                     + ". Transaction ID: "
                     + transactionId
             );


             // ====================================================
             // SUCCESS MESSAGE
             // ====================================================

             System.out.println();

             System.out.println(
                     "========================================================"
             );

             System.out.println(
                     "              TRANSFER SUCCESSFUL"
             );

             System.out.println(
                     "========================================================"
             );

             System.out.println(
                     "Transaction ID : "
                     + transactionId
             );

             System.out.println(
                     "Transferred    : "
                     + AmountFormatter.formatWithRupee(amount)
             );

             System.out.println(
                     "Receiver       : "
                     + receiver.getName()
             );

             System.out.println(
                     "New Balance    : "
                     + AmountFormatter.formatWithRupee(senderNewBalance)
             );

             System.out.println(
                     "--------------------------------------------------------"
             );
             
             TransactionReceipt receipt =
            	        ReceiptService.createReceipt(
            	                transaction,
            	                sender
            	        );

            	ReceiptService.printReceipt(receipt);
             break;
         }
         catch(NumberFormatException e)
         {
             System.out.println(
                     "Please enter a valid number."
             );
         }
         catch(InvalidAmountException e)
         {
             System.out.println(
                     e.getMessage()
             );
         }
     }
 }

    // ========================================================
    // TRANSACTION HISTORY
    // ========================================================

    public static void transactionHistory(User user)
    {
        List<Transaction> transactions =
                TransactionStore.getTransactionsByUserId(
                        user.getUserId()
                );

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                  TRANSACTION HISTORY");
        System.out.println("========================================================");

        if(transactions.isEmpty())
        {
            System.out.println("No transactions found.");
            System.out.println("--------------------------------------------------------");
            return;
        }


        for(Transaction transaction : transactions)
        {
            System.out.println();

            System.out.println(
                    "Transaction ID : "
                    + transaction.getTransactionId()
            );

            System.out.println(
                    "Type           : "
                    + getTransactionType(transaction, user)
            );

            System.out.println(
                    "Amount         : "
                    + AmountFormatter.formatWithRupee(transaction.getAmount())
            );
            
            System.out.println(
                    "Date & Time    : "
                    + TransactionStore.getTransactionDateTime(
                            transaction.getTransactionId()
                    )
            );

            if(transaction.getSenderUserId() != null &&
               transaction.getReceiverUserId() != null)
            {
                if(transaction.getSenderUserId().equals(user.getUserId()))
                {
                    User receiver =
                            userDAO.findUserById(
                                    transaction.getReceiverUserId()
                            );

                    if(receiver != null)
                    {
                        System.out.println(
                                "To             : "
                                + receiver.getName()
                        );
                    }
                }
                else
                {
                    User sender =
                            userDAO.findUserById(
                                    transaction.getSenderUserId()
                            );

                    if(sender != null)
                    {
                        System.out.println(
                                "From           : "
                                + sender.getName()
                        );
                    }
                }
            }


            System.out.println(
                    "Status         : "
                    + transaction.getStatus()
            );

            System.out.println(
                    "--------------------------------------------------------"
            );
        }
    }


    // ========================================================
    // TRANSACTION TYPE
    // ========================================================

    private static String getTransactionType(
            Transaction transaction,
            User user)
    {
        if(transaction.getType() == TransactionType.DEPOSIT)
        {
            return "DEPOSIT";
        }

        if(transaction.getType() == TransactionType.WITHDRAWAL)
        {
            return "WITHDRAWAL";
        }

        if(transaction.getType() == TransactionType.TRANSFER)
        {
            if(transaction.getSenderUserId().equals(user.getUserId()))
            {
                return "TRANSFER SENT";
            }
            else
            {
                return "TRANSFER RECEIVED";
            }
        }

        return transaction.getType().name();
    }


    // ========================================================
    // EDIT PROFILE
    // ========================================================

    public static void editProfile(User user, Scanner sc)
    {
        System.out.println();
        System.out.println("================================================");
        System.out.println("                 EDIT PROFILE");
        System.out.println("================================================");

        // ====================================================
        // NAME VALIDATION
        // ====================================================

        System.out.println("Current Name: " + user.getName());

        System.out.print("Enter new name: ");
        String newName = sc.nextLine();

        try
        {
            Registeruser.validatename(newName);
            newName = newName.trim();
        }
        catch(EmptyNameException e)
        {
            System.out.println(e.getMessage());
            return;
        }


        // ====================================================
        // EMAIL VALIDATION
        // ====================================================

        System.out.println("Current Email: " + user.getEmail());

        System.out.print("Enter new email: ");
        String newEmail = sc.nextLine();

        try
        {
            Registeruser.validateemail(newEmail);
            newEmail = newEmail.trim().toLowerCase();
        }
        catch(InvalidEmailException e)
        {
            System.out.println(e.getMessage());
            return;
        }


        // ====================================================
        // DUPLICATE EMAIL CHECK
        // ====================================================

        if(!newEmail.equalsIgnoreCase(user.getEmail())
                && userDAO.findUserByEmail(newEmail) != null)
        {
            System.out.println(
                    "Email already exists. Please use another email."
            );

            return;
        }


        // ====================================================
        // MOBILE NUMBER VALIDATION
        // ====================================================

        System.out.println(
                "Current Mobile Number: "
                + user.getMobileNumber()
        );

        System.out.print("Enter new mobile number: ");
        String newMobile = sc.nextLine();

        try
        {
            Registeruser.validatemobile(newMobile);
            newMobile = newMobile.trim();
        }
        catch(InvalidMobileException e)
        {
            System.out.println(e.getMessage());
            return;
        }


        // ====================================================
        // UPDATE PROFILE
        // ====================================================

        user.setName(newName);
        user.setMobileNumber(newMobile);

        if(!newEmail.equals(user.getEmail()))
        {
            user.setEmail(newEmail);
        }

        if(!userDAO.updateUser(user))
        {
            System.out.println("Unable to update profile in database.");
            return;
        }


        AuditLogService.log(
                user.getUserId(),
                "PROFILE_UPDATED",
                "User profile details were updated"
        );

        System.out.println();
        System.out.println("Profile updated successfully!");
    }


 // ========================================================
 // CHANGE PASSWORD
 // ========================================================

 public static void changePassword(User user, Scanner sc)
 {
     System.out.println();
     System.out.println("================================================");
     System.out.println("                 CHANGE PASSWORD");
     System.out.println("================================================");

     // ====================================================
     // VERIFY CURRENT PASSWORD
     // ====================================================

     System.out.print("Enter current password: ");

     String currentPassword = sc.nextLine();

     if(!user.getPassword().equals(currentPassword))
     {
         System.out.println("Incorrect current password.");
         return;
     }


     // ====================================================
     // GET NEW PASSWORD
     // ====================================================

     System.out.print("Enter new password: ");

     String newPassword = sc.nextLine();


     // ====================================================
     // CHECK NEW PASSWORD IS DIFFERENT
     // ====================================================

     if(newPassword.equals(currentPassword))
     {
         System.out.println(
                 "New password must be different from current password."
         );

         return;
     }


     // ====================================================
     // CONFIRM NEW PASSWORD
     // ====================================================

     System.out.print("Confirm new password: ");

     String confirmPassword = sc.nextLine();


     // ====================================================
     // STRONG PASSWORD VALIDATION
     // ====================================================

     try
     {
         Registeruser.validatepassword(
                 newPassword,
                 confirmPassword
         );
     }
     catch(PasswordMismatchException e)
     {
         System.out.println(e.getMessage());
         return;
     }


     // ====================================================
     // UPDATE PASSWORD
     // ====================================================

     user.setPassword(newPassword);

     if(!userDAO.updateUser(user))
     {
         System.out.println("Unable to update password in database.");
         return;
     }

     AuditLogService.log(
             user.getUserId(),
             "PASSWORD_CHANGED",
             "User password changed successfully"
     );

     System.out.println();
     System.out.println("Password changed successfully!");
 }

    // ========================================================
    // LOCK WALLET
    // ========================================================

    public static void lockWallet(User user)
    {
        Wallet wallet =
                walletDAO.findWalletByUserId(user.getUserId());

        if(wallet == null)
        {
            System.out.println("Wallet not found.");
            return;
        }


        if("LOCKED".equals(wallet.getStatus()))
        {
            System.out.println("Wallet is already locked.");
            return;
        }


        wallet.setStatus("LOCKED");

        if (!walletDAO.updateWallet(wallet)) {
            System.out.println("Unable to lock wallet in database.");
            return;
        }

        AuditLogService.log(
                user.getUserId(),
                "WALLET_LOCKED",
                "Wallet was locked by the user"
        );

        System.out.println();
        System.out.println("Wallet locked successfully.");
    }


    // ========================================================
    // UNLOCK WALLET
    // ========================================================

    public static void unlockWallet(User user, Scanner sc)
    {
        Wallet wallet =
                walletDAO.findWalletByUserId(user.getUserId());

        if(wallet == null)
        {
            System.out.println("Wallet not found.");
            return;
        }


        if("ACTIVE".equals(wallet.getStatus()))
        {
            System.out.println("Wallet is already active.");
            return;
        }


        System.out.print("Enter your password: ");

        String password = sc.nextLine();

        if(!user.getPassword().equals(password))
        {
            System.out.println("Incorrect password.");
            return;
        }


        wallet.setStatus("ACTIVE");

        if (!walletDAO.updateWallet(wallet)) {
            System.out.println("Unable to unlock wallet in database.");
            return;
        }

        AuditLogService.log(
                user.getUserId(),
                "WALLET_UNLOCKED",
                "Wallet was unlocked successfully"
        );

        System.out.println();
        System.out.println("Wallet unlocked successfully.");
    }


    // ========================================================
    // ADD BENEFICIARY
    // ========================================================

    public static void addBeneficiary(User user, Scanner sc)
    {
        System.out.println();
        System.out.println("================================================");
        System.out.println("              ADD BENEFICIARY");
        System.out.println("================================================");

        System.out.print("Enter beneficiary email: ");

        String email = sc.nextLine().trim().toLowerCase();

        User beneficiaryUser =
                userDAO.findUserByEmail(email);


        if(beneficiaryUser == null)
        {
            System.out.println("User not found.");
            return;
        }


        if(beneficiaryUser.getUserId().equals(user.getUserId()))
        {
            System.out.println(
                    "You cannot add yourself as a beneficiary."
            );

            return;
        }


        try
        {
            if(beneficiaryDAO.exists(
                    user.getUserId(),
                    beneficiaryUser.getUserId()))
            {
                System.out.println("Beneficiary already exists.");
                return;
            }
        }
        catch(SQLException e)
        {
            System.out.println("Unable to check beneficiary.");
            System.out.println("Database error: " + e.getMessage());
            return;
        }


        String beneficiaryId =
                beneficiaryDAO.generateBeneficiaryId();


        Beneficiary beneficiary =
                new Beneficiary(
                        beneficiaryId,
                        user.getUserId(),
                        beneficiaryUser.getUserId(),
                        beneficiaryUser.getName(),
                        beneficiaryUser.getEmail()
                );


        try
        {
            beneficiaryDAO.save(beneficiary);
        }
        catch(SQLException e)
        {
            System.out.println("Unable to save beneficiary.");
            System.out.println("Database error: " + e.getMessage());
            return;
        }

        AuditLogService.log(
                user.getUserId(),
                "BENEFICIARY_ADDED",
                "Added beneficiary "
                + beneficiaryUser.getName()
                + " (" + beneficiaryId + ")"
        );

        System.out.println();
        System.out.println("Beneficiary added successfully.");

        System.out.println(
                "Beneficiary ID : " + beneficiaryId
        );

        System.out.println(
                "Name           : "
                + beneficiaryUser.getName()
        );

        System.out.println(
                "Email          : "
                + beneficiaryUser.getEmail()
        );
    }


    // ========================================================
    // VIEW BENEFICIARIES
    // ========================================================

    public static void viewBeneficiaries(User user)
    {
        System.out.println();
        System.out.println("================================================");
        System.out.println("             MY BENEFICIARIES");
        System.out.println("================================================");


        List<Beneficiary> beneficiaries;

        try
        {
            beneficiaries =
                    beneficiaryDAO.findByUserId(
                            user.getUserId()
                    );
        }
        catch(SQLException e)
        {
            System.out.println("Unable to load beneficiaries.");
            System.out.println("Database error: " + e.getMessage());
            return;
        }


        if(beneficiaries.isEmpty())
        {
            System.out.println("No beneficiaries found.");
            return;
        }


        for(Beneficiary beneficiary : beneficiaries)
        {
            System.out.println();

            System.out.println(
                    "Beneficiary ID : "
                    + beneficiary.getBeneficiaryId()
            );

            System.out.println(
                    "Name           : "
                    + beneficiary.getBeneficiaryName()
            );

            System.out.println(
                    "Email          : "
                    + beneficiary.getBeneficiaryEmail()
            );

            System.out.println(
                    "------------------------------------------------"
            );
        }
    }


    // ========================================================
    // REMOVE BENEFICIARY
    // ========================================================

    public static void removeBeneficiary(User user, Scanner sc)
    {
        System.out.println();
        System.out.println("================================================");
        System.out.println("            REMOVE BENEFICIARY");
        System.out.println("================================================");

        System.out.print("Enter beneficiary ID: ");

        String beneficiaryId = sc.nextLine().trim();


        Beneficiary beneficiary;

        try
        {
            beneficiary =
                    beneficiaryDAO.findById(beneficiaryId);
        }
        catch(SQLException e)
        {
            System.out.println("Unable to load beneficiary.");
            System.out.println("Database error: " + e.getMessage());
            return;
        }


        if(beneficiary == null)
        {
            System.out.println("Beneficiary not found.");
            return;
        }


        if(!beneficiary.getUserId().equals(user.getUserId()))
        {
            System.out.println("Beneficiary not found.");
            return;
        }


        System.out.println();

        System.out.println(
                "Beneficiary: "
                + beneficiary.getBeneficiaryName()
        );

        System.out.print(
                "Are you sure you want to remove this beneficiary? (Y/N): "
        );

        String confirmation = sc.nextLine();


        if(!confirmation.equalsIgnoreCase("Y"))
        {
            System.out.println(
                    "Beneficiary removal cancelled."
            );

            return;
        }


        try
        {
            beneficiaryDAO.delete(beneficiaryId);
        }
        catch(SQLException e)
        {
            System.out.println("Unable to remove beneficiary.");
            System.out.println("Database error: " + e.getMessage());
            return;
        }

        AuditLogService.log(
                user.getUserId(),
                "BENEFICIARY_REMOVED",
                "Removed beneficiary "
                + beneficiary.getBeneficiaryName()
                + " (" + beneficiaryId + ")"
        );

        System.out.println(
                "Beneficiary removed successfully."
        );
    }


    // ========================================================
    // TRANSACTION DETAILS
    // ========================================================

    public static void transactionDetails(
            User user,
            Scanner sc)
    {
        System.out.println();
        System.out.println("================================================");
        System.out.println("             TRANSACTION DETAILS");
        System.out.println("================================================");

        System.out.print("Enter transaction ID: ");

        String transactionId = sc.nextLine().trim();


        Transaction transaction =
                TransactionStore.getTransactionById(
                        transactionId
                );


        if(transaction == null)
        {
            System.out.println("Transaction not found.");
            return;
        }


        boolean belongsToUser =
                user.getUserId().equals(
                        transaction.getSenderUserId())
                ||
                user.getUserId().equals(
                        transaction.getReceiverUserId());


        if(!belongsToUser)
        {
            System.out.println("Transaction not found.");
            return;
        }


        System.out.println();

        System.out.println(
                "Transaction ID : "
                + transaction.getTransactionId()
        );

        System.out.println(
                "Type           : "
                + getTransactionType(transaction, user)
        );

        System.out.println(
                "Amount         : "
                + AmountFormatter.formatWithRupee(transaction.getAmount())
        );

        System.out.println(
                "Status         : "
                + transaction.getStatus()
        );


        if(transaction.getSenderUserId() != null)
        {
            User sender =
                    userDAO.findUserById(
                            transaction.getSenderUserId()
                    );

            if(sender != null)
            {
                System.out.println(
                        "Sender         : "
                        + sender.getName()
                );
            }
        }


        if(transaction.getReceiverUserId() != null)
        {
            User receiver =
                    userDAO.findUserById(
                            transaction.getReceiverUserId()
                    );

            if(receiver != null)
            {
                System.out.println(
                        "Receiver       : "
                        + receiver.getName()
                );
            }
        }


        System.out.println(
                "------------------------------------------------"
        );
    }


    // ========================================================
    // SEARCH TRANSACTIONS
    // ========================================================

    public static void searchTransactions(
            User user,
            Scanner sc)
    {
        while(true)
        {
            System.out.println();
            System.out.println("================================================");
            System.out.println("             SEARCH TRANSACTIONS");
            System.out.println("================================================");

            System.out.println("1. Search by Transaction ID");
            System.out.println("2. Search by Type");
            System.out.println("3. Search by Status");
            System.out.println("4. Search by Amount");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();


            switch(choice)
            {
                case "1":

                    System.out.print("Enter transaction ID: ");

                    String transactionId =
                            sc.nextLine();

                    Transaction transaction =
                            TransactionStore.getTransactionById(
                                    transactionId
                            );

                    if(transaction == null)
                    {
                        System.out.println(
                                "Transaction not found."
                        );
                    }
                    else
                    {
                        boolean belongsToUser =
                                user.getUserId().equals(
                                        transaction.getSenderUserId())
                                ||
                                user.getUserId().equals(
                                        transaction.getReceiverUserId());

                        if(!belongsToUser)
                        {
                            System.out.println(
                                    "Transaction not found."
                            );
                        }
                        else
                        {
                            displaySearchResults(
                                    List.of(transaction),
                                    user
                            );
                        }
                    }

                    break;


                case "2":

                    System.out.print(
                            "Enter transaction type: "
                    );

                    String type =
                            sc.nextLine().trim();

                    displaySearchResults(
                            TransactionStore.getTransactionsByType(
                                    user.getUserId(),
                                    type
                            ),
                            user
                    );

                    break;


                case "3":

                    System.out.print(
                            "Enter transaction status: "
                    );

                    String status =
                            sc.nextLine().trim();

                    displaySearchResults(
                            TransactionStore.getTransactionsByStatus(
                                    user.getUserId(),
                                    status
                            ),
                            user
                    );

                    break;


                case "4":

                    System.out.print(
                            "Enter transaction amount: ₹"
                    );

                    String amountInput =
                            sc.nextLine();

                    try
                    {
                        double amount =
                                Double.parseDouble(
                                        amountInput
                                );

                        validateAmount(amount);

              // ====================================================
              // IDEMPOTENCY KEY
              // ====================================================

              System.out.print("Enter transfer request key: ");

              String requestKey =
                      sc.nextLine().trim();

              if(requestKey.isEmpty())
              {
                  System.out.println(
                          "Request key cannot be empty."
                  );

                  continue;
              }

              if(IdempotencyService.isAlreadyProcessed(
                      requestKey))
              {
                  String existingTransactionId =
                          IdempotencyService
                                  .getExistingTransactionId(
                                          requestKey
                                  );

                  System.out.println();

                  System.out.println(
                          "This transfer request has already "
                          + "been processed."
                  );

                  System.out.println(
                          "Existing Transaction ID : "
                          + existingTransactionId
                  );

                  return;
              }


                        displaySearchResults(
                                TransactionStore.getTransactionsByAmount(
                                        user.getUserId(),
                                        amount
                                ),
                                user
                        );
                    }
                    catch(NumberFormatException e)
                    {
                        System.out.println(
                                "Please enter a valid amount."
                        );
                    }
                    catch(InvalidAmountException e)
                    {
                        System.out.println(e.getMessage());
                    }

                    break;


                case "5":

                    return;


                default:

                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }


    // ========================================================
    // DISPLAY SEARCH RESULTS
    // ========================================================

    private static void displaySearchResults(
            List<Transaction> transactions,
            User user)
    {
        System.out.println();

        if(transactions == null ||
           transactions.isEmpty())
        {
            System.out.println(
                    "No matching transactions found."
            );

            return;
        }


        System.out.println(
                "Matching Transactions: "
                + transactions.size()
        );


        for(Transaction transaction : transactions)
        {
            System.out.println();

            System.out.println(
                    "Transaction ID : "
                    + transaction.getTransactionId()
            );

            System.out.println(
                    "Type           : "
                    + getTransactionType(transaction, user)
            );

            System.out.println(
                    "Amount         : "
                    + AmountFormatter.formatWithRupee(transaction.getAmount())
            );
            System.out.println(
                    "Date & Time    : "
                    + TransactionStore.getTransactionDateTime(
                            transaction.getTransactionId()
                    )
            );

            System.out.println(
                    "Status         : "
                    + transaction.getStatus()
            );

            System.out.println(
                    "------------------------------------------------"
            );
        }
    }
 // ========================================================
 // CREATE PAYMENT REQUEST
 // ========================================================

 public static void createPaymentRequest(User requester, Scanner sc)
 {
     System.out.println();
     System.out.println("================================================");
     System.out.println("             CREATE PAYMENT REQUEST");
     System.out.println("================================================");

     System.out.print("Enter payer email: ");

     String payerEmail = sc.nextLine().trim().toLowerCase();

     User payer =
             userDAO.findUserByEmail(payerEmail);


     // CHECK PAYER EXISTS

     if(payer == null)
     {
         System.out.println("User not found.");
         return;
     }


     // PREVENT SELF REQUEST

     if(payer.getUserId().equals(requester.getUserId()))
     {
         System.out.println(
                 "You cannot request money from yourself."
         );

         return;
     }


     // CHECK DUPLICATE PENDING REQUEST

     if(PaymentRequestStore.pendingRequestExists(
             requester.getUserId(),
             payer.getUserId()))
     {
         System.out.println(
                 "A pending payment request already exists for this user."
         );

         return;
     }


     // ENTER AMOUNT

     while(true)
     {
         System.out.print("Enter requested amount: ₹");

         String input = sc.nextLine();

         try
         {
             double amount =
                     Double.parseDouble(input);

             validateAmount(amount);


             // CREATE REQUEST ID

             String requestId =
                     PaymentRequestStore.generateRequestId();


             // CREATE PAYMENT REQUEST

             PaymentRequest paymentRequest =
                     new PaymentRequest(
                             requestId,
                             requester.getUserId(),
                             payer.getUserId(),
                             amount,
                             "PENDING"
                     );


             // STORE REQUEST

             PaymentRequestStore.addPaymentRequest(
                     paymentRequest
             );

             AuditLogService.log(
                     requester.getUserId(),
                     "PAYMENT_REQUEST_CREATED",
                     "Created payment request "
                     + requestId
                     + " for "
                     + AmountFormatter.formatWithRupee(amount)
             );


             // SUCCESS MESSAGE

             System.out.println();

             System.out.println(
                     "Payment request created successfully!"
             );

             System.out.println(
                     "Request ID : " + requestId
             );

             System.out.println(
                     "Payer      : " + payer.getName()
             );

             System.out.println(
                     "Amount     : " + AmountFormatter.formatWithRupee(amount)
             );

             System.out.println(
                     "Status     : PENDING"
             );

             break;
         }
         catch(NumberFormatException e)
         {
             System.out.println(
                     "Please enter a valid number."
             );
         }
         catch(InvalidAmountException e)
         {
             System.out.println(
                     e.getMessage()
             );
         }
     }
 }
//========================================================
//VIEW PENDING PAYMENT REQUESTS
//========================================================

public static void viewPendingPaymentRequests(User user)
{
  System.out.println();
  System.out.println("================================================");
  System.out.println("          PENDING PAYMENT REQUESTS");
  System.out.println("================================================");

  List<PaymentRequest> pendingRequests =
          PaymentRequestStore.getPendingRequestsForPayer(
                  user.getUserId()
          );


  // NO PENDING REQUESTS

  if(pendingRequests.isEmpty())
  {
      System.out.println("No pending payment requests.");
      System.out.println("------------------------------------------------");
      return;
  }


  // DISPLAY REQUESTS

  for(PaymentRequest request : pendingRequests)
  {
      User requester =
              userDAO.findUserById(
                      request.getRequesterUserId()
              );


      System.out.println();

      System.out.println(
              "Request ID : "
              + request.getRequestId()
      );

      if(requester != null)
      {
          System.out.println(
                  "From       : "
                  + requester.getName()
          );

          System.out.println(
                  "Email      : "
                  + requester.getEmail()
          );
      }

      System.out.println(
              "Amount     : "
              + AmountFormatter.formatWithRupee(request.getAmount())
      );

      System.out.println(
              "Status     : "
              + request.getStatus()
      );

      System.out.println(
              "------------------------------------------------"
      );
  }
}
//========================================================
//APPROVE / REJECT PAYMENT REQUEST
//========================================================

public static void processPaymentRequest(
     User user,
     Scanner sc)
{
 System.out.println();
 System.out.println("================================================");
 System.out.println("          PROCESS PAYMENT REQUEST");
 System.out.println("================================================");

 System.out.print("Enter request ID: ");

 String requestId = sc.nextLine();


 // FIND REQUEST

 PaymentRequest request =
         PaymentRequestStore.getPaymentRequestById(
                 requestId
         );


 if(request == null)
 {
     System.out.println("Payment request not found.");
     return;
 }


 // VERIFY REQUEST BELONGS TO LOGGED-IN PAYER

 if(!request.getPayerUserId().equals(user.getUserId()))
 {
     System.out.println(
             "You are not authorized to process this request."
     );

     return;
 }


 // CHECK REQUEST STATUS

 
  // ====================================================
  // IDEMPOTENCY CHECK
  // ====================================================

  String paymentApprovalKey =
          "PAYMENT_APPROVAL:" + request.getRequestId();

  if(IdempotencyService.isAlreadyProcessed(
          paymentApprovalKey))
  {
      String existingTransactionId =
              IdempotencyService.getExistingTransactionId(
                      paymentApprovalKey
              );

      System.out.println();
      System.out.println(
              "This payment request has already been processed."
      );

      System.out.println(
              "Existing Transaction ID : "
              + existingTransactionId
      );

      return;
  }


  if(!request.getStatus().equals("PENDING"))
 {
     System.out.println(
             "This payment request has already been processed."
     );

     System.out.println(
             "Current Status: " + request.getStatus()
     );

     return;
 }


 // FIND REQUESTER

 User requester =
         userDAO.findUserById(
                 request.getRequesterUserId()
         );


 if(requester == null)
 {
     System.out.println(
             "Requester account not found."
     );

     return;
 }


 // DISPLAY REQUEST DETAILS

 System.out.println();

 System.out.println(
         "Request ID : "
         + request.getRequestId()
 );

 System.out.println(
         "Requester  : "
         + requester.getName()
 );

 System.out.println(
         "Amount     : "
         + AmountFormatter.formatWithRupee(request.getAmount())
 );

 System.out.println(
         "Status     : "
         + request.getStatus()
 );


 // CHOOSE ACTION

 System.out.println();

 System.out.println("1. Approve");
 System.out.println("2. Reject");
 System.out.println("3. Back");

 System.out.print("Enter your choice: ");

 String choice = sc.nextLine().trim();


 // ====================================================
 // REJECT
 // ====================================================

 if(choice.equals("2"))
 {
     request.setStatus("REJECTED");

     AuditLogService.log(
             user.getUserId(),
             "PAYMENT_REQUEST_REJECTED",
             "Rejected payment request "
             + request.getRequestId()
             + " from "
             + requester.getName()
     );

     System.out.println();
     System.out.println(
             "Payment request rejected successfully."
     );

     return;
 }


 // ====================================================
 // BACK
 // ====================================================

 if(choice.equals("3"))
 {
     return;
 }


 // ====================================================
 // APPROVE
 // ====================================================

 if(!choice.equals("1"))
 {
     System.out.println("Invalid choice.");
     return;
 }


 // ====================================================
 // CHECK MAXIMUM TRANSFER LIMIT
 // ====================================================

 if(request.getAmount() > WalletConstants.MAX_TRANSFER_AMOUNT)
 {
     System.out.println();
     System.out.println(
             "Maximum transfer limit is "
             + AmountFormatter.formatWithRupee(
                     WalletConstants.MAX_TRANSFER_AMOUNT
             )
     );
     return;
 }

 // ====================================================
 // CHECK DAILY TRANSFER LIMIT
 // ====================================================

 double todaysTransferAmount =
         TransactionStore.getTodaysSuccessfulTransferAmount(
                 user.getUserId()
         );

 double remainingDailyLimit =
         WalletConstants.DAILY_TRANSFER_LIMIT
         - todaysTransferAmount;

 if(request.getAmount() > remainingDailyLimit)
 {
     System.out.println();
     System.out.println("Daily transfer limit exceeded.");
     System.out.println(
             "Remaining daily limit: "
             + AmountFormatter.formatWithRupee(
                     remainingDailyLimit
             )
     );
     return;
 }

 // GET PAYER WALLET

 Wallet payerWallet =
         walletDAO.findWalletByUserId(
                 user.getUserId()
         );


 if(payerWallet == null)
 {
     System.out.println(
             "Your wallet was not found."
     );

     return;
 }


 // GET REQUESTER WALLET

 Wallet requesterWallet =
         walletDAO.findWalletByUserId(
                 requester.getUserId()
         );


 if(requesterWallet == null)
 {
     System.out.println(
             "Requester wallet was not found."
     );

     return;
 }


 // CHECK PAYER WALLET STATUS

 if(!"ACTIVE".equals(payerWallet.getStatus()))
 {
     System.out.println(
             "Your wallet is not active."
     );

     return;
 }


 // CHECK REQUESTER WALLET STATUS

 if(!"ACTIVE".equals(requesterWallet.getStatus()))
 {
     System.out.println(
             "Requester wallet is not active."
     );

     return;
 }


 // CHECK BALANCE

 if(request.getAmount() > payerWallet.getBalance())
 {
     System.out.println();
     System.out.println(
             "Insufficient balance."
     );

     System.out.println(
             "Available Balance: "
             + AmountFormatter.formatWithRupee(payerWallet.getBalance())
     );

     return;
 }


 // ====================================================
 // DEBIT PAYER
 // ====================================================

 double payerNewBalance =
         payerWallet.getBalance()
         - request.getAmount();

 payerWallet.setBalance(
         payerNewBalance
 );

 if (!walletDAO.updateWallet(payerWallet)) {
     System.out.println("Unable to update payer wallet in database.");
     return;
 }


 // ====================================================
 // CREDIT REQUESTER
 // ====================================================

 double requesterNewBalance =
         requesterWallet.getBalance()
         + request.getAmount();

 requesterWallet.setBalance(
         requesterNewBalance
 );

 if (!walletDAO.updateWallet(requesterWallet)) {
     System.out.println("Unable to update requester wallet in database.");
     return;
 }


 // ====================================================
 // CREATE TRANSACTION
 // ====================================================

 String transactionId =
	        TransactionStore.generateTransactionId();


 Transaction transaction =
         new Transaction(
                 transactionId,
                 user.getUserId(),
                 requester.getUserId(),
                 TransactionType.TRANSFER,
                 request.getAmount(),
                 TransactionStatus.SUCCESS
         );


 TransactionStore.addTransaction(
         transaction
 );

  IdempotencyService.recordProcessedRequest(
          paymentApprovalKey,
          transactionId
  );


 AuditLogService.log(
         user.getUserId(),
         "PAYMENT_REQUEST_APPROVED",
         "Approved payment request "
         + request.getRequestId()
         + " and transferred "
         + AmountFormatter.formatWithRupee(request.getAmount())
         + " to "
         + requester.getName()
         + ". Transaction ID: "
         + transactionId
 );


 // ====================================================
 // UPDATE REQUEST STATUS
 // ====================================================

 request.setStatus("APPROVED");
 TransactionReceipt receipt =
	        ReceiptService.createReceipt(
	                transaction,
	                user
	        );

	ReceiptService.printReceipt(receipt);


 // ====================================================
 // SUCCESS MESSAGE
 // ====================================================

 System.out.println();

 System.out.println(
         "================================================"
 );

 System.out.println(
         "        PAYMENT REQUEST APPROVED"
 );

 System.out.println(
         "================================================"
 );

 System.out.println(
         "Request ID       : "
         + request.getRequestId()
 );

 System.out.println(
         "Transaction ID   : "
         + transactionId
 );

 System.out.println(
         "Paid To          : "
         + requester.getName()
 );

 System.out.println(
         "Amount Paid      : "
         + AmountFormatter.formatWithRupee(request.getAmount())
 );

 System.out.println(
         "Remaining Balance: "
         + AmountFormatter.formatWithRupee(payerNewBalance)
 );

 System.out.println(
         "------------------------------------------------"
 );
}
//========================================================
//VIEW MY PAYMENT REQUESTS
//========================================================

public static void viewMyPaymentRequests(User user)
{
 System.out.println();
 System.out.println("================================================");
 System.out.println("             MY PAYMENT REQUESTS");
 System.out.println("================================================");

 List<PaymentRequest> requests =
         PaymentRequestStore.getRequestsByRequesterId(
                 user.getUserId()
         );

 if(requests.isEmpty())
 {
     System.out.println("You have not created any payment requests.");
     System.out.println("------------------------------------------------");
     return;
 }

 for(PaymentRequest request : requests)
 {
     User payer =
             userDAO.findUserById(
                     request.getPayerUserId()
             );

     System.out.println();

     System.out.println(
             "Request ID : "
             + request.getRequestId()
     );

     if(payer != null)
     {
         System.out.println(
                 "Payer      : "
                 + payer.getName()
         );

         System.out.println(
                 "Email      : "
                 + payer.getEmail()
         );
     }

     System.out.println(
             "Amount     : "
             + AmountFormatter.formatWithRupee(request.getAmount())
     );

     System.out.println(
             "Status     : "
             + request.getStatus()
     );

     System.out.println(
             "------------------------------------------------"
     );
 }
}
//========================================================
//MINI STATEMENT
//========================================================

public static void miniStatement(User user)
{
 Wallet wallet =
         walletDAO.findWalletByUserId(
                 user.getUserId()
         );

 if(wallet == null)
 {
     System.out.println("Wallet not found.");
     return;
 }


 List<Transaction> transactions =
         TransactionStore.getLatestTransactions(
                 user.getUserId(),
                 5
         );


 System.out.println();
 System.out.println("========================================================");
 System.out.println("                    MINI STATEMENT");
 System.out.println("========================================================");

 System.out.println(
         "User    : " + user.getName()
 );

 System.out.println(
         "Balance : " + AmountFormatter.formatWithRupee(wallet.getBalance())
 );

 System.out.println(
         "--------------------------------------------------------"
 );


 if(transactions.isEmpty())
 {
     System.out.println(
             "No transactions found."
     );

     System.out.println(
             "--------------------------------------------------------"
     );

     return;
 }


 for(Transaction transaction : transactions)
 {
     System.out.println();

     System.out.println(
             "Transaction ID : "
             + transaction.getTransactionId()
     );

     System.out.println(
             "Type           : "
             + getTransactionType(
                     transaction,
                     user
             )
     );

     System.out.println(
             "Amount         : "
             + AmountFormatter.formatWithRupee(transaction.getAmount())
     );

     System.out.println(
             "Date & Time    : "
             + TransactionStore.getTransactionDateTime(
                     transaction.getTransactionId()
             )
     );

     System.out.println(
             "Status         : "
             + transaction.getStatus()
     );

     System.out.println(
             "--------------------------------------------------------"
     );
 }


 System.out.println(
         "Showing latest "
         + transactions.size()
         + " transaction(s)."
 );

 System.out.println(
         "========================================================"
 );
}
//========================================================
//CANCEL TRANSACTION
//========================================================

public static void cancelTransaction(
     User user,
     Scanner sc)
{
 System.out.println();
 System.out.println("================================================");
 System.out.println("              CANCEL TRANSACTION");
 System.out.println("================================================");

 System.out.print("Enter transaction ID: ");

 String transactionId = sc.nextLine().trim();

 Transaction transaction =
         TransactionStore.getTransactionById(transactionId);

 if(transaction == null)
 {
     System.out.println();
     System.out.println("Transaction not found.");
     return;
 }

 long transactionAge =
         System.currentTimeMillis()
         - TransactionStore.getTransactionTimestamp(
                 transaction.getTransactionId());

 if(transactionAge >
         WalletConstants.TRANSACTION_CANCELLATION_WINDOW)
 {
     System.out.println();
     System.out.println("This transaction can no longer be cancelled.");
     System.out.println("Cancellation is allowed only within 5 minutes.");
     return;
 }

 // ====================================================
 // CHECK TRANSACTION BELONGS TO USER
 // ====================================================

 if(!user.getUserId().equals(
         transaction.getSenderUserId()))
 {
     System.out.println(
             "Only the sender can cancel this transaction."
     );

     return;
 }


 // ====================================================
 // CHECK TRANSACTION TYPE
 // ====================================================

 if(transaction.getType() != TransactionType.TRANSFER)
 {
     System.out.println(
             "Only transfer transactions can be cancelled."
     );

     return;
 }


 // ====================================================
 // CHECK TRANSACTION STATUS
 // ====================================================

 if(transaction.getStatus() != TransactionStatus.SUCCESS)
 {
     System.out.println();

     System.out.println(
             "This transaction cannot be cancelled."
     );

     System.out.println(
             "Current Status: "
             + transaction.getStatus()
     );

     return;
 }


 // ====================================================
 // FIND RECEIVER
 // ====================================================

 User receiver =
         userDAO.findUserById(
                 transaction.getReceiverUserId()
         );


 if(receiver == null)
 {
     System.out.println(
             "Receiver account not found."
     );

     return;
 }


 // ====================================================
 // FIND WALLETS
 // ====================================================

 Wallet senderWallet =
         walletDAO.findWalletByUserId(
                 user.getUserId()
         );


 Wallet receiverWallet =
         walletDAO.findWalletByUserId(
                 receiver.getUserId()
         );


 if(senderWallet == null)
 {
     System.out.println(
             "Sender wallet not found."
     );

     return;
 }


 if(receiverWallet == null)
 {
     System.out.println(
             "Receiver wallet not found."
     );

     return;
 }


 // ====================================================
 // CHECK WALLET STATUS
 // ====================================================

 if(!"ACTIVE".equals(senderWallet.getStatus()))
 {
     System.out.println(
             "Your wallet is not active."
     );

     return;
 }


 if(!"ACTIVE".equals(receiverWallet.getStatus()))
 {
     System.out.println(
             "Receiver wallet is not active."
     );

     return;
 }


 // ====================================================
 // CHECK RECEIVER BALANCE
 // ====================================================

 if(receiverWallet.getBalance()
         < transaction.getAmount())
 {
     System.out.println();

     System.out.println(
             "Transaction cannot be cancelled."
     );

     System.out.println(
             "Receiver does not have sufficient balance "
             + "to reverse the transaction."
     );

     return;
 }


 // ====================================================
 // DISPLAY TRANSACTION
 // ====================================================

 System.out.println();

 System.out.println(
         "Transaction ID : "
         + transaction.getTransactionId()
 );

 System.out.println(
         "Receiver       : "
         + receiver.getName()
 );

 System.out.println(
         "Amount         : "
         + AmountFormatter.formatWithRupee(transaction.getAmount())
 );

 System.out.println(
         "Date & Time    : "
         + TransactionStore.getTransactionDateTime(
                 transaction.getTransactionId()
         )
 );

 System.out.println(
         "Status         : "
         + transaction.getStatus()
 );


 // ====================================================
 // CONFIRM CANCELLATION
 // ====================================================

 System.out.println();

 System.out.println(
         "Are you sure you want to cancel this transaction?"
 );

 System.out.println("1. Confirm Cancellation");
 System.out.println("2. Keep Transaction");

 System.out.print("Enter your choice: ");

 String choice = sc.nextLine().trim();


 if(choice.equals("2"))
 {
     System.out.println(
             "Transaction cancellation cancelled."
     );

     return;
 }


 if(!choice.equals("1"))
 {
     System.out.println(
             "Invalid choice. Transaction cancellation cancelled."
     );

     return;
 }


 // ====================================================
 // REVERSE RECEIVER BALANCE
 // ====================================================

 double receiverNewBalance =
         receiverWallet.getBalance()
         - transaction.getAmount();

 receiverWallet.setBalance(
         receiverNewBalance
 );

 if (!walletDAO.updateWallet(receiverWallet)) {
     System.out.println("Unable to update receiver wallet in database.");
     return;
 }


 // ====================================================
 // RETURN MONEY TO SENDER
 // ====================================================

 double senderNewBalance =
         senderWallet.getBalance()
         + transaction.getAmount();

 senderWallet.setBalance(
         senderNewBalance
 );

 if (!walletDAO.updateWallet(senderWallet)) {
     System.out.println("Unable to update sender wallet in database.");
     return;
 }


 // ====================================================
 // MARK ORIGINAL TRANSACTION AS CANCELLED
 // ====================================================

 transaction.setStatus(TransactionStatus.CANCELLED);


 // ====================================================
 // CREATE REVERSAL TRANSACTION
 // ====================================================

 String reversalTransactionId =
         TransactionStore.generateTransactionId();


 Transaction reversalTransaction =
         new Transaction(
                 reversalTransactionId,
                 receiver.getUserId(),
                 user.getUserId(),
                 TransactionType.TRANSFER_REVERSAL,
                 transaction.getAmount(),
                 TransactionStatus.SUCCESS
         );


 TransactionStore.addTransaction(
         reversalTransaction
 );

 AuditLogService.log(
         user.getUserId(),
         "TRANSFER_CANCELLED",
         "Cancelled transfer "
         + transaction.getTransactionId()
         + " and received "
         + AmountFormatter.formatWithRupee(transaction.getAmount())
         + " back. Reversal Transaction ID: "
         + reversalTransactionId
 );

 TransactionReceipt receipt =
	        ReceiptService.createReceipt(
	                reversalTransaction,
	                user
	        );

	ReceiptService.printReceipt(receipt);

 // ====================================================
 // SUCCESS MESSAGE
 // ====================================================

 System.out.println();

 System.out.println(
         "================================================"
 );

 System.out.println(
         "          TRANSACTION CANCELLED"
 );

 System.out.println(
         "================================================"
 );

 System.out.println(
         "Original Transaction : "
         + transaction.getTransactionId()
 );

 System.out.println(
         "Reversal Transaction : "
         + reversalTransactionId
 );

 System.out.println(
         "Refunded Amount      : "
         + AmountFormatter.formatWithRupee(transaction.getAmount())
 );

 System.out.println(
         "New Balance          : "
         + AmountFormatter.formatWithRupee(senderNewBalance)
 );

 System.out.println(
         "Status               : CANCELLED"
 );

 System.out.println(
         "------------------------------------------------"
 );
}

public static void viewTransactionStatistics(User user) {

    if (user == null) {
        System.out.println("User not found.");
        return;
    }

    String userId = user.getUserId();

    double totalDeposited =
            TransactionStatisticsService.getTotalDeposited(userId);

    double totalWithdrawn =
            TransactionStatisticsService.getTotalWithdrawn(userId);

    double totalTransferred =
            TransactionStatisticsService.getTotalTransferred(userId);

    double totalReceived =
            TransactionStatisticsService.getTotalReceived(userId);

    int successfulTransactions =
            TransactionStatisticsService
                    .getSuccessfulTransactionCount(userId);

    int cancelledTransactions =
            TransactionStatisticsService
                    .getCancelledTransactionCount(userId);

    double averageTransactionAmount =
            TransactionStatisticsService
                    .getAverageTransactionAmount(userId);

    System.out.println();
    System.out.println("========================================================");
    System.out.println("              TRANSACTION STATISTICS");
    System.out.println("========================================================");

    System.out.println(
            "Total Deposited          : "
            + AmountFormatter.formatWithRupee(totalDeposited)
    );

    System.out.println(
            "Total Withdrawn          : "
            + AmountFormatter.formatWithRupee(totalWithdrawn)
    );

    System.out.println(
            "Total Transferred        : "
            + AmountFormatter.formatWithRupee(totalTransferred)
    );

    System.out.println(
            "Total Received           : "
            + AmountFormatter.formatWithRupee(totalReceived)
    );

    System.out.println("--------------------------------------------------------");

    System.out.println(
            "Successful Transactions  : "
            + successfulTransactions
    );

    System.out.println(
            "Cancelled Transactions   : "
            + cancelledTransactions
    );

    System.out.println(
            "Average Transaction      : "
            + AmountFormatter.formatWithRupee(
                    averageTransactionAmount
            )
    );

    System.out.println("========================================================");
}
    // ========================================================
    // AMOUNT VALIDATION
    // ========================================================

    static void validateAmount(double amount)
            throws InvalidAmountException
    {
        if(Double.isNaN(amount) || Double.isInfinite(amount))
        {
            throw new InvalidAmountException(
                    "Amount must be a valid number."
            );
        }

        if(amount <= 0)
        {
            throw new InvalidAmountException(
                    "Amount must be greater than zero."
            );
        }
    }
}