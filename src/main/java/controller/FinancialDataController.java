package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import dao.FinancialDataDAO;
import model.FinancialData;

public class FinancialDataController {
    private FinancialDataDAO financialDataDAO;

    public FinancialDataController() {
        this.financialDataDAO = new FinancialDataDAO();
    }

    public boolean addFinancialData(int userId, LocalDate date, String category, BigDecimal amount, String type, String description) {
        FinancialData newData = new FinancialData(userId, date, category, amount, type, description);
        return financialDataDAO.insertFinancialData(newData);
    }

    public FinancialData getFinancialDataById(int id) {
        return financialDataDAO.getFinancialDataById(id);
    }

    public List<FinancialData> getFinancialDataByUserId(int userId) {
        return financialDataDAO.getFinancialDataByUserId(userId);
    }

    public List<FinancialData> getFinancialDataByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        return financialDataDAO.getFinancialDataByUserIdAndDateRange(userId, startDate, endDate);
    }

    public boolean updateFinancialData(FinancialData data) {
        return financialDataDAO.updateFinancialData(data);
    }

    public boolean deleteFinancialData(int id) {
        return financialDataDAO.deleteFinancialData(id);
    }

    public BigDecimal getTotalIncomeByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        List<FinancialData> dataList = getFinancialDataByUserIdAndDateRange(userId, startDate, endDate);
        return dataList.stream()
                .filter(data -> "income".equals(data.getType()))
                .map(FinancialData::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenseByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        List<FinancialData> dataList = getFinancialDataByUserIdAndDateRange(userId, startDate, endDate);
        return dataList.stream()
                .filter(data -> "expense".equals(data.getType()))
                .map(FinancialData::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}