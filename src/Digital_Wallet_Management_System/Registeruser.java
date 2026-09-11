package Digital_Wallet_Management_System;

import java.util.Scanner;

import Digital_Wallet_Management_System.exception.EmptyNameException;
import Digital_Wallet_Management_System.exception.InvalidEmailException;
import Digital_Wallet_Management_System.exception.InvalidMobileException;
import Digital_Wallet_Management_System.exception.PasswordMismatchException;

public class Registeruser {

    public static void register(Scanner sc) {

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                 DIGITAL WALLET SYSTEM");
        System.out.println("                    USER REGISTRATION");
        System.out.println("========================================================");

        System.out.println();
        System.out.println("Please enter your details:");
        System.out.println();

        System.out.println("--------------------------------------------------------");

        String name;
        String email;
        String mobileNumber;
        String password;
        String confirmPassword;


        // ========================================================
        // NAME VALIDATION
        // ========================================================

        while (true) {

            System.out.print("Full Name       : ");
            name = sc.nextLine();

            try {

                validatename(name);
                name = name.trim();

                break;

            } catch (EmptyNameException e) {

                System.out.println(e.getMessage());
            }
        }


        // ========================================================
        // EMAIL VALIDATION
        // ========================================================

        while (true) {

            System.out.print("Email Address   : ");
            email = sc.nextLine();

            try {

                validateemail(email);
                email = email.trim().toLowerCase();

                break;

            } catch (InvalidEmailException e) {

                System.out.println(e.getMessage());
            }
        }


        // ========================================================
        // DUPLICATE EMAIL CHECK
        // ========================================================

        UserDAO userDAO = new UserDAO();

        if (userDAO.findUserByEmail(email) != null) {

            System.out.println(
                    "Email is already registered."
            );

            return;
        }


        // ========================================================
        // MOBILE NUMBER VALIDATION
        // ========================================================

        while (true) {

            System.out.print("Mobile Number   : ");
            mobileNumber = sc.nextLine();

            try {

                validatemobile(mobileNumber);
                mobileNumber = mobileNumber.trim();

                break;

            } catch (InvalidMobileException e) {

                System.out.println(e.getMessage());
            }
        }


        // ========================================================
        // PASSWORD VALIDATION
        // ========================================================

        while (true) {

            System.out.print("Create Password : ");
            password = sc.nextLine();

            System.out.print("Confirm Password: ");
            confirmPassword = sc.nextLine();

            try {

                validatepassword(
                        password,
                        confirmPassword
                );

                break;

            } catch (PasswordMismatchException e) {

                System.out.println(e.getMessage());
                System.out.println(
                        "Please enter the password again."
                );
            }
        }


        // ========================================================
        // CREATE USER ID
        // ========================================================

        /*
         * Temporary ID generation for the JDBC migration.
         *
         * We are not using Userstore.getUserCount()
         * because Userstore is the old in-memory storage.
         *
         * This will later be replaced with proper database-side
         * ID generation.
         */

        String userId = generateUserId();


        // ========================================================
        // CREATE USER
        // ========================================================

        User user = new User(
                userId,
                name,
                email,
                mobileNumber,
                password
        );


        // ========================================================
        // SAVE USER TO MYSQL
        // ========================================================

        boolean userSaved = userDAO.saveUser(user);

        if (!userSaved) {

            System.out.println();
            System.out.println(
                    "Registration failed while saving user."
            );

            return;
        }


        // ========================================================
        // CREATE WALLET
        // ========================================================

        String walletId = generateWalletId(userId);

        Wallet wallet = new Wallet(
                walletId,
                userId
        );


        // ========================================================
        // SAVE WALLET TO MYSQL
        // ========================================================

        WalletDAO walletDAO = new WalletDAO();

        boolean walletSaved =
                walletDAO.saveWallet(wallet);

        if (!walletSaved) {

            System.out.println();
            System.out.println(
                    "User was created, but wallet creation failed."
            );

            System.out.println(
                    "This will be handled with a database transaction "
                    + "in the next migration step."
            );

            return;
        }


        // ========================================================
        // AUDIT LOG
        // ========================================================

        AuditLogService.log(
                userId,
                "USER_REGISTERED",
                "New user registered successfully."
        );


        // ========================================================
        // SUCCESS MESSAGE
        // ========================================================

        System.out.println();

        System.out.println(
                "Registration successful!"
        );

        System.out.println(
                "Your User ID: " + userId
        );

        System.out.println(
                "Your Wallet ID: " + walletId
        );

        System.out.println(
                "-------------------------------------------------------"
        );
    }


    // ========================================================
    // GENERATE USER ID
    // ========================================================

    private static String generateUserId() {

        return "USR" + System.currentTimeMillis();
    }


    // ========================================================
    // GENERATE WALLET ID
    // ========================================================

    private static String generateWalletId(String userId) {

        return "WAL" + userId.substring(3);
    }


    // ========================================================
    // NAME VALIDATION
    // ========================================================

    static void validatename(String name)
            throws EmptyNameException {

        if (name == null ||
            name.trim().isEmpty()) {

            throw new EmptyNameException(
                    "Please enter your name."
            );
        }

        String trimmedName = name.trim();

        if (trimmedName.length() < 2) {

            throw new EmptyNameException(
                    "Name must contain at least 2 characters."
            );
        }

        if (trimmedName.length() > 50) {

            throw new EmptyNameException(
                    "Name cannot exceed 50 characters."
            );
        }

        if (!trimmedName.matches(
                "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$")) {

            throw new EmptyNameException(
                    "Name can contain only letters, spaces, "
                    + "apostrophes and hyphens."
            );
        }
    }


    // ========================================================
    // PASSWORD VALIDATION
    // ========================================================

    static void validatepassword(
            String password,
            String confirmPassword)
            throws PasswordMismatchException {

        if (password == null ||
            password.trim().isEmpty()) {

            throw new PasswordMismatchException(
                    "Password cannot be empty."
            );
        }

        if (confirmPassword == null) {

            throw new PasswordMismatchException(
                    "Please confirm your password."
            );
        }

        if (!password.equals(confirmPassword)) {

            throw new PasswordMismatchException(
                    "Passwords do not match."
            );
        }

        if (password.length() < 8) {

            throw new PasswordMismatchException(
                    "Password must contain at least 8 characters."
            );
        }

        if (password.length() > 64) {

            throw new PasswordMismatchException(
                    "Password cannot exceed 64 characters."
            );
        }

        if (!password.matches(".*[A-Z].*")) {

            throw new PasswordMismatchException(
                    "Password must contain at least one uppercase letter."
            );
        }

        if (!password.matches(".*[a-z].*")) {

            throw new PasswordMismatchException(
                    "Password must contain at least one lowercase letter."
            );
        }

        if (!password.matches(".*\\d.*")) {

            throw new PasswordMismatchException(
                    "Password must contain at least one digit."
            );
        }

        if (!password.matches(".*[^A-Za-z0-9].*")) {

            throw new PasswordMismatchException(
                    "Password must contain at least one special character."
            );
        }
    }


    // ========================================================
    // MOBILE NUMBER VALIDATION
    // ========================================================

    static void validatemobile(String mobileNumber)
            throws InvalidMobileException {

        if (mobileNumber == null ||
            !mobileNumber.trim().matches("[6-9]\\d{9}")) {

            throw new InvalidMobileException(
                    "Invalid mobile number. "
                    + "Enter a valid 10-digit Indian mobile number."
            );
        }
    }


    // ========================================================
    // EMAIL VALIDATION
    // ========================================================

    static void validateemail(String email)
            throws InvalidEmailException {

        if (email == null ||
            email.trim().isEmpty()) {

            throw new InvalidEmailException(
                    "Email address cannot be empty."
            );
        }

        String trimmedEmail = email.trim();

        if (trimmedEmail.length() > 254) {

            throw new InvalidEmailException(
                    "Email address is too long."
            );
        }

        if (!trimmedEmail.matches(
                "^[A-Za-z0-9](?:[A-Za-z0-9._%+-]*[A-Za-z0-9])?@"
                + "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?"
                + "(?:\\.[A-Za-z]{2,})+$")) {

            throw new InvalidEmailException(
                    "Invalid Email Address."
            );
        }
    }
}