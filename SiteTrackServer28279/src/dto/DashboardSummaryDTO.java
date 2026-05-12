package dto;

import java.io.Serializable;

public class DashboardSummaryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalProjects;
    private int activeProjects;
    private int completedProjects;
    private int totalWorkers;
    private int totalMaterials;
    private java.math.BigDecimal totalMaterialExpenditure;
    private java.math.BigDecimal totalLaborExpenditure;
    private java.util.List<ProjectDTO> recentProjects;
    private java.util.List<ProjectMaterialStockDTO> lowStockAlerts;

    public DashboardSummaryDTO() {}

    public DashboardSummaryDTO(int totalProjects, int activeProjects, int completedProjects, int totalWorkers, int totalMaterials, java.math.BigDecimal totalMaterialExpenditure, java.math.BigDecimal totalLaborExpenditure, java.util.List<ProjectDTO> recentProjects, java.util.List<ProjectMaterialStockDTO> lowStockAlerts) {
        this.totalProjects = totalProjects;
        this.activeProjects = activeProjects;
        this.completedProjects = completedProjects;
        this.totalWorkers = totalWorkers;
        this.totalMaterials = totalMaterials;
        this.totalMaterialExpenditure = totalMaterialExpenditure;
        this.totalLaborExpenditure = totalLaborExpenditure;
        this.recentProjects = recentProjects;
        this.lowStockAlerts = lowStockAlerts;
    }

    public int getTotalProjects() { return totalProjects; }
    public void setTotalProjects(int totalProjects) { this.totalProjects = totalProjects; }
    public int getActiveProjects() { return activeProjects; }
    public void setActiveProjects(int activeProjects) { this.activeProjects = activeProjects; }
    public int getCompletedProjects() { return completedProjects; }
    public void setCompletedProjects(int completedProjects) { this.completedProjects = completedProjects; }
    public int getTotalWorkers() { return totalWorkers; }
    public void setTotalWorkers(int totalWorkers) { this.totalWorkers = totalWorkers; }
    public int getTotalMaterials() { return totalMaterials; }
    public void setTotalMaterials(int totalMaterials) { this.totalMaterials = totalMaterials; }
    public java.math.BigDecimal getTotalMaterialExpenditure() { return totalMaterialExpenditure; }
    public void setTotalMaterialExpenditure(java.math.BigDecimal totalMaterialExpenditure) { this.totalMaterialExpenditure = totalMaterialExpenditure; }
    public java.math.BigDecimal getTotalLaborExpenditure() { return totalLaborExpenditure; }
    public void setTotalLaborExpenditure(java.math.BigDecimal totalLaborExpenditure) { this.totalLaborExpenditure = totalLaborExpenditure; }
    public java.util.List<ProjectDTO> getRecentProjects() { return recentProjects; }
    public void setRecentProjects(java.util.List<ProjectDTO> recentProjects) { this.recentProjects = recentProjects; }
    public java.util.List<ProjectMaterialStockDTO> getLowStockAlerts() { return lowStockAlerts; }
    public void setLowStockAlerts(java.util.List<ProjectMaterialStockDTO> lowStockAlerts) { this.lowStockAlerts = lowStockAlerts; }
}
