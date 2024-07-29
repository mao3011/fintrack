package util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class HashUtil {
    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i);

    // パスワードをハッシュ化
    public static String hashPassword(String rawPassword) {
        return argon2.hash(10, 65536, 1, rawPassword.toCharArray());
    }

    // パスワードを検証
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return argon2.verify(hashedPassword, rawPassword.toCharArray());
    }
}