import java.util.List;
import dto.ProjectDTO;
public class TestRMI4 {
    public static void main(String[] args) {
        try {
            service.interfaces.ProjectService ps = (service.interfaces.ProjectService) java.rmi.Naming.lookup("rmi://localhost:4567/project-service");
            List<ProjectDTO> p = ps.getAllProjects();
            System.out.println("Success! Projects: " + p.size());
            for(ProjectDTO pr : p) {
                System.out.println(" - " + pr.getProjectName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
