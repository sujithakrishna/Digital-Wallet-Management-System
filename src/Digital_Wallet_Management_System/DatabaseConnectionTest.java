package Digital_Wallet_Management_System;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection != null) {
                System.out.println(
                        "MySQL connection successful."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "MySQL connection failed."
            );

            e.printStackTrace();
        }
    }
}