package service.interfaces;

import dto.MaterialUsageReportDTO;
import dto.LaborCostReportDTO;
import dto.StockMovementReportDTO;
import dto.ProjectSummaryDTO;
import dto.DashboardSummaryDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ReportService extends Remote {
    MaterialUsageReportDTO getMaterialUsageReport(String projectId, LocalDate from, LocalDate to) throws RemoteException;
    LaborCostReportDTO getLaborCostReport(String projectId, LocalDate from, LocalDate to) throws RemoteException;
    StockMovementReportDTO getStockMovementReport(String projectId, LocalDate from, LocalDate to) throws RemoteException;
    List<ProjectSummaryDTO> getAllProjectsSummary() throws RemoteException;
    DashboardSummaryDTO getAdminDashboardSummary() throws RemoteException;
    ProjectSummaryDTO getSiteManagerDashboardSummary(String projectId) throws RemoteException;
}
