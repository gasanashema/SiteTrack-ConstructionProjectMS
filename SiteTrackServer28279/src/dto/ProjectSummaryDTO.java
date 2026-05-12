package dto;

import java.io.Serializable;

public class ProjectSummaryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectId;
    private String projectName;
    private String status;
    private int progressPercentage;
    private java.math.BigDecimal totalMaterialCost;
    private java.math.BigDecimal totalLaborCost;
    private java.math.BigDecimal totalExpenditure;
    private int totalWorkers;
    private int presentToday;
    private java.util.List<ProjectMaterialStockDTO> lowStockItems;

    public ProjectSummaryDTO() {}

    public ProjectSummaryDTO(String projectId, String projectName, String status, int progressPercentage, java.math.BigDecimal totalMaterialCost, java.math.BigDecimal totalLaborCost, java.math.BigDecimal totalExpenditure, int totalWorkers, int presentToday, java.util.List<ProjectMaterialStockDTO> lowStockItems) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.status = status;
        this.progressPercentage = progressPercentage;
        this.totalMaterialCost = totalMaterialCost;
        this.totalLaborCost = totalLaborCost;
        this.totalExpenditure = totalExpenditure;
        this.totalWorkers = totalWorkers;
        this.presentToday = presentToday;
        this.lowStockItems = lowStockItems;
    }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
    public java.math.BigDecimal getTotalMaterialCost() { return totalMaterialCost; }
    public void setTotalMaterialCost(java.math.BigDecimal totalMaterialCost) { this.totalMaterialCost = totalMaterialCost; }
    public java.math.BigDecimal getTotalLaborCost() { return totalLaborCost; }
    public void setTotalLaborCost(java.math.BigDecimal totalLaborCost) { this.totalLaborCost = totalLaborCost; }
    public java.math.BigDecimal getTotalExpenditure() { return totalExpenditure; }
    public void setTotalExpenditure(java.math.BigDecimal totalExpenditure) { this.totalExpenditure = totalExpenditure; }
    public int getTotalWorkers() { return totalWorkers; }
    public void setTotalWorkers(int totalWorkers) { this.totalWorkers = totalWorkers; }
    public int getPresentToday() { return presentToday; }
    public void setPresentToday(int presentToday) { this.presentToday = presentToday; }
    public java.util.List<ProjectMaterialStockDTO> getLowStockItems() { return lowStockItems; }
    public void setLowStockItems(java.util.List<ProjectMaterialStockDTO> lowStockItems) { this.lowStockItems = lowStockItems; }
}
