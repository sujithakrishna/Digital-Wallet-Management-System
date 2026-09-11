package Digital_Wallet_Management_System;

import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        User loggedInUser = null;

        while (true) {

            System.out.println();
            System.out.println("========================================================");
            System.out.println("              DIGITAL WALLET MANAGEMENT");
            System.out.println("========================================================");

            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Edit Profile");
            System.out.println("4. Change Password");
            System.out.println("5. View Profile");
            System.out.println("6. Check Balance");
            System.out.println("7. Deposit Money");
            System.out.println("8. Withdraw Money");
            System.out.println("9. Transfer Money");
            System.out.println("10. Transaction History");
            System.out.println("11. Mini Statement");
            System.out.println("12. Transaction Details");
            System.out.println("13. Cancel Transaction");
            System.out.println("14. Search Transactions");
            System.out.println("15. Lock Wallet");
            System.out.println("16. Unlock Wallet");
            System.out.println("17. Beneficiary Management");
            System.out.println("18. Payment Request Management");
            System.out.println("19. Logout");
            System.out.println("20. Exit");
            System.out.println("21. Transaction Statistics");

            System.out.println("========================================================");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = sc.nextInt();
                sc.nextLine();
            }
            catch (java.util.InputMismatchException e) {
                System.out.println();
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {

                case 1:
                    Registeruser.register(sc);
                    break;

                case 2:
                    loggedInUser = Login.login(sc);
                    break;

                case 3:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.editProfile(loggedInUser, sc);
                    }
                    break;

                case 4:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.changePassword(loggedInUser, sc);
                    }
                    break;

                case 5:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.viewProfile(loggedInUser);
                    }
                    break;

                case 6:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.checkBalance(loggedInUser);
                    }
                    break;

                case 7:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.depositMoney(loggedInUser, sc);
                    }
                    break;

                case 8:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.withdrawMoney(loggedInUser, sc);
                    }
                    break;

                case 9:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.transferMoney(loggedInUser, sc);
                    }
                    break;

                case 10:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.transactionHistory(loggedInUser);
                    }
                    break;

                case 11:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.miniStatement(loggedInUser);
                    }
                    break;

                case 12:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.transactionDetails(loggedInUser, sc);
                    }
                    break;

                case 13:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.cancelTransaction(loggedInUser, sc);
                    }
                    break;

                case 14:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.searchTransactions(loggedInUser, sc);
                    }
                    break;

                case 15:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        WalletService.lockWallet(loggedInUser);
                    }
                    break;

                case 16:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                    	WalletService.unlockWallet(loggedInUser, sc);
                    }
                    break;

                case 17:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        beneficiaryMenu(loggedInUser, sc);
                    }
                    break;

                case 18:
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                    }
                    else {
                        paymentRequestMenu(loggedInUser, sc);
                    }
                    break;

                case 19:
                    if (loggedInUser == null) {
                        System.out.println("You are not logged in.");
                    }
                    else {
                        loggedInUser = null;
                        System.out.println();
                        System.out.println("Logged out successfully.");
                    }
                    break;

                case 20:
                    System.out.println();
                    System.out.println("Thank you for using Digital Wallet.");
                    sc.close();
                    return;
                    
                case 21:
                    WalletService.viewTransactionStatistics(loggedInUser);
                    break;

                default:
                    System.out.println();
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    // ========================================================
    // BENEFICIARY MANAGEMENT
    // ========================================================

    public static void beneficiaryMenu(User user, Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("========================================================");
            System.out.println("             BENEFICIARY MANAGEMENT");
            System.out.println("========================================================");

            System.out.println("1. Add Beneficiary");
            System.out.println("2. View Beneficiaries");
            System.out.println("3. Remove Beneficiary");
            System.out.println("4. Back");

            System.out.println("========================================================");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    WalletService.addBeneficiary(user, sc);
                    break;

                case "2":
                    WalletService.viewBeneficiaries(user);
                    break;

                case "3":
                    WalletService.removeBeneficiary(user, sc);
                    break;

                case "4":
                    return;

                default:
                    System.out.println();
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    // ========================================================
    // PAYMENT REQUEST MANAGEMENT
    // ========================================================

    public static void paymentRequestMenu(User user, Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("========================================================");
            System.out.println("            PAYMENT REQUEST MANAGEMENT");
            System.out.println("========================================================");

            System.out.println("1. Create Payment Request");
            System.out.println("2. View Pending Requests");
            System.out.println("3. Approve / Reject Request");
            System.out.println("4. View My Requests");
            System.out.println("5. Back");

            System.out.println("========================================================");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    WalletService.createPaymentRequest(user, sc);
                    break;

                case "2":
                    WalletService.viewPendingPaymentRequests(user);
                    break;

                case "3":
                    WalletService.processPaymentRequest(user, sc);
                    break;

                case "4":
                    WalletService.viewMyPaymentRequests(user);
                    break;

                case "5":
                    return;

                default:
                    System.out.println();
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}