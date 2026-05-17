package dto;

import java.io.Serializable;

public class MaterialCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String categoryName;
    private String description;
    private String unit;

    public MaterialCategoryDTO() {}

    public MaterialCategoryDTO(String id, String categoryName, String description, String unit) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.unit = unit;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
