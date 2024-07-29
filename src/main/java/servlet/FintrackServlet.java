package servlet;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.HomeController;
import controller.HomeController.CalendarEvent;
import controller.HomeController.DashboardData;
import controller.UserController;
import exception.FintrackException;
import model.User;
import util.ErrorHandler;
import util.SecurityUtils;
import util.ValidationUtils;

@WebServlet({"/", "/home"})
public class FintrackServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FintrackServlet.class.getName());
    
    private UserController userController;
    private HomeController homeController;

    @Override
    public void init() throws ServletException {
        super.init();
        userController = new UserController();
        homeController = new HomeController();
        logger.info("FintrackServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("FintrackServlet doGet method called. RequestURI: " + request.getRequestURI());
        try {
            if (!SecurityUtils.isUserLoggedIn(request)) {
                logger.info("User not logged in. Forwarding to login page.");
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }

            int userId = SecurityUtils.getUserIdFromSession(request);
            logger.info("Processing request for user ID: " + userId);

            DashboardData dashboardData = homeController.getDashboardData(userId, YearMonth.now());
            List<CalendarEvent> calendarEvents = homeController.getCalendarEvents(userId, YearMonth.now());

            request.setAttribute("dashboardData", dashboardData);
            request.setAttribute("calendarEvents", calendarEvents);
            request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));

            logger.info("Forwarding to index.jsp");
            request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Error in FintrackServlet doGet: " + e.getMessage());
            ErrorHandler.handleException(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("FintrackServlet doPost method called");
        try {
            String action = request.getParameter("action");
            if (action == null) {
                logger.warning("Missing action parameter");
                throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Missing action parameter");
            }

            logger.info("Processing action: " + action);
            switch (action) {
                case "login":
                    handleLogin(request, response);
                    break;
                case "register":
                    handleRegistration(request, response);
                    break;
                default:
                    logger.warning("Invalid action parameter: " + action);
                    throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid action parameter");
            }
        } catch (Exception e) {
            logger.severe("Error in FintrackServlet doPost: " + e.getMessage());
            ErrorHandler.handleException(request, response, e);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Handling login request");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!ValidationUtils.isValidEmail(email) || !ValidationUtils.isValidPassword(password)) {
            logger.warning("Invalid email or password format");
            request.setAttribute("error", "Invalid email or password format");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        boolean authenticated = userController.authenticateUser(email, password);
        if (authenticated) {
            User user = userController.getUserByEmail(email);
            SecurityUtils.setUserSession(request, user.getId());
            logger.info("User authenticated successfully. User ID: " + user.getId());
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            logger.warning("Authentication failed for email: " + email);
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Handling registration request");
        String username = SecurityUtils.sanitizeHtml(request.getParameter("username"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!ValidationUtils.isValidUsername(username) || !ValidationUtils.isValidEmail(email) || !ValidationUtils.isValidPassword(password)) {
            logger.warning("Invalid registration data");
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid registration data");
        }

        boolean success = userController.registerUser(username, email, password);
        if (success) {
            logger.info("User registered successfully: " + email);
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            logger.warning("Failed to register user: " + email);
            throw new FintrackException(FintrackException.ErrorCode.DATABASE_ERROR, "Failed to register user");
        }
    }
}