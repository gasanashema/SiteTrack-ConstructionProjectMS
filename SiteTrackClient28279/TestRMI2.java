import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestRMI2 {
    public static void main(String[] args) {
        try {
            service.interfaces.ReportService reportService = (service.interfaces.ReportService) java.rmi.Naming.lookup("rmi://localhost:4567/report-service");
            dto.DashboardSummaryDTO summary = reportService.getAdminDashboardSummary();
            System.out.println("Success! Active projects: " + summary.getActiveProjects());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
