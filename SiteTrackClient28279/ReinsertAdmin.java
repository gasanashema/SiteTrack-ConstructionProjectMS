import model.*;
import dao.*;
import java.time.LocalDateTime;

public class ReinsertAdmin {
    public static void main(String[] args) {
        try {
            User admin = new User();
            admin.setId("USR-001");
            admin.setFullName("Admin User");
            admin.setUsername("shema");
            admin.setEmail("shemaphilbert8@gmail.com");
            admin.setPassword(util.BCrypt.hashpw("disaster", util.BCrypt.gensalt()));
            admin.setRole(ERole.ADMIN);
            admin.setStatus(EUserStatus.ACTIVE);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            
            UserDao dao = new UserDao();
            dao.save(admin);
            
            User sm = new User();
            sm.setId("USR-002");
            sm.setFullName("Tricia Manager");
            sm.setUsername("tricia");
            sm.setEmail("tricia@example.com");
            sm.setPassword(util.BCrypt.hashpw("manager123", util.BCrypt.gensalt()));
            sm.setRole(ERole.SITE_MANAGER);
            sm.setStatus(EUserStatus.ACTIVE);
            sm.setCreatedAt(LocalDateTime.now());
            sm.setUpdatedAt(LocalDateTime.now());
            
            dao.save(sm);
            
            System.out.println("Admin and Site Manager inserted.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
