package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "projects")
public class Project implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "PRJ"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_projects_id")
        }
    )
    @Column(name = "id")
    private String id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate startDate;

    @Column(name = "expected_end_date", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate expectedEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EProjectStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<ProjectManager> projectManagers = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<MaterialPurchase> materialPurchases = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<ProjectMaterialStock> materialStocks = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<MaterialStockMovement> stockMovements = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<MaterialUsage> materialUsages = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<ProjectActivity> projectActivities = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<WorkerAttendance> workerAttendances = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private transient List<WorkerPayment> workerPayments = new ArrayList<>();

    public Project() {
    }

    public Project(String id, String projectName, String location, String description, LocalDate startDate, LocalDate expectedEndDate, EProjectStatus status, User createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, List<ProjectManager> projectManagers, List<MaterialPurchase> materialPurchases, List<ProjectMaterialStock> materialStocks, List<MaterialStockMovement> stockMovements, List<MaterialUsage> materialUsages, List<ProjectActivity> projectActivities, List<WorkerAttendance> workerAttendances, List<WorkerPayment> workerPayments) {
        this.id = id;
        this.projectName = projectName;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projectManagers = projectManagers;
        this.materialPurchases = materialPurchases;
        this.materialStocks = materialStocks;
        this.stockMovements = stockMovements;
        this.materialUsages = materialUsages;
        this.projectActivities = projectActivities;
        this.workerAttendances = workerAttendances;
        this.workerPayments = workerPayments;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(LocalDate expectedEndDate) { this.expectedEndDate = expectedEndDate; }
    public EProjectStatus getStatus() { return status; }
    public void setStatus(EProjectStatus status) { this.status = status; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<ProjectManager> getProjectManagers() { return projectManagers; }
    public void setProjectManagers(List<ProjectManager> projectManagers) { this.projectManagers = projectManagers; }
    public List<MaterialPurchase> getMaterialPurchases() { return materialPurchases; }
    public void setMaterialPurchases(List<MaterialPurchase> materialPurchases) { this.materialPurchases = materialPurchases; }
    public List<ProjectMaterialStock> getMaterialStocks() { return materialStocks; }
    public void setMaterialStocks(List<ProjectMaterialStock> materialStocks) { this.materialStocks = materialStocks; }
    public List<MaterialStockMovement> getStockMovements() { return stockMovements; }
    public void setStockMovements(List<MaterialStockMovement> stockMovements) { this.stockMovements = stockMovements; }
    public List<MaterialUsage> getMaterialUsages() { return materialUsages; }
    public void setMaterialUsages(List<MaterialUsage> materialUsages) { this.materialUsages = materialUsages; }
    public List<ProjectActivity> getProjectActivities() { return projectActivities; }
    public void setProjectActivities(List<ProjectActivity> projectActivities) { this.projectActivities = projectActivities; }
    public List<WorkerAttendance> getWorkerAttendances() { return workerAttendances; }
    public void setWorkerAttendances(List<WorkerAttendance> workerAttendances) { this.workerAttendances = workerAttendances; }
    public List<WorkerPayment> getWorkerPayments() { return workerPayments; }
    public void setWorkerPayments(List<WorkerPayment> workerPayments) { this.workerPayments = workerPayments; }

    @Override
    public String toString() {
        return "Project{" + "id=" + id + ", projectName=" + projectName + ", status=" + status + '}';
    }
}
