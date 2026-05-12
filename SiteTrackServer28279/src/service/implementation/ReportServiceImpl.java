package service.implementation;

import dto.MaterialUsageReportDTO;
import dto.LaborCostReportDTO;
import dto.StockMovementReportDTO;
import dto.ProjectSummaryDTO;
import dto.DashboardSummaryDTO;
import service.interfaces.ReportService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl extends UnicastRemoteObject implements ReportService {

    public ReportServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public MaterialUsageReportDTO getMaterialUsageReport(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        // Stub: In real impl, would query MaterialUsageDao and aggregate totals
        return new MaterialUsageReportDTO(projectId, from, to, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, new ArrayList<>());
    }

    @Override
    public LaborCostReportDTO getLaborCostReport(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        // Stub: In real impl, would query WorkerPaymentDao and aggregate
        return new LaborCostReportDTO(projectId, from, to, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, new ArrayList<>());
    }

    @Override
    public StockMovementReportDTO getStockMovementReport(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        // Stub: In real impl, would query MaterialStockMovementDao
        return new StockMovementReportDTO(projectId, from, to, new ArrayList<>());
    }

    @Override
    public List<ProjectSummaryDTO> getAllProjectsSummary() throws RemoteException {
        // Stub
        return new ArrayList<>();
    }

    @Override
    public DashboardSummaryDTO getAdminDashboardSummary() throws RemoteException {
        // Stub: aggregates data across all entities
        return new DashboardSummaryDTO(0, 0, 0, 0, 0, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public ProjectSummaryDTO getSiteManagerDashboardSummary(String projectId) throws RemoteException {
        // Stub
        return new ProjectSummaryDTO();
    }
}
