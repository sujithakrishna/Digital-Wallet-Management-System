package Digital_Wallet_Management_System;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/digital_wallet";

    private static final String USERNAME =
            "root";

    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    private DatabaseConnection() {
        // Prevent object creation
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}