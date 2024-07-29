package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.FinancialData;

public class FinancialDataDAO {
    private static final String JDBC_URL = "jdbc:mariadb://localhost/fintrack";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public FinancialDataDAO() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした", e);
        }
    }

    public boolean insertFinancialData(FinancialData data) {
        String sql = "INSERT INTO financial_data (user_id, date, category, amount, type, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, data.getUserId());
            pstmt.setDate(2, java.sql.Date.valueOf(data.getDate()));
            pstmt.setString(3, data.getCategory());
            pstmt.setBigDecimal(4, data.getAmount());
            pstmt.setString(5, data.getType());
            pstmt.setString(6, data.getDescription());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    data.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public FinancialData getFinancialDataById(int id) {
        String sql = "SELECT * FROM financial_data WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractFinancialDataFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FinancialData> getFinancialDataByUserId(int userId) {
        List<FinancialData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM financial_data WHERE user_id = ? ORDER BY date DESC";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dataList.add(extractFinancialDataFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public List<FinancialData> getFinancialDataByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        List<FinancialData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM financial_data WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dataList.add(extractFinancialDataFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public boolean updateFinancialData(FinancialData data) {
        String sql = "UPDATE financial_data SET date = ?, category = ?, amount = ?, type = ?, description = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(data.getDate()));
            pstmt.setString(2, data.getCategory());
            pstmt.setBigDecimal(3, data.getAmount());
            pstmt.setString(4, data.getType());
            pstmt.setString(5, data.getDescription());
            pstmt.setInt(6, data.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFinancialData(int id) {
        String sql = "DELETE FROM financial_data WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private FinancialData extractFinancialDataFromResultSet(ResultSet rs) throws SQLException {
        FinancialData data = new FinancialData();
        data.setId(rs.getInt("id"));
        data.setUserId(rs.getInt("user_id"));
        data.setDate(rs.getDate("date").toLocalDate());
        data.setCategory(rs.getString("category"));
        data.setAmount(rs.getBigDecimal("amount"));
        data.setType(rs.getString("type"));
        data.setDescription(rs.getString("description"));
        return data;
    }
    public BigDecimal getTotalIncomeByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(amount) AS total_income " +
                     "FROM transactions " +
                     "WHERE user_id = ? " +
                     "AND transaction_type = 'income' " +
                     "AND date BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_income");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpenseByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(amount) AS total_expense " +
                     "FROM transactions " +
                     "WHERE user_id = ? " +
                     "AND transaction_type = 'expense' " +
                     "AND date BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_expense");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

}