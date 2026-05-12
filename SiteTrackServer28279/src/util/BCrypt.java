package util;

/**
 * Stub implementation of BCrypt to allow the service layer to compile.
 * In a real environment, replace this with org.mindrot.jbcrypt.BCrypt.
 */
public class BCrypt {
    public static String hashpw(String password, String salt) {
        return password + "_hashed"; // Basic stub logic
    }

    public static String gensalt() {
        return "salt";
    }

    public static boolean checkpw(String plaintext, String hashed) {
        if (plaintext == null || hashed == null) return false;
        return (plaintext + "_hashed").equals(hashed);
    }
}
