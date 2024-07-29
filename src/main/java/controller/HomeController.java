package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dao.FinancialDataDAO;
import dao.TransactionDAO;
import model.Transaction;

public class HomeController {
    private FinancialDataDAO financialDataDAO;
    private TransactionDAO transactionDAO;

    public HomeController() {
        this.financialDataDAO = new FinancialDataDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public DashboardData getDashboardData(int userId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal totalIncome = financialDataDAO.getTotalIncomeByUserIdAndDateRange(userId, startDate, endDate);
        BigDecimal totalExpense = financialDataDAO.getTotalExpenseByUserIdAndDateRange(userId, startDate, endDate);

        List<Transaction> transactions = transactionDAO.getTransactionsByUserIdAndDateRange(userId, startDate, endDate);
        Map<String, BigDecimal> expenseCategories = getExpenseCategories(transactions);

        return new DashboardData(totalIncome, totalExpense, expenseCategories);
    }

    public List<CalendarEvent> getCalendarEvents(int userId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionDAO.getTransactionsByUserIdAndDateRange(userId, startDate, endDate);

        return transactions.stream()
                .map(t -> new CalendarEvent(t.getCategory(), t.getDate(), t.getAmount(), t.getTransactionType()))
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> getExpenseCategories(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> "expense".equals(t.getTransactionType()))
                .collect(Collectors.toMap(
                        Transaction::getCategory,
                        Transaction::getAmount,
                        BigDecimal::add
                ));
    }

    public static class DashboardData {
        private BigDecimal income;
        private BigDecimal expense;
        private Map<String, BigDecimal> expenseCategories;

        public DashboardData(BigDecimal income, BigDecimal expense, Map<String, BigDecimal> expenseCategories) {
            this.income = income;
            this.expense = expense;
            this.expenseCategories = expenseCategories;
        }

        public BigDecimal getIncome() {
            return income;
        }

        public void setIncome(BigDecimal income) {
            this.income = income;
        }

        public BigDecimal getExpense() {
            return expense;
        }

        public void setExpense(BigDecimal expense) {
            this.expense = expense;
        }

        public Map<String, BigDecimal> getExpenseCategories() {
            return expenseCategories;
        }

        public void setExpenseCategories(Map<String, BigDecimal> expenseCategories) {
            this.expenseCategories = expenseCategories;
        }
    }

    public static class CalendarEvent {
        private String title;
        private LocalDate date;
        private BigDecimal amount;
        private String type;

        public CalendarEvent(String title, LocalDate date, BigDecimal amount, String type) {
            this.title = title;
            this.date = date;
            this.amount = amount;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
