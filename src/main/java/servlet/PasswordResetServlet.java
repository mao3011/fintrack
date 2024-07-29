package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PasswordResetServlet
 */
@WebServlet("/password-reset")
public class PasswordResetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordResetServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // パスワードリセットフォームを表示
        request.getRequestDispatcher("/WEB-INF/jsp/password-reset.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "メールアドレスを入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/password-reset.jsp").forward(request, response);
            return;
        }

        // TODO: メールアドレスの形式を検証する
        // TODO: データベースでメールアドレスの存在を確認する
        // TODO: パスワードリセットトークンを生成し、データベースに保存する
        // TODO: リセットリンクを含むメールを送信する

        // 仮の実装：すべての処理が成功したと仮定
        request.setAttribute("message", "パスワードリセットのリンクを " + email + " に送信しました。");
        request.getRequestDispatcher("/WEB-INF/jsp/password-reset.jsp").forward(request, response);
    }

}