import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestRMI3 {
    public static void main(String[] args) {
        try {
            service.interfaces.ProjectService ps = (service.interfaces.ProjectService) java.rmi.Naming.lookup("rmi://localhost:4567/project-service");
            java.util.List<dto.ProjectDTO> p = ps.getAllProjects();
            System.out.println("Success! Projects: " + p.size());
            for(dto.ProjectDTO pr : p) {
                System.out.println(" - " + pr.getProjectName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
