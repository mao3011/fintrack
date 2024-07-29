package model;

import dao.UsersDAO;
import util.HashUtil;

public class LoginLogic {
    private UsersDAO usersDAO;

    public LoginLogic() {
        this.usersDAO = new UsersDAO();
    }

    public User authenticateUser(String email, String rawPassword) {
        User user = usersDAO.findByEmail(email);
        if (user != null) {
            if (HashUtil.verifyPassword(rawPassword, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }
}