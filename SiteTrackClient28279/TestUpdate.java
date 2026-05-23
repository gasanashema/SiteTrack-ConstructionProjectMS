import dto.UserDTO;
import service.implementation.UserServiceImpl;
public class TestUpdate {
    public static void main(String[] args) {
        try {
            UserServiceImpl service = new UserServiceImpl();
            UserDTO admin = service.getUserById("USR-001");
            if(admin != null) {
                admin.setFullName("Admin Updated");
                UserDTO updated = service.updateUser(admin);
                System.out.println("Updated successfully! " + (updated != null));
            } else {
                System.out.println("Admin not found!");
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
