package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import model.BalanceSheet;

public class BalanceSheetDAO {
    private static final String JDBC_URL = "jdbc:mariadb://localhost/fintrack";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public BalanceSheetDAO() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした", e);
        }
    }

    public BalanceSheet getBalanceSheetByUserIdAndDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM balance_sheets WHERE user_id = ? AND date = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBalanceSheetFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BalanceSheet extractBalanceSheetFromResultSet(ResultSet rs) throws SQLException {
        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setId(rs.getInt("id"));
        balanceSheet.setUserId(rs.getInt("user_id"));
        balanceSheet.setDate(rs.getDate("date").toLocalDate());
        balanceSheet.setCashDeposits(rs.getBigDecimal("cash_deposits"));
        balanceSheet.setNotesReceivable(rs.getBigDecimal("notes_receivable"));
        balanceSheet.setAccountsReceivable(rs.getBigDecimal("accounts_receivable"));
        balanceSheet.setSecurities(rs.getBigDecimal("securities"));
        balanceSheet.setInventory(rs.getBigDecimal("inventory"));
        balanceSheet.setAllowanceDoubtful(rs.getBigDecimal("allowance_doubtful"));
        balanceSheet.setBuildings(rs.getBigDecimal("buildings"));
        balanceSheet.setLand(rs.getBigDecimal("land"));
        balanceSheet.setNotesPayable(rs.getBigDecimal("notes_payable"));
        balanceSheet.setAccountsPayable(rs.getBigDecimal("accounts_payable"));
        balanceSheet.setShortTermLoans(rs.getBigDecimal("short_term_loans"));
        balanceSheet.setLongTermLoans(rs.getBigDecimal("long_term_loans"));
        balanceSheet.setCapitalStock(rs.getBigDecimal("capital_stock"));
        balanceSheet.setRetainedEarnings(rs.getBigDecimal("retained_earnings"));
        return balanceSheet;
    }
}
