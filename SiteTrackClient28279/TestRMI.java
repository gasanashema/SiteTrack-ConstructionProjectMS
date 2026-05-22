import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestRMI {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service.interfaces.ReportService reportService = (service.interfaces.ReportService) registry.lookup("ReportService");
            dto.DashboardSummaryDTO summary = reportService.getAdminDashboardSummary();
            System.out.println("Success! Active projects: " + summary.getActiveProjects());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
