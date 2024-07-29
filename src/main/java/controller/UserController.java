package controller;

import java.util.List;

import dao.UsersDAO;
import model.User;

public class UserController {
    private UsersDAO userDAO;

    public UserController() {
        this.userDAO = new UsersDAO();
    }

    public boolean registerUser(String username, String email, String password) {
        User newUser = new User(username, email, password);
        return userDAO.insertUser(newUser);
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            // ここでパスワードの検証を行う
            // 実際の実装では、ハッシュ化されたパスワードを比較する必要があります
            return user.getPasswordHash().equals(password);
        }
        return false;
    }
    public boolean resetPassword(String email) {
        // パスワードリセット処理の実装
        // 例えば、パスワードリセットトークンの生成や
        // メール送信などの処理が含まれる
        return true;
    }
}