package dto;

import java.io.Serializable;

public class MaterialDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String categoryId;
    private String categoryName;
    private String materialName;
    private String unit;
    private java.math.BigDecimal currentPrice;
    private String description;
    private String status;

    public MaterialDTO() {}

    public MaterialDTO(String id, String categoryId, String categoryName, String materialName, String unit, java.math.BigDecimal currentPrice, String description, String status) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.materialName = materialName;
        this.unit = unit;
        this.currentPrice = currentPrice;
        this.description = description;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public java.math.BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(java.math.BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
