package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "material_usage")
public class MaterialUsage implements Serializable {
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

    @Column(name = "quantity_used", nullable = false)
    private BigDecimal quantityUsed;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "activity_description")
    private String activityDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public MaterialUsage() {
    }

    public MaterialUsage(String id, Project project, Material material, BigDecimal quantityUsed, BigDecimal unitPrice, BigDecimal totalCost, LocalDate usageDate, String activityDescription, User recordedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.project = project;
        this.material = material;
        this.quantityUsed = quantityUsed;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.usageDate = usageDate;
        this.activityDescription = activityDescription;
        this.recordedBy = recordedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }
    public BigDecimal getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(BigDecimal quantityUsed) { this.quantityUsed = quantityUsed; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public LocalDate getUsageDate() { return usageDate; }
    public void setUsageDate(LocalDate usageDate) { this.usageDate = usageDate; }
    public String getActivityDescription() { return activityDescription; }
    public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }
    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "MaterialUsage{" + "id=" + id + ", project=" + (project != null ? project.getId() : null) + ", material=" + (material != null ? material.getId() : null) + ", quantityUsed=" + quantityUsed + ", totalCost=" + totalCost + '}';
    }
}
