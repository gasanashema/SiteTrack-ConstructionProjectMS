package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import org.hibernate.annotations.Check;

@Entity
@Table(name = "project_activities")
@Check(constraints = "progress_percentage BETWEEN 0 AND 100")
public class ProjectActivity implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "ACT"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_project_activities_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "activity_title", nullable = false)
    private String activityTitle;

    @Column(name = "activity_description")
    private String activityDescription;

    @Column(name = "activity_date", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate activityDate;

    @Column(name = "progress_percentage", nullable = false)
    private int progressPercentage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @Column(name = "created_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime updatedAt;

    public ProjectActivity() {
    }

    public ProjectActivity(String id, Project project, String activityTitle, String activityDescription, LocalDate activityDate, int progressPercentage, User recordedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.project = project;
        this.activityTitle = activityTitle;
        this.activityDescription = activityDescription;
        this.activityDate = activityDate;
        this.progressPercentage = progressPercentage;
        this.recordedBy = recordedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
    public String getActivityDescription() { return activityDescription; }
    public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }
    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
    public int getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "ProjectActivity{" + "id=" + id + ", project=" + (project != null ? project.getId() : null) + ", activityTitle=" + activityTitle + ", progressPercentage=" + progressPercentage + '}';
    }
}
