package Digital_Wallet_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BeneficiaryDAO {

    private static final String INSERT_SQL =
            "INSERT INTO beneficiaries "
            + "(beneficiary_id, user_id, beneficiary_user_id, "
            + "beneficiary_name, beneficiary_email) "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_USER_SQL =
            "SELECT beneficiary_id, user_id, beneficiary_user_id, "
            + "beneficiary_name, beneficiary_email "
            + "FROM beneficiaries "
            + "WHERE user_id = ? "
            + "ORDER BY created_at DESC";

    private static final String FIND_BY_ID_SQL =
            "SELECT beneficiary_id, user_id, beneficiary_user_id, "
            + "beneficiary_name, beneficiary_email "
            + "FROM beneficiaries "
            + "WHERE beneficiary_id = ?";

    private static final String EXISTS_SQL =
            "SELECT 1 FROM beneficiaries "
            + "WHERE user_id = ? AND beneficiary_user_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM beneficiaries WHERE beneficiary_id = ?";

    public void save(Beneficiary beneficiary) throws SQLException {

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(INSERT_SQL)) {

            statement.setString(1, beneficiary.getBeneficiaryId());
            statement.setString(2, beneficiary.getUserId());
            statement.setString(3, beneficiary.getBeneficiaryUserId());
            statement.setString(4, beneficiary.getBeneficiaryName());
            statement.setString(5, beneficiary.getBeneficiaryEmail());

            statement.executeUpdate();
        }
    }

    public List<Beneficiary> findByUserId(String userId)
            throws SQLException {

        List<Beneficiary> beneficiaries = new ArrayList<>();

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(FIND_BY_USER_SQL)) {

            statement.setString(1, userId);

            try(ResultSet resultSet = statement.executeQuery()) {

                while(resultSet.next()) {
                    beneficiaries.add(mapBeneficiary(resultSet));
                }
            }
        }

        return beneficiaries;
    }

    public Beneficiary findById(String beneficiaryId)
            throws SQLException {

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setString(1, beneficiaryId);

            try(ResultSet resultSet = statement.executeQuery()) {

                if(resultSet.next()) {
                    return mapBeneficiary(resultSet);
                }
            }
        }

        return null;
    }

    public boolean exists(String userId, String beneficiaryUserId)
            throws SQLException {

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(EXISTS_SQL)) {

            statement.setString(1, userId);
            statement.setString(2, beneficiaryUserId);

            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void delete(String beneficiaryId) throws SQLException {

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(DELETE_SQL)) {

            statement.setString(1, beneficiaryId);
            statement.executeUpdate();
        }
    }

    public String generateBeneficiaryId() {
        return "BEN" + UUID.randomUUID().toString().replace("-", "").substring(0, 13);
    }

    private Beneficiary mapBeneficiary(ResultSet resultSet)
            throws SQLException {

        return new Beneficiary(
                resultSet.getString("beneficiary_id"),
                resultSet.getString("user_id"),
                resultSet.getString("beneficiary_user_id"),
                resultSet.getString("beneficiary_name"),
                resultSet.getString("beneficiary_email")
        );
    }
}
