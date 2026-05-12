package dto;

import java.io.Serializable;

public class ProjectMaterialStockDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String materialId;
    private String materialName;
    private String unit;
    private java.math.BigDecimal quantityAvailable;
    private java.math.BigDecimal minimumQuantity;
    private java.math.BigDecimal averageUnitPrice;
    private boolean belowMinimum;

    public ProjectMaterialStockDTO() {}

    public ProjectMaterialStockDTO(String id, String projectId, String projectName, String materialId, String materialName, String unit, java.math.BigDecimal quantityAvailable, java.math.BigDecimal minimumQuantity, java.math.BigDecimal averageUnitPrice, boolean belowMinimum) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.materialId = materialId;
        this.materialName = materialName;
        this.unit = unit;
        this.quantityAvailable = quantityAvailable;
        this.minimumQuantity = minimumQuantity;
        this.averageUnitPrice = averageUnitPrice;
        this.belowMinimum = belowMinimum;
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
    public java.math.BigDecimal getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(java.math.BigDecimal quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    public java.math.BigDecimal getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(java.math.BigDecimal minimumQuantity) { this.minimumQuantity = minimumQuantity; }
    public java.math.BigDecimal getAverageUnitPrice() { return averageUnitPrice; }
    public void setAverageUnitPrice(java.math.BigDecimal averageUnitPrice) { this.averageUnitPrice = averageUnitPrice; }
    public boolean isBelowMinimum() { return belowMinimum; }
    public void setBelowMinimum(boolean belowMinimum) { this.belowMinimum = belowMinimum; }
}
