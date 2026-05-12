package dto;

import java.io.Serializable;

public class MaterialStockMovementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String materialId;
    private String materialName;
    private String movementType;
    private java.math.BigDecimal quantity;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal totalPrice;
    private java.time.LocalDate movementDate;
    private String description;
    private String referenceType;
    private String referenceId;
    private String recordedByName;

    public MaterialStockMovementDTO() {}

    public MaterialStockMovementDTO(String id, String projectId, String projectName, String materialId, String materialName, String movementType, java.math.BigDecimal quantity, java.math.BigDecimal unitPrice, java.math.BigDecimal totalPrice, java.time.LocalDate movementDate, String description, String referenceType, String referenceId, String recordedByName) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.materialId = materialId;
        this.materialName = materialName;
        this.movementType = movementType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.movementDate = movementDate;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
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
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public java.math.BigDecimal getQuantity() { return quantity; }
    public void setQuantity(java.math.BigDecimal quantity) { this.quantity = quantity; }
    public java.math.BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(java.math.BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public java.math.BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(java.math.BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public java.time.LocalDate getMovementDate() { return movementDate; }
    public void setMovementDate(java.time.LocalDate movementDate) { this.movementDate = movementDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getRecordedByName() { return recordedByName; }
    public void setRecordedByName(String recordedByName) { this.recordedByName = recordedByName; }
}
