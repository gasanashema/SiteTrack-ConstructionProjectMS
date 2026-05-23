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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM MaterialUsage m WHERE m.project.id = :projectId AND m.usageDate >= :from AND m.usageDate <= :to ORDER BY m.usageDate DESC";
            Query query = session.createQuery(hql);
            query.setParameter("projectId", projectId);
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<model.MaterialUsage> list = query.list();

            BigDecimal totalQuantity = BigDecimal.ZERO;
            BigDecimal totalCost = BigDecimal.ZERO;
            List<dto.MaterialUsageDTO> dtoList = new ArrayList<>();
            String projName = "";

            for (model.MaterialUsage m : list) {
                totalQuantity = totalQuantity.add(m.getQuantityUsed());
                totalCost = totalCost.add(m.getTotalCost());
                if (projName.isEmpty()) projName = m.getProject().getProjectName();

                dtoList.add(new dto.MaterialUsageDTO(
                    m.getId(), m.getProject().getId(), m.getProject().getProjectName(),
                    m.getMaterial().getId(), m.getMaterial().getMaterialName(), m.getMaterial().getUnit(),
                    m.getQuantityUsed(), m.getUnitPrice(), m.getTotalCost(),
                    m.getUsageDate(), m.getActivityDescription(),
                    m.getRecordedBy().getId(), m.getRecordedBy().getUsername()
                ));
            }

            if (projName.isEmpty() && !list.isEmpty()) {
                projName = list.get(0).getProject().getProjectName();
            }

            return new MaterialUsageReportDTO(projName, from, to, totalQuantity, totalCost, dtoList);
        } finally {
            session.close();
        }
    }

    @Override
    public LaborCostReportDTO getLaborCostReport(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM WorkerPayment w WHERE w.project.id = :projectId AND w.workDate >= :from AND w.workDate <= :to ORDER BY w.workDate DESC";
            Query query = session.createQuery(hql);
            query.setParameter("projectId", projectId);
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<model.WorkerPayment> list = query.list();

            BigDecimal totalOwed = BigDecimal.ZERO;
            BigDecimal totalPaid = BigDecimal.ZERO;
            List<dto.WorkerPaymentDTO> dtoList = new ArrayList<>();
            String projName = "";

            for (model.WorkerPayment p : list) {
                BigDecimal owed = p.getAmountOwed() != null ? p.getAmountOwed() : BigDecimal.ZERO;
                BigDecimal paid = p.getAmountPaid() != null ? p.getAmountPaid() : BigDecimal.ZERO;
                totalOwed = totalOwed.add(owed);
                totalPaid = totalPaid.add(paid);
                if (projName.isEmpty()) projName = p.getProject().getProjectName();

                dtoList.add(new dto.WorkerPaymentDTO(
                    p.getId(), p.getProject().getId(), p.getProject().getProjectName(),
                    p.getWorker().getId(), p.getWorker().getFullName(),
                    p.getAttendance() != null ? p.getAttendance().getId() : null,
                    p.getWorkDate(), p.getDailyRate(), p.getAmountOwed(), p.getAmountPaid(),
                    p.getPaymentStatus() != null ? p.getPaymentStatus().name() : "",
                    p.getPaidBy() != null ? p.getPaidBy().getUsername() : null,
                    p.getNotes() != null ? p.getNotes() : ""
                ));
            }

            BigDecimal totalPending = totalOwed.subtract(totalPaid);
            return new LaborCostReportDTO(projName, from, to, totalOwed, totalPaid, totalPending, dtoList);
        } finally {
            session.close();
        }
    }

    @Override
    public StockMovementReportDTO getStockMovementReport(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM MaterialStockMovement m WHERE m.project.id = :projectId AND m.movementDate >= :from AND m.movementDate <= :to ORDER BY m.movementDate DESC";
            Query query = session.createQuery(hql);
            query.setParameter("projectId", projectId);
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<model.MaterialStockMovement> list = query.list();

            List<dto.MaterialStockMovementDTO> dtoList = new ArrayList<>();
            String projName = "";

            for (model.MaterialStockMovement m : list) {
                if (projName.isEmpty()) projName = m.getProject().getProjectName();

                dtoList.add(new dto.MaterialStockMovementDTO(
                    m.getId(), m.getProject().getId(), m.getProject().getProjectName(),
                    m.getMaterial().getId(), m.getMaterial().getMaterialName(),
                    m.getMovementType() != null ? m.getMovementType().name() : "", m.getQuantity(), m.getUnitPrice(), m.getTotalPrice(),
                    m.getMovementDate(), m.getDescription(), m.getReferenceType(), m.getReferenceId(),
                    m.getRecordedBy() != null ? m.getRecordedBy().getUsername() : null
                ));
            }

            return new StockMovementReportDTO(projName, from, to, dtoList);
        } finally {
            session.close();
        }
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

            BigDecimal matExp = (BigDecimal) session.createQuery("SELECT SUM(mp.totalPrice) FROM MaterialPurchase mp").uniqueResult();
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Project project = (Project) session.get(Project.class, projectId);
            if (project == null) return null;

            Long totalWorkers = (Long) session.createQuery("SELECT COUNT(a) FROM WorkerAssignment a WHERE a.project.id = :projectId AND a.status = 'ACTIVE'")
                .setParameter("projectId", projectId).uniqueResult();

            Long presentToday = (Long) session.createQuery("SELECT COUNT(a) FROM WorkerAttendance a WHERE a.project.id = :projectId AND a.attendanceStatus = 'PRESENT' AND a.workDate = :today")
                .setParameter("projectId", projectId)
                .setParameter("today", LocalDate.now())
                .uniqueResult();

            BigDecimal totalMatCost = (BigDecimal) session.createQuery("SELECT SUM(u.totalCost) FROM MaterialUsage u WHERE u.project.id = :projectId")
                .setParameter("projectId", projectId).uniqueResult();
            if (totalMatCost == null) totalMatCost = BigDecimal.ZERO;

            BigDecimal totalLabCost = (BigDecimal) session.createQuery("SELECT SUM(p.amountOwed) FROM WorkerPayment p WHERE p.project.id = :projectId")
                .setParameter("projectId", projectId).uniqueResult();
            if (totalLabCost == null) totalLabCost = BigDecimal.ZERO;

            BigDecimal totalExp = totalMatCost.add(totalLabCost);

            List<ProjectMaterialStock> lowStockList = session.createQuery("FROM ProjectMaterialStock s WHERE s.project.id = :projectId AND s.quantityAvailable < s.minimumQuantity")
                .setParameter("projectId", projectId).setMaxResults(10).list();
            
            List<dto.ProjectMaterialStockDTO> lowStockDto = new ArrayList<>();
            for (ProjectMaterialStock s : lowStockList) {
                lowStockDto.add(new dto.ProjectMaterialStockDTO(
                    s.getId(), s.getProject().getId(), s.getProject().getProjectName(),
                    s.getMaterial().getId(), s.getMaterial().getMaterialName(), s.getMaterial().getUnit(),
                    s.getQuantityAvailable(), s.getMinimumQuantity(), s.getAverageUnitPrice(), true
                ));
            }

            return new ProjectSummaryDTO(
                projectId, project.getProjectName(), project.getStatus().name(),
                0, totalMatCost, totalLabCost, totalExp,
                totalWorkers != null ? totalWorkers.intValue() : 0,
                presentToday != null ? presentToday.intValue() : 0,
                lowStockDto
            );
        } finally {
            session.close();
        }
    }
}
