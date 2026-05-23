import config.RMIConnection;
import dto.LoginResponseDTO;
import dto.ProjectDTO;
import service.interfaces.AuthService;
import session.SessionManager;
import controller.ProjectController;
import dto.UserDTO;

import java.util.List;

public class TestLoginAndProjects {
    public static void main(String[] args) throws Exception {
        AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
        
        // 1. Simulate login for site manager
        LoginResponseDTO loginRes = authService.login("tricia", "manager123");
        System.out.println("Login response: " + loginRes.getMessage() + ", Role: " + loginRes.getRole() + ", ID: " + loginRes.getUserId());
        
        if (loginRes.isSuccess() && loginRes.getUserId() != null) {
            // Simulate OTP verification success by directly setting session
            UserDTO userDTO = new UserDTO();
            userDTO.setId(loginRes.getUserId());
            userDTO.setRole(loginRes.getRole());
            userDTO.setFullName(loginRes.getFullName());
            SessionManager.getInstance().setUser(userDTO, "dummy-otp");
            
            System.out.println("Session User ID: " + SessionManager.getInstance().getCurrentUserId());
            System.out.println("Is Admin? " + SessionManager.getInstance().isAdmin());
            
            // 2. Test ProjectController
            ProjectController pc = new ProjectController();
            List<ProjectDTO> projects = pc.getAllProjects();
            
            System.out.println("Projects returned for site manager:");
            for (ProjectDTO p : projects) {
                System.out.println(p.getId() + " - " + p.getProjectName());
            }
        }
        System.exit(0);
    }
}
