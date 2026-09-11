package Digital_Wallet_Management_System;

public class UserDAOTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        // --------------------------------------------
        // TEST 1: FIND EXISTING USER BY EMAIL
        // --------------------------------------------

        User user =
                userDAO.findUserByEmail("testuser@gmail.com");

        if (user != null) {

            System.out.println("User found successfully.");

            System.out.println(
                    "User ID: " + user.getUserId()
            );

            System.out.println(
                    "Name: " + user.getName()
            );

            System.out.println(
                    "Email: " + user.getEmail()
            );

            System.out.println(
                    "Mobile: " + user.getMobileNumber()
            );

        } else {

            System.out.println("User not found.");
        }


        // --------------------------------------------
        // TEST 2: FIND USER BY ID
        // --------------------------------------------

        User userById =
                userDAO.findUserById("U1001");

        if (userById != null) {

            System.out.println(
                    "\nUser found using ID."
            );

            System.out.println(
                    "Name: " + userById.getName()
            );

        } else {

            System.out.println(
                    "\nUser not found using ID."
            );
        }


        // --------------------------------------------
        // TEST 3: SAVE NEW USER
        // --------------------------------------------

        User newUser = new User(
                "U1003",
                "DAO Test User",
                "daotest@gmail.com",
                "9000000003",
                "Test@789"
        );

        boolean saved = userDAO.saveUser(newUser);

        if (saved) {

            System.out.println(
                    "\nNew user saved successfully."
            );

        } else {

            System.out.println(
                    "\nFailed to save new user."
            );
        }


        // --------------------------------------------
        // TEST 4: FIND THE NEW USER
        // --------------------------------------------

        User savedUser =
                userDAO.findUserByEmail("daotest@gmail.com");

        if (savedUser != null) {

            System.out.println(
                    "\nNew user retrieved successfully."
            );

            System.out.println(
                    "User ID: " + savedUser.getUserId()
            );

            System.out.println(
                    "Name: " + savedUser.getName()
            );

            System.out.println(
                    "Email: " + savedUser.getEmail()
            );

        } else {

            System.out.println(
                    "\nNew user could not be retrieved."
            );
        }
    }
}