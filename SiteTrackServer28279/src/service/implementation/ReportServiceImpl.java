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
import org.hibernate.Session;
import org.hibernate.Query;
import util.HibernateUtil;
import model.Project;
import model.ProjectMaterialStock;
import java.math.BigDecimal;

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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Long totalProj = (Long) session.createQuery("SELECT COUNT(p) FROM Project p").uniqueResult();
            Long activeProj = (Long) session.createQuery("SELECT COUNT(p) FROM Project p WHERE p.status = 'ONGOING'").uniqueResult();
            Long compProj = (Long) session.createQuery("SELECT COUNT(p) FROM Project p WHERE p.status = 'COMPLETED'").uniqueResult();
            Long totalWork = (Long) session.createQuery("SELECT COUNT(w) FROM SiteWorker w WHERE w.status = 'ACTIVE'").uniqueResult();
            Long totalMat = (Long) session.createQuery("SELECT COUNT(m) FROM Material m").uniqueResult();

            BigDecimal matExp = (BigDecimal) session.createQuery("SELECT SUM(mp.totalCost) FROM MaterialPurchase mp").uniqueResult();
            if (matExp == null) matExp = BigDecimal.ZERO;

            BigDecimal labExp = (BigDecimal) session.createQuery("SELECT SUM(wp.netPay) FROM WorkerPayment wp").uniqueResult();
            if (labExp == null) labExp = BigDecimal.ZERO;

            List<Project> recentProjectsList = session.createQuery("FROM Project ORDER BY expectedEndDate DESC").setMaxResults(5).list();
            List<dto.ProjectDTO> recentProjDto = new ArrayList<>();
            for (Project p : recentProjectsList) {
                recentProjDto.add(new dto.ProjectDTO(
                    p.getId(), 
                    p.getProjectName(), 
                    p.getLocation(), 
                    p.getDescription(), 
                    p.getStartDate(), 
                    p.getExpectedEndDate(), 
                    p.getStatus().name(), 
                    p.getCreatedBy() != null ? p.getCreatedBy().getUsername() : "Unknown", 
                    p.getCreatedAt()
                ));
            }

            List<ProjectMaterialStock> lowStockList = session.createQuery("FROM ProjectMaterialStock s WHERE s.quantityAvailable < s.minimumQuantity").setMaxResults(10).list();
            List<dto.ProjectMaterialStockDTO> lowStockDto = new ArrayList<>();
            for (ProjectMaterialStock s : lowStockList) {
                lowStockDto.add(new dto.ProjectMaterialStockDTO(
                    s.getId(), 
                    s.getProject().getId(), 
                    s.getProject().getProjectName(), 
                    s.getMaterial().getId(), 
                    s.getMaterial().getMaterialName(), 
                    s.getMaterial().getUnit(), 
                    s.getQuantityAvailable(), 
                    s.getMinimumQuantity(), 
                    s.getAverageUnitPrice(), 
                    s.getQuantityAvailable().compareTo(s.getMinimumQuantity()) < 0
                ));
            }

            return new DashboardSummaryDTO(
                totalProj != null ? totalProj.intValue() : 0,
                activeProj != null ? activeProj.intValue() : 0,
                compProj != null ? compProj.intValue() : 0,
                totalWork != null ? totalWork.intValue() : 0,
                totalMat != null ? totalMat.intValue() : 0,
                matExp,
                labExp,
                recentProjDto,
                lowStockDto
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new DashboardSummaryDTO(0, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>());
        } finally {
            session.close();
        }
    }

    @Override
    public ProjectSummaryDTO getSiteManagerDashboardSummary(String projectId) throws RemoteException {
        // Stub
        return new ProjectSummaryDTO();
    }
}
