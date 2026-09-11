package Digital_Wallet_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletDAO {

    // --------------------------------------------------
    // SAVE WALLET
    // --------------------------------------------------

    public boolean saveWallet(Wallet wallet) {

        String sql = """
                INSERT INTO wallets
                (
                    wallet_id,
                    user_id,
                    balance,
                    status
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, wallet.getWalletId());
            statement.setString(2, wallet.getUserId());
            statement.setDouble(3, wallet.getBalance());
            statement.setString(4, wallet.getStatus());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Error while saving wallet.");
            e.printStackTrace();

            return false;
        }
    }


    // --------------------------------------------------
    // FIND WALLET BY USER ID
    // --------------------------------------------------

    public Wallet findWalletByUserId(String userId) {

        String sql = """
                SELECT
                    wallet_id,
                    user_id,
                    balance,
                    status
                FROM wallets
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Wallet wallet = new Wallet(
                            resultSet.getString("wallet_id"),
                            resultSet.getString("user_id")
                    );

                    wallet.setBalance(
                            resultSet.getDouble("balance")
                    );

                    wallet.setStatus(
                            resultSet.getString("status")
                    );

                    return wallet;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while finding wallet by user ID."
            );

            e.printStackTrace();
        }

        return null;
    }


    // --------------------------------------------------
    // UPDATE WALLET
    // --------------------------------------------------

    public boolean updateWallet(Wallet wallet) {

        String sql = """
                UPDATE wallets
                SET
                    balance = ?,
                    status = ?
                WHERE wallet_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDouble(1, wallet.getBalance());
            statement.setString(2, wallet.getStatus());
            statement.setString(3, wallet.getWalletId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.out.println("Error while updating wallet.");
            e.printStackTrace();

            return false;
        }
    }
}
