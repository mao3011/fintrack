package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.UserController;
import exception.FintrackException;
import model.User;
import util.ErrorHandler;
import util.SecurityUtils;
import util.ValidationUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserController userController;

    @Override
    public void init() throws ServletException {
        super.init();
        userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ログインページの表示
        request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!SecurityUtils.isValidCSRFToken(request)) {
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "Invalid CSRF token");
            }

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (!ValidationUtils.isValidEmail(email) || !ValidationUtils.isValidPassword(password)) {
                throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid email or password format");
            }

            boolean authenticated = userController.authenticateUser(email, password);
            if (authenticated) {
                User user = userController.getUserByEmail(email);
                SecurityUtils.setUserSession(request, user.getId());
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "Invalid email or password");
            }
        } catch (Exception e) {
            ErrorHandler.handleException(request, response, e);
        }
    }
}