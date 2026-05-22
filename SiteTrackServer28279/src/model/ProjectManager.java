package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "project_managers")
public class ProjectManager implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "PMG"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_project_managers_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "assigned_date", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate assignedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EManagerStatus status;

    @Column(name = "created_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime updatedAt;

    public ProjectManager() {
    }

    public ProjectManager(String id, Project project, User user, LocalDate assignedDate, EManagerStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.project = project;
        this.user = user;
        this.assignedDate = assignedDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }
    public EManagerStatus getStatus() { return status; }
    public void setStatus(EManagerStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "ProjectManager{" + "id=" + id + ", project=" + (project != null ? project.getId() : null) + ", user=" + (user != null ? user.getId() : null) + ", status=" + status + '}';
    }
}
