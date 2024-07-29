package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int userId;
    private int categoryId;
    private int paymentMethodId;
    private BigDecimal amount;
    private String transactionType; // 'income' or 'expense'
    private LocalDate date;
    private String memo;
    private LocalDateTime createdAt;

    // Default constructor
    public Transaction() {}

    // Constructor with all fields
    public Transaction(int id, int userId, int categoryId, int paymentMethodId, BigDecimal amount,
                       String transactionType, LocalDate date, String memo, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
        this.memo = memo;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategory() {
        // ここで適切なカテゴリー取得ロジックを実装する
        // 例えば、カテゴリーIDからカテゴリー名を取得する
        return "カテゴリー名";
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", paymentMethodId=" + paymentMethodId +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", date=" + date +
                ", memo='" + memo + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}