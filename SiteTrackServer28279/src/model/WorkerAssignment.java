package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "worker_assignments", uniqueConstraints = @UniqueConstraint(columnNames = {"worker_id", "status"}, name = "uk_active_worker_assignment"))
public class WorkerAssignment implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "WAS"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_worker_assignment_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_id", nullable = false)
    private SiteWorker worker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EAssignmentStatus status;

    @Column(name = "assigned_date", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate assignedDate;

    @Column(name = "end_date")
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime updatedAt;

    public WorkerAssignment() {
    }

    public WorkerAssignment(String id, SiteWorker worker, Project project, EAssignmentStatus status, LocalDate assignedDate, LocalDate endDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.worker = worker;
        this.project = project;
        this.status = status;
        this.assignedDate = assignedDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public SiteWorker getWorker() { return worker; }
    public void setWorker(SiteWorker worker) { this.worker = worker; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public EAssignmentStatus getStatus() { return status; }
    public void setStatus(EAssignmentStatus status) { this.status = status; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "WorkerAssignment{" + "id=" + id + ", worker=" + (worker != null ? worker.getId() : null) + ", project=" + (project != null ? project.getId() : null) + ", status=" + status + '}';
    }
}
