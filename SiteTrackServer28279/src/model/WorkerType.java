package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "worker_types")
public class WorkerType implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "WKT"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_worker_types_id")
        }
    )
    @Column(name = "id")
    private String id;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "default_daily_rate", nullable = false)
    private BigDecimal defaultDailyRate;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workerType", fetch = FetchType.LAZY)
    private transient List<SiteWorker> siteWorkers = new ArrayList<>();

    public WorkerType() {
    }

    public WorkerType(String id, String typeName, BigDecimal defaultDailyRate, String description, LocalDateTime createdAt, LocalDateTime updatedAt, List<SiteWorker> siteWorkers) {
        this.id = id;
        this.typeName = typeName;
        this.defaultDailyRate = defaultDailyRate;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.siteWorkers = siteWorkers;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public BigDecimal getDefaultDailyRate() { return defaultDailyRate; }
    public void setDefaultDailyRate(BigDecimal defaultDailyRate) { this.defaultDailyRate = defaultDailyRate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<SiteWorker> getSiteWorkers() { return siteWorkers; }
    public void setSiteWorkers(List<SiteWorker> siteWorkers) { this.siteWorkers = siteWorkers; }

    @Override
    public String toString() {
        return "WorkerType{" + "id=" + id + ", typeName=" + typeName + ", defaultDailyRate=" + defaultDailyRate + '}';
    }
}
