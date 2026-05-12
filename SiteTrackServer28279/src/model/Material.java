package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "materials")
public class Material implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "MAT"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_materials_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private MaterialCategory category;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EMaterialStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private transient List<MaterialPurchase> materialPurchases = new ArrayList<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private transient List<ProjectMaterialStock> materialStocks = new ArrayList<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private transient List<MaterialStockMovement> stockMovements = new ArrayList<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private transient List<MaterialUsage> materialUsages = new ArrayList<>();

    public Material() {
    }

    public Material(String id, MaterialCategory category, String materialName, String unit, BigDecimal currentPrice, String description, EMaterialStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, List<MaterialPurchase> materialPurchases, List<ProjectMaterialStock> materialStocks, List<MaterialStockMovement> stockMovements, List<MaterialUsage> materialUsages) {
        this.id = id;
        this.category = category;
        this.materialName = materialName;
        this.unit = unit;
        this.currentPrice = currentPrice;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.materialPurchases = materialPurchases;
        this.materialStocks = materialStocks;
        this.stockMovements = stockMovements;
        this.materialUsages = materialUsages;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public MaterialCategory getCategory() { return category; }
    public void setCategory(MaterialCategory category) { this.category = category; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public EMaterialStatus getStatus() { return status; }
    public void setStatus(EMaterialStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<MaterialPurchase> getMaterialPurchases() { return materialPurchases; }
    public void setMaterialPurchases(List<MaterialPurchase> materialPurchases) { this.materialPurchases = materialPurchases; }
    public List<ProjectMaterialStock> getMaterialStocks() { return materialStocks; }
    public void setMaterialStocks(List<ProjectMaterialStock> materialStocks) { this.materialStocks = materialStocks; }
    public List<MaterialStockMovement> getStockMovements() { return stockMovements; }
    public void setStockMovements(List<MaterialStockMovement> stockMovements) { this.stockMovements = stockMovements; }
    public List<MaterialUsage> getMaterialUsages() { return materialUsages; }
    public void setMaterialUsages(List<MaterialUsage> materialUsages) { this.materialUsages = materialUsages; }

    @Override
    public String toString() {
        return "Material{" + "id=" + id + ", materialName=" + materialName + ", unit=" + unit + ", status=" + status + '}';
    }
}
