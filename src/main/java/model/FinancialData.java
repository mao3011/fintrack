package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinancialData {
    private int id;
    private int userId;
    private LocalDate date;
    private String category;
    private BigDecimal amount;
    private String type; // "income" or "expense"
    private String description;

    // コンストラクタ
    public FinancialData() {}

    public FinancialData(int userId, LocalDate date, String category, BigDecimal amount, String type, String description) {
        this.userId = userId;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    // ゲッターとセッター
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FinancialData{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}