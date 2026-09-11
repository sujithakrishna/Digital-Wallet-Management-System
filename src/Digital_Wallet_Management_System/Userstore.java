package Digital_Wallet_Management_System;

import java.util.HashMap;
import java.util.Map;

public class Userstore {

    private static Map<String, User> users = new HashMap<>();

    public static boolean emailExists(String email) {
        return users.containsKey(email);
    }

    public static void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    public static User getUserByEmail(String email) {
        return users.get(email);
    }

    public static User getUserById(String userId) {
        for (User user : users.values()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }

        return null;
    }

    public static int getUserCount() {
        return users.size();
    }

    public static void updateEmail(User user, String newEmail) {

        users.remove(user.getEmail());

        user.setEmail(newEmail);

        users.put(newEmail, user);
    }
}