package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "material_categories")
public class MaterialCategory implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "MCA"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_material_categories_id")
        }
    )
    @Column(name = "id")
    private String id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private transient List<Material> materials = new ArrayList<>();

    public MaterialCategory() {
    }

    public MaterialCategory(String id, String categoryName, String description, String unit, LocalDateTime createdAt, LocalDateTime updatedAt, List<Material> materials) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.unit = unit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.materials = materials;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Material> getMaterials() { return materials; }
    public void setMaterials(List<Material> materials) { this.materials = materials; }

    @Override
    public String toString() {
        return "MaterialCategory{" + "id=" + id + ", categoryName=" + categoryName + '}';
    }
}
