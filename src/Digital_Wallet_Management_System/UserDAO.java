package Digital_Wallet_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // --------------------------------------------------
    // SAVE USER
    // --------------------------------------------------

    public boolean saveUser(User user) {

        String sql = """
                INSERT INTO users
                (
                    user_id,
                    name,
                    email,
                    mobile_number,
                    password,
                    failed_login_attempts,
                    account_locked,
                    locked_until
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getUserId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMobileNumber());
            statement.setString(5, user.getPassword());
            statement.setInt(6, user.getFailedLoginAttempts());
            statement.setBoolean(7, user.isAccountLocked());
            statement.setLong(8, user.getLockedUntil());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Error while saving user.");
            e.printStackTrace();

            return false;
        }
    }


    // --------------------------------------------------
    // FIND USER BY EMAIL
    // --------------------------------------------------

    public User findUserByEmail(String email) {

        String sql = """
                SELECT
                    user_id,
                    name,
                    email,
                    mobile_number,
                    password,
                    failed_login_attempts,
                    account_locked,
                    locked_until
                FROM users
                WHERE email = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error while finding user by email.");
            e.printStackTrace();
        }

        return null;
    }


    // --------------------------------------------------
    // FIND USER BY ID
    // --------------------------------------------------

    public User findUserById(String userId) {

        String sql = """
                SELECT
                    user_id,
                    name,
                    email,
                    mobile_number,
                    password,
                    failed_login_attempts,
                    account_locked,
                    locked_until
                FROM users
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error while finding user by ID.");
            e.printStackTrace();
        }

        return null;
    }


    // --------------------------------------------------
    // UPDATE USER
    // --------------------------------------------------

    public boolean updateUser(User user) {

        String sql = """
                UPDATE users
                SET
                    name = ?,
                    email = ?,
                    mobile_number = ?,
                    password = ?,
                    failed_login_attempts = ?,
                    account_locked = ?,
                    locked_until = ?
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getMobileNumber());
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getFailedLoginAttempts());
            statement.setBoolean(6, user.isAccountLocked());
            statement.setLong(7, user.getLockedUntil());
            statement.setString(8, user.getUserId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.out.println("Error while updating user.");
            e.printStackTrace();

            return false;
        }
    }


    // --------------------------------------------------
    // MAP RESULT SET TO USER OBJECT
    // --------------------------------------------------

    private User mapUser(ResultSet resultSet)
            throws SQLException {

        User user = new User(
                resultSet.getString("user_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("mobile_number"),
                resultSet.getString("password")
        );

        user.setFailedLoginAttempts(
                resultSet.getInt("failed_login_attempts")
        );

        user.setAccountLocked(
                resultSet.getBoolean("account_locked")
        );

        user.setLockedUntil(
                resultSet.getLong("locked_until")
        );

        return user;
    }
}