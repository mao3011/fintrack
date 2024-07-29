package util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CSRF_TOKEN_ATTRIBUTE = "csrfToken";

    public static String generateCSRFToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = generateRandomToken();
        session.setAttribute(CSRF_TOKEN_ATTRIBUTE, token);
        return token;
    }

    public static boolean isValidCSRFToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTRIBUTE);
        String requestToken = request.getParameter(CSRF_TOKEN_ATTRIBUTE);
        return sessionToken != null && sessionToken.equals(requestToken);
    }

    private static String generateRandomToken() {
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public static String getActionFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "";
        }
        String[] splits = pathInfo.split("/");
        return splits.length > 1 ? splits[1] : "";
    }

    public static String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;")
                    .replace("/", "&#x2F;");
    }

    public static String hashPassword(String password) {
        return HashUtil.hashPassword(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return HashUtil.verifyPassword(password, hashedPassword);
    }

    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    public static int getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new IllegalStateException("User is not logged in");
        }
        return (int) session.getAttribute("userId");
    }

    public static void setUserSession(HttpServletRequest request, int userId) {
        HttpSession session = request.getSession();
        session.setAttribute("userId", userId);
    }
}