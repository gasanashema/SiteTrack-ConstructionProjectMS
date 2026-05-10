package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "project_material_stock",
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "material_id"}))
public class ProjectMaterialStock implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name = "quantity_available", nullable = false)
    private BigDecimal quantityAvailable;

    @Column(name = "minimum_quantity", nullable = false)
    private BigDecimal minimumQuantity;

    @Column(name = "average_unit_price", nullable = false)
    private BigDecimal averageUnitPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ProjectMaterialStock() {
    }

    public ProjectMaterialStock(String id, Project project, Material material, BigDecimal quantityAvailable, BigDecimal minimumQuantity, BigDecimal averageUnitPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.project = project;
        this.material = material;
        this.quantityAvailable = quantityAvailable;
        this.minimumQuantity = minimumQuantity;
        this.averageUnitPrice = averageUnitPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }
    public BigDecimal getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(BigDecimal quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    public BigDecimal getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(BigDecimal minimumQuantity) { this.minimumQuantity = minimumQuantity; }
    public BigDecimal getAverageUnitPrice() { return averageUnitPrice; }
    public void setAverageUnitPrice(BigDecimal averageUnitPrice) { this.averageUnitPrice = averageUnitPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "ProjectMaterialStock{" + "id=" + id + ", project=" + (project != null ? project.getId() : null) + ", material=" + (material != null ? material.getId() : null) + ", quantityAvailable=" + quantityAvailable + '}';
    }
}
