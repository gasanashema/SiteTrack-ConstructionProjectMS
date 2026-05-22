import util.BCrypt;

public class GenHash {
    public static void main(String[] args) {
        System.out.println("HASH=" + BCrypt.hashpw("12345678", BCrypt.gensalt()));
    }
}
