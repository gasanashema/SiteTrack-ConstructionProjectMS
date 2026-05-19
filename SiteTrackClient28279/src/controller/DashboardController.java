package controller;

import config.RMIConnection;
import dto.DashboardSummaryDTO;
import dto.ProjectDTO;
import dto.ProjectMaterialStockDTO;
import dto.ProjectSummaryDTO;
import dto.MaterialPurchaseDTO;
import dto.WorkerPaymentDTO;
import dto.WorkerAttendanceDTO;
import service.interfaces.ProjectService;
import service.interfaces.ReportService;
import service.interfaces.MaterialPurchaseService;
import service.interfaces.WorkerPaymentService;
import service.interfaces.WorkerAttendanceService;
import service.interfaces.MaterialStockService;
import session.SessionManager;

import javax.swing.*;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {
    
    private static DashboardController instance;

    // Simple TTL Cache
    private DashboardSummaryDTO cachedAdminDashboard;
    private long adminCacheTimestamp = 0;
    
    private Map<String, ProjectSummaryDTO> cachedSiteManagerDashboards = new HashMap<>();
    private Map<String, Long> siteManagerCacheTimestamps = new HashMap<>();

    private static final long CACHE_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    public DashboardController() {}

    public static DashboardController getInstance() {
        if (instance == null) {
            instance = new DashboardController();
        }
        return instance;
    }

    public DashboardSummaryDTO getAdminDashboard() {
        long now = System.currentTimeMillis();
        if (cachedAdminDashboard != null && (now - adminCacheTimestamp) < CACHE_DURATION_MS) {
            return cachedAdminDashboard;
        }

        try {
            ReportService reportService = RMIConnection.getInstance().getService(ReportService.class);
            cachedAdminDashboard = reportService.getAdminDashboardSummary();
            adminCacheTimestamp = now;
            return cachedAdminDashboard;
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Failed to load admin dashboard data: " + e.getMessage());
            return cachedAdminDashboard; // Return stale cache if available
        }
    }

    public ProjectSummaryDTO getSiteManagerDashboard(String projectId) {
        if (projectId == null || projectId.trim().isEmpty()) return null;

        long now = System.currentTimeMillis();
        if (cachedSiteManagerDashboards.containsKey(projectId)) {
            long lastFetch = siteManagerCacheTimestamps.getOrDefault(projectId, 0L);
            if ((now - lastFetch) < CACHE_DURATION_MS) {
                return cachedSiteManagerDashboards.get(projectId);
            }
        }

        try {
            ReportService reportService = RMIConnection.getInstance().getService(ReportService.class);
            ProjectSummaryDTO summary = reportService.getSiteManagerDashboardSummary(projectId);
            cachedSiteManagerDashboards.put(projectId, summary);
            siteManagerCacheTimestamps.put(projectId, now);
            return summary;
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Failed to load project dashboard data: " + e.getMessage());
            return cachedSiteManagerDashboards.get(projectId);
        }
    }

    public List<ProjectDTO> getRecentProjects(int limit) {
        try {
            ProjectService projectService = RMIConnection.getInstance().getService(ProjectService.class);
            List<ProjectDTO> all = projectService.getAllProjects();
            if (all == null) return new ArrayList<>();
            all.sort((p1, p2) -> {
                if (p1.getCreatedAt() == null && p2.getCreatedAt() == null) return 0;
                if (p1.getCreatedAt() == null) return 1;
                if (p2.getCreatedAt() == null) return -1;
                return p2.getCreatedAt().compareTo(p1.getCreatedAt());
            });
            return all.stream().limit(limit).collect(Collectors.toList());
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<ProjectMaterialStockDTO> getLowStockAlerts() {
        DashboardSummaryDTO data = getAdminDashboard();
        if (data != null && data.getLowStockAlerts() != null) {
            return data.getLowStockAlerts();
        }
        return new ArrayList<>();
    }

    public Map<String, BigDecimal> getMaterialExpenditureByCategory() {
        Map<String, BigDecimal> categoryCost = new HashMap<>();
        try {
            ProjectService projectService = RMIConnection.getInstance().getService(ProjectService.class);
            MaterialPurchaseService purchaseService = RMIConnection.getInstance().getService(MaterialPurchaseService.class);
            List<ProjectDTO> projects = projectService.getAllProjects();
            
            for (ProjectDTO p : projects) {
                List<MaterialPurchaseDTO> purchases = purchaseService.getPurchasesByProject(p.getId());
                if (purchases != null) {
                    for (MaterialPurchaseDTO pur : purchases) {
                        String category = pur.getMaterialName();
                        if (category == null || category.isEmpty()) category = "Uncategorized";
                        categoryCost.put(category, categoryCost.getOrDefault(category, BigDecimal.ZERO).add(pur.getTotalPrice()));
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return categoryCost;
    }

    public Map<String, BigDecimal> getLaborExpenditureByWorkerType() {
        Map<String, BigDecimal> typeCost = new HashMap<>();
        try {
            ProjectService projectService = RMIConnection.getInstance().getService(ProjectService.class);
            WorkerPaymentService paymentService = RMIConnection.getInstance().getService(WorkerPaymentService.class);
            service.interfaces.SiteWorkerService workerService = RMIConnection.getInstance().getService(service.interfaces.SiteWorkerService.class);
            
            // Map workerId to workerTypeName to avoid N+1 queries
            Map<String, String> workerTypeMap = new HashMap<>();
            List<dto.SiteWorkerDTO> allWorkers = workerService.getAllWorkers();
            if (allWorkers != null) {
                for (dto.SiteWorkerDTO w : allWorkers) {
                    workerTypeMap.put(w.getId(), w.getWorkerTypeName());
                }
            }

            List<ProjectDTO> projects = projectService.getAllProjects();
            
            for (ProjectDTO p : projects) {
                List<WorkerPaymentDTO> payments = paymentService.getPaymentsByProject(p.getId());
                if (payments != null) {
                    for (WorkerPaymentDTO pay : payments) {
                        String type = workerTypeMap.getOrDefault(pay.getWorkerId(), "General Labor");
                        if (type == null || type.isEmpty()) type = "General Labor";
                        BigDecimal amount = pay.getAmountPaid() != null ? pay.getAmountPaid() : BigDecimal.ZERO;
                        typeCost.put(type, typeCost.getOrDefault(type, BigDecimal.ZERO).add(amount));
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return typeCost;
    }

    public Map<LocalDate, Integer> getAttendanceTrendByDate(String projectId, int dayCount) {
        Map<LocalDate, Integer> trend = new TreeMap<>();
        try {
            WorkerAttendanceService attendanceService = RMIConnection.getInstance().getService(WorkerAttendanceService.class);
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(dayCount - 1);
            
            List<WorkerAttendanceDTO> attendance = attendanceService.getAttendanceByProjectAndDateRange(projectId, startDate, endDate);
            
            // Initialize all days to 0
            for (int i = 0; i < dayCount; i++) {
                trend.put(startDate.plusDays(i), 0);
            }
            
            if (attendance != null) {
                for (WorkerAttendanceDTO att : attendance) {
                    if ("PRESENT".equalsIgnoreCase(att.getAttendanceStatus())) {
                        LocalDate date = att.getWorkDate();
                        if (trend.containsKey(date)) {
                            trend.put(date, trend.get(date) + 1);
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return trend;
    }

    public Map<String, Integer> getProjectProgressByStatus() {
        Map<String, Integer> statusCount = new HashMap<>();
        try {
            ProjectService projectService = RMIConnection.getInstance().getService(ProjectService.class);
            List<ProjectDTO> projects = projectService.getAllProjects();
            if (projects != null) {
                for (ProjectDTO p : projects) {
                    String status = p.getStatus() != null ? p.getStatus().toUpperCase() : "UNKNOWN";
                    statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return statusCount;
    }

    public BigDecimal getTotalExpenditureByMonth(String projectId, int monthCount) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            MaterialPurchaseService purchaseService = RMIConnection.getInstance().getService(MaterialPurchaseService.class);
            WorkerPaymentService paymentService = RMIConnection.getInstance().getService(WorkerPaymentService.class);
            
            LocalDate to = LocalDate.now();
            LocalDate from = to.minusMonths(monthCount).withDayOfMonth(1);
            
            List<MaterialPurchaseDTO> purchases = purchaseService.getPurchasesByProjectAndDateRange(projectId, from, to);
            List<WorkerPaymentDTO> payments = paymentService.getPaymentsByProjectAndDateRange(projectId, from, to);
            
            if (purchases != null) {
                for (MaterialPurchaseDTO p : purchases) {
                    total = total.add(p.getTotalPrice() != null ? p.getTotalPrice() : BigDecimal.ZERO);
                }
            }
            if (payments != null) {
                for (WorkerPaymentDTO p : payments) {
                    total = total.add(p.getAmountPaid() != null ? p.getAmountPaid() : BigDecimal.ZERO);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<WorkerAttendanceDTO> getTodayAttendance(String projectId) {
        try {
            WorkerAttendanceService attendanceService = RMIConnection.getInstance().getService(WorkerAttendanceService.class);
            LocalDate today = LocalDate.now();
            List<WorkerAttendanceDTO> list = attendanceService.getAttendanceByProjectAndDateRange(projectId, today, today);
            return list != null ? list : new ArrayList<>();
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Map<String, BigDecimal> getProjectBudgetUtilization(String projectId) {
        Map<String, BigDecimal> utilization = new HashMap<>();
        try {
            // Simplified budget logic assuming no strict budget is enforced in the model yet.
            // Using a mock budget of 100,000,000 for demonstration.
            BigDecimal budget = new BigDecimal("100000000");
            BigDecimal spent = getTotalExpenditureByMonth(projectId, 120); // all time approx
            
            utilization.put("Used", spent);
            utilization.put("Remaining", budget.subtract(spent).max(BigDecimal.ZERO));
            
            BigDecimal percentage = spent.divide(budget, 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            utilization.put("Percentage", percentage);
            
        } catch (Exception e) {
            e.printStackTrace();
            utilization.put("Used", BigDecimal.ZERO);
            utilization.put("Remaining", BigDecimal.ZERO);
            utilization.put("Percentage", BigDecimal.ZERO);
        }
        return utilization;
    }
}
