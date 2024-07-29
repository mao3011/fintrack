package util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.FintrackException;

public class ErrorHandler {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());

    public static void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        if (e instanceof FintrackException) {
            handleFintrackException(request, response, (FintrackException) e);
        } else {
            handleGenericException(request, response, e);
        }
    }

    private static void handleFintrackException(HttpServletRequest request, HttpServletResponse response, FintrackException e) throws ServletException, IOException {
        logger.log(Level.SEVERE, "FintrackException occurred: " + e.getMessage(), e);
        request.setAttribute("errorMessage", e.getErrorCode().getMessage());

        switch (e.getErrorCode()) {
            case AUTHENTICATION_FAILED:
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                break;
            case INVALID_INPUT:
                request.getRequestDispatcher("/WEB-INF/jsp/error/invalidInput.jsp").forward(request, response);
                break;
            case DATABASE_ERROR:
            case INTERNAL_SERVER_ERROR:
            default:
                request.getRequestDispatcher("/WEB-INF/jsp/error/serverError.jsp").forward(request, response);
                break;
        }
    }

    private static void handleGenericException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        logger.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage(), e);
        request.setAttribute("errorMessage", "予期しないエラーが発生しました。管理者にお問い合わせください。");
        request.getRequestDispatcher("/WEB-INF/jsp/error/serverError.jsp").forward(request, response);
    }
}