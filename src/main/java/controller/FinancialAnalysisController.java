package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dao.BalanceSheetDAO;
import dao.FinancialDataDAO;
import model.BalanceSheet;
import model.FinancialData;

public class FinancialAnalysisController {
    private FinancialDataDAO financialDataDAO;
    private BalanceSheetDAO balanceSheetDAO;

    public FinancialAnalysisController() {
        this.financialDataDAO = new FinancialDataDAO();
        this.balanceSheetDAO = new BalanceSheetDAO();
    }

    public AssetChangeData getAssetChangeData(int userId, YearMonth period) {
        List<FinancialData> financialData = financialDataDAO.getFinancialDataByUserIdAndDateRange(
                userId, period.atDay(1), period.atEndOfMonth());

        List<String> labels = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();

        for (FinancialData data : financialData) {
            labels.add(data.getDate().toString());
            values.add(data.getAmount());
        }

        return new AssetChangeData(labels, values);
    }

    public ExpenseCategoryData getExpenseCategoryData(int userId, YearMonth period) {
        List<FinancialData> financialData = financialDataDAO.getFinancialDataByUserIdAndDateRange(
                userId, period.atDay(1), period.atEndOfMonth());

        List<String> labels = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();

        Map<String, BigDecimal> expenseCategories = getExpenseCategories(financialData);
        for (Map.Entry<String, BigDecimal> entry : expenseCategories.entrySet()) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
        }

        return new ExpenseCategoryData(labels, values);
    }

    public BalanceSheetData getBalanceSheetData(int userId, LocalDate date) {
        BalanceSheet balanceSheet = balanceSheetDAO.getBalanceSheetByUserIdAndDate(userId, date);
        return new BalanceSheetData(balanceSheet);
    }

    private Map<String, BigDecimal> getExpenseCategories(List<FinancialData> financialData) {
        return financialData.stream()
                .filter(data -> "expense".equals(data.getType()))
                .collect(Collectors.toMap(
                        FinancialData::getCategory,
                        FinancialData::getAmount,
                        BigDecimal::add
                ));
    }

    public static class AssetChangeData {
        private List<String> labels;
        private List<BigDecimal> values;

        public AssetChangeData(List<String> labels, List<BigDecimal> values) {
            this.labels = labels;
            this.values = values;
        }

		public List<String> getLabels() {
			return labels;
		}

		public void setLabels(List<String> labels) {
			this.labels = labels;
		}

		public List<BigDecimal> getValues() {
			return values;
		}

		public void setValues(List<BigDecimal> values) {
			this.values = values;
		}


        // ゲッターとセッター
    }

    public static class ExpenseCategoryData {
        private List<String> labels;
        private List<BigDecimal> values;

        public ExpenseCategoryData(List<String> labels, List<BigDecimal> values) {
            this.labels = labels;
            this.values = values;
        }

		public List<String> getLabels() {
			return labels;
		}

		public void setLabels(List<String> labels) {
			this.labels = labels;
		}

		public List<BigDecimal> getValues() {
			return values;
		}

		public void setValues(List<BigDecimal> values) {
			this.values = values;
		}

        // ゲッターとセッター
    }

    public static class BalanceSheetData {
        private BalanceSheet balanceSheet;

        public BalanceSheetData(BalanceSheet balanceSheet) {
            this.balanceSheet = balanceSheet;
        }

		public BalanceSheet getBalanceSheet() {
			return balanceSheet;
		}

		public void setBalanceSheet(BalanceSheet balanceSheet) {
			this.balanceSheet = balanceSheet;
		}


        // ゲッターとセッター
    }
}