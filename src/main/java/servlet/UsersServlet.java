
package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.UserController;
import exception.FintrackException;
import model.User;
import util.ErrorHandler;
import util.SecurityUtils;
import util.ValidationUtils;

@WebServlet("/user/*")
public class UsersServlet extends HttpServlet {
    private UserController userController;

    @Override
    public void init() throws ServletException {
        super.init();
        userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = SecurityUtils.getActionFromPath(request);
        switch (action) {
            case "profile":
                showProfile(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!SecurityUtils.isValidCSRFToken(request)) {
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "Invalid CSRF token");
            }

            String action = SecurityUtils.getActionFromPath(request);
            switch (action) {
                case "register":
                    registerUser(request, response);
                    break;
                case "login":
                    loginUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            ErrorHandler.handleException(request, response, e);
        }
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        User user = userController.getUserById(userId);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("user", user);
        request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!ValidationUtils.isValidUsername(username) || !ValidationUtils.isValidEmail(email) || !ValidationUtils.isValidPassword(password)) {
            request.setAttribute("error", "無効な入力です。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        username = SecurityUtils.sanitizeHtml(username);
        email = SecurityUtils.sanitizeHtml(email);

        boolean success = userController.registerUser(username, email, password);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("error", "ユーザー登録に失敗しました。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!ValidationUtils.isValidEmail(email) || !ValidationUtils.isValidPassword(password)) {
            request.setAttribute("error", "無効な入力です。");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        boolean authenticated = userController.authenticateUser(email, password);
        if (authenticated) {
            User user = userController.getUserByEmail(email);
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.setAttribute("error", "メールアドレスまたはパスワードが間違っています。");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        User user = userController.getUserById(userId);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");

        if (!ValidationUtils.isValidUsername(username) || !ValidationUtils.isValidEmail(email)) {
            request.setAttribute("error", "無効な入力です。");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
            return;
        }

        username = SecurityUtils.sanitizeHtml(username);
        email = SecurityUtils.sanitizeHtml(email);

        user.setUsername(username);
        user.setEmail(email);

        boolean success = userController.updateUser(user);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/user/profile");
        } else {
            request.setAttribute("error", "ユーザー情報の更新に失敗しました。");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}