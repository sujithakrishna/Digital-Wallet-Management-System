package Digital_Wallet_Management_System;

public class PasswordValidator {

    public static boolean isStrongPassword(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char ch : password.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            }
            else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            }
            else if (Character.isDigit(ch)) {
                hasDigit = true;
            }
            else {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase
                && hasLowercase
                && hasDigit
                && hasSpecialCharacter;
    }
}