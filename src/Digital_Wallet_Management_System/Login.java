package Digital_Wallet_Management_System;

import java.sql.SQLException;
import java.util.Scanner;

public class Login {

    public static User login(Scanner sc) {

        System.out.println("\n================================================");
        System.out.println("                     LOGIN");
        System.out.println("================================================");

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim().toLowerCase();

        UserDAO userDAO = new UserDAO();

        // ========================================================
        // FIND USER FROM MYSQL
        // ========================================================

        User user = userDAO.findUserByEmail(email);


        // ========================================================
        // USER NOT FOUND
        // ========================================================

        if (user == null) {

            System.out.println("User not found.");

            /*
             * We cannot create a user-specific audit log here
             * because no valid user exists for this email.
             */

            return null;
        }


        // ========================================================
        // CHECK WHETHER ACCOUNT IS CURRENTLY LOCKED
        // ========================================================

        if (user.isAccountLocked()) {

            long currentTime = System.currentTimeMillis();


            // ====================================================
            // LOCK PERIOD HAS EXPIRED
            // ====================================================

            if (currentTime >= user.getLockedUntil()) {

                user.setAccountLocked(false);
                user.setFailedLoginAttempts(0);
                user.setLockedUntil(0);

                // Persist unlocked status in MySQL
                boolean updated = userDAO.updateUser(user);

                if (!updated) {

                    System.out.println(
                            "Unable to update account status."
                    );

                    return null;
                }

                System.out.println(
                        "Account lock has expired."
                );

                System.out.println(
                        "You can try logging in again."
                );
            }


            // ====================================================
            // ACCOUNT IS STILL LOCKED
            // ====================================================

            else {

                long remainingTime =
                        user.getLockedUntil() - currentTime;

                long remainingMinutes =
                        (remainingTime / 1000) / 60;

                long remainingSeconds =
                        (remainingTime / 1000) % 60;

                System.out.println();

                System.out.println(
                        "Account is temporarily locked."
                );

                System.out.println(
                        "Try again in "
                        + remainingMinutes
                        + " minutes "
                        + remainingSeconds
                        + " seconds."
                );

                return null;
            }
        }


        // ========================================================
        // ENTER PASSWORD
        // ========================================================

        System.out.print("Enter password: ");
        String password = sc.nextLine();


        // ========================================================
        // WRONG PASSWORD
        // ========================================================

        if (!user.getPassword().equals(password)) {

            int attempts =
                    user.getFailedLoginAttempts() + 1;

            user.setFailedLoginAttempts(attempts);

            System.out.println();
            System.out.println("Incorrect password.");

            System.out.println(
                    "Failed login attempts: "
                    + attempts
                    + "/3"
            );


            // ====================================================
            // AUDIT FAILED LOGIN
            // ====================================================

            AuditLogService.log(
                    user.getUserId(),
                    "LOGIN_FAILED",
                    "Incorrect password. Failed attempt "
                    + attempts
                    + " of 3"
            );


            // ====================================================
            // LOCK ACCOUNT AFTER 3 FAILED ATTEMPTS
            // ====================================================

            if (attempts >= 3) {

                user.setAccountLocked(true);


                // 5 MINUTE LOCK

                long lockDuration =
                        5 * 60 * 1000;

                user.setLockedUntil(
                        System.currentTimeMillis()
                        + lockDuration
                );

                System.out.println();

                System.out.println(
                        "Account has been temporarily locked."
                );

                System.out.println(
                        "Account will be locked for 5 minutes."
                );


                // ================================================
                // AUDIT ACCOUNT LOCK
                // ================================================

                AuditLogService.log(
                        user.getUserId(),
                        "ACCOUNT_LOCKED",
                        "Account temporarily locked for 5 minutes "
                        + "after 3 failed login attempts"
                );
            }


            // ====================================================
            // SAVE FAILED ATTEMPT / LOCK STATUS TO MYSQL
            // ====================================================

            boolean updated = userDAO.updateUser(user);

            if (!updated) {

                System.out.println(
                        "Warning: Login security status "
                        + "could not be saved to database."
                );
            }

            return null;
        }


        // ========================================================
        // SUCCESSFUL LOGIN
        // ========================================================

        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setLockedUntil(0);


        // ========================================================
        // SAVE LOGIN RESET TO MYSQL
        // ========================================================

        boolean updated = userDAO.updateUser(user);

        if (!updated) {

            System.out.println(
                    "Unable to update login status."
            );

            return null;
        }


        System.out.println();
        System.out.println("Login successful!");

        System.out.println(
                "Welcome, " + user.getName() + "!"
        );


        // ========================================================
        // AUDIT SUCCESSFUL LOGIN
        // ========================================================

        AuditLogService.log(
                user.getUserId(),
                "LOGIN_SUCCESS",
                "User logged in successfully"
        );


        return user;
    }
}