package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.FinancialDataController;
import exception.FintrackException;
import model.FinancialData;
import util.ErrorHandler;
import util.SecurityUtils;
import util.ValidationUtils;

@WebServlet("/financial-data/*")
public class FinancialDataServlet extends HttpServlet {
    private FinancialDataController financialDataController;

    @Override
    public void init() throws ServletException {
        super.init();
        financialDataController = new FinancialDataController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!SecurityUtils.isUserLoggedIn(request)) {
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "User not logged in");
            }

            String action = SecurityUtils.getActionFromPath(request);
            switch (action) {
                case "":
                case "list":
                    listFinancialData(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            ErrorHandler.handleException(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!SecurityUtils.isUserLoggedIn(request)) {
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "User not logged in");
            }

            if (!SecurityUtils.isValidCSRFToken(request)) {
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "Invalid CSRF token");
            }

            String action = SecurityUtils.getActionFromPath(request);
            switch (action) {
                case "add":
                    addFinancialData(request, response);
                    break;
                case "update":
                    updateFinancialData(request, response);
                    break;
                case "delete":
                    deleteFinancialData(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            ErrorHandler.handleException(request, response, e);
        }
    }

    private void listFinancialData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = SecurityUtils.getUserIdFromSession(request);

        List<FinancialData> financialDataList = financialDataController.getFinancialDataByUserId(userId);

        request.setAttribute("financialDataList", financialDataList);
        request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));

        request.getRequestDispatcher("/WEB-INF/jsp/financialData/list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dataIdStr = request.getParameter("id");
        if (!ValidationUtils.isPositiveInteger(dataIdStr)) {
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid financial data ID");
        }

        int dataId = Integer.parseInt(dataIdStr);
        FinancialData data = financialDataController.getFinancialDataById(dataId);

        if (data == null) {
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Financial data not found");
        }

        int userId = SecurityUtils.getUserIdFromSession(request);
        if (data.getUserId() != userId) {
            throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "You don't have permission to edit this data");
        }

        request.setAttribute("financialData", data);
        request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));

        request.getRequestDispatcher("/WEB-INF/jsp/financialData/edit.jsp").forward(request, response);
    }

    private void addFinancialData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = SecurityUtils.getUserIdFromSession(request);
        String dateStr = request.getParameter("date");
        String category = SecurityUtils.sanitizeHtml(request.getParameter("category"));
        String amountStr = request.getParameter("amount");
        String type = request.getParameter("type");
        String description = SecurityUtils.sanitizeHtml(request.getParameter("description"));

        if (!ValidationUtils.isValidDate(dateStr) || !ValidationUtils.isValidCategory(category)
                || !ValidationUtils.isValidMoney(amountStr) || !ValidationUtils.isValidFinancialType(type)) {
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid input data");
        }

        LocalDate date = LocalDate.parse(dateStr);
        BigDecimal amount = new BigDecimal(amountStr);

        boolean success = financialDataController.addFinancialData(userId, date, category, amount, type, description);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/financial-data");
        } else {
            throw new FintrackException(FintrackException.ErrorCode.DATABASE_ERROR, "Failed to add financial data");
        }
    }

    private void updateFinancialData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Similar implementation to addFinancialData, but with existing data ID
        // Remember to validate user's permission to update the data
    }

    private void deleteFinancialData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for deleting financial data
        // Remember to validate user's permission to delete the data
    }
}