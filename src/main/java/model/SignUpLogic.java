package model;

import dao.UsersDAO;
import util.HashUtil;

public class SignUpLogic {
    private UsersDAO usersDAO;

    public SignUpLogic() {
        this.usersDAO = new UsersDAO();
    }

    public boolean processSignUp(String username, String email, String rawPassword) {
        if (usersDAO.emailExists(email)) {
            return false;
        }

        String hashedPassword = HashUtil.hashPassword(rawPassword);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);

        return usersDAO.insertUser(newUser);
    }
}