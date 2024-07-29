package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.UserController;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserController userController;

    @Override
	public void init() throws ServletException {
        super.init();
        userController = new UserController();
    }

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean success = userController.registerUser(username, email, password);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("error", "ユーザー登録に失敗しました。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}