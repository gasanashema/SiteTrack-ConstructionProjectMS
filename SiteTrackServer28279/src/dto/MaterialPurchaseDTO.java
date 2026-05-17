package dto;

import java.io.Serializable;

public class MaterialPurchaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String materialId;
    private String materialName;
    private String unit;
    private java.math.BigDecimal quantity;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal totalPrice;
    private String supplierName;
    private java.time.LocalDate purchaseDate;
    private String recordedById;
    private String recordedByName;

    public MaterialPurchaseDTO() {}

    public MaterialPurchaseDTO(String id, String projectId, String projectName, String materialId, String materialName, String unit, java.math.BigDecimal quantity, java.math.BigDecimal unitPrice, java.math.BigDecimal totalPrice, String supplierName, java.time.LocalDate purchaseDate, String recordedById, String recordedByName) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.materialId = materialId;
        this.materialName = materialName;
        this.unit = unit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.supplierName = supplierName;
        this.purchaseDate = purchaseDate;
        this.recordedById = recordedById;
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
    public java.math.BigDecimal getQuantity() { return quantity; }
    public void setQuantity(java.math.BigDecimal quantity) { this.quantity = quantity; }
    public java.math.BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(java.math.BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public java.math.BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(java.math.BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public java.time.LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(java.time.LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getRecordedById() { return recordedById; }
    public void setRecordedById(String recordedById) { this.recordedById = recordedById; }
    public String getRecordedByName() { return recordedByName; }
    public void setRecordedByName(String recordedByName) { this.recordedByName = recordedByName; }
}
