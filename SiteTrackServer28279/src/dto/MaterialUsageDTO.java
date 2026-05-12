package dto;

import java.io.Serializable;

public class MaterialUsageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String materialId;
    private String materialName;
    private String unit;
    private java.math.BigDecimal quantityUsed;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal totalCost;
    private java.time.LocalDate usageDate;
    private String activityDescription;
    private String recordedByName;

    public MaterialUsageDTO() {}

    public MaterialUsageDTO(String id, String projectId, String projectName, String materialId, String materialName, String unit, java.math.BigDecimal quantityUsed, java.math.BigDecimal unitPrice, java.math.BigDecimal totalCost, java.time.LocalDate usageDate, String activityDescription, String recordedByName) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.materialId = materialId;
        this.materialName = materialName;
        this.unit = unit;
        this.quantityUsed = quantityUsed;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.usageDate = usageDate;
        this.activityDescription = activityDescription;
        this.recordedByName = recordedByName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getMaterialId() { return materialId; }
    public void setMaterialId(String materialId) { this.materialId = materialId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public java.math.BigDecimal getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(java.math.BigDecimal quantityUsed) { this.quantityUsed = quantityUsed; }
    public java.math.BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(java.math.BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public java.math.BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(java.math.BigDecimal totalCost) { this.totalCost = totalCost; }
    public java.time.LocalDate getUsageDate() { return usageDate; }
    public void setUsageDate(java.time.LocalDate usageDate) { this.usageDate = usageDate; }
    public String getActivityDescription() { return activityDescription; }
    public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }
    public String getRecordedByName() { return recordedByName; }
    public void setRecordedByName(String recordedByName) { this.recordedByName = recordedByName; }
}
