package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]{1,50}$");

    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidCategory(String category) {
        return category != null && CATEGORY_PATTERN.matcher(category).matches();
    }

    public static boolean isValidAssetName(String name) {
        return name != null && CATEGORY_PATTERN.matcher(name).matches();
    }

    public static boolean isValidAssetType1(String type) {
        return type != null && Arrays.asList("bank", "card", "security", "emoney", "point", "liability").contains(type);
    }

    public static boolean isValidFinancialType1(String type) {
        return "income".equals(type) || "expense".equals(type);
    }
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isValidMoney(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isPositiveInteger(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidAssetName1(String name) {
        return name != null && name.matches("^[a-zA-Z0-9\\s]{1,50}$");
    }

    public static boolean isValidAssetType(String type) {
        return type != null && Arrays.asList("bank", "card", "security", "emoney", "point", "liability").contains(type);
    }

    public static boolean isValidFinancialType(String type) {
        return "income".equals(type) || "expense".equals(type);
    }
}