package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "worker_attendance",
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "worker_id", "work_date"}))
public class WorkerAttendance implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "ATT"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_worker_attendance_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_id")
    private SiteWorker worker;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private EAttendanceStatus attendanceStatus;

    @Column(name = "work_description")
    private String workDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "attendance", fetch = FetchType.LAZY)
    private transient WorkerPayment workerPayment;

    public WorkerAttendance() {
    }

    public WorkerAttendance(String id, Project project, SiteWorker worker, LocalDate workDate, EAttendanceStatus attendanceStatus, String workDescription, User recordedBy, LocalDateTime createdAt, LocalDateTime updatedAt, WorkerPayment workerPayment) {
        this.id = id;
        this.project = project;
        this.worker = worker;
        this.workDate = workDate;
        this.attendanceStatus = attendanceStatus;
        this.workDescription = workDescription;
        this.recordedBy = recordedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.workerPayment = workerPayment;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public SiteWorker getWorker() { return worker; }
    public void setWorker(SiteWorker worker) { this.worker = worker; }
    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
    public EAttendanceStatus getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(EAttendanceStatus attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getWorkDescription() { return workDescription; }
    public void setWorkDescription(String workDescription) { this.workDescription = workDescription; }
    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public WorkerPayment getWorkerPayment() { return workerPayment; }
    public void setWorkerPayment(WorkerPayment workerPayment) { this.workerPayment = workerPayment; }

    @Override
    public String toString() {
        return "WorkerAttendance{" + "id=" + id + ", project=" + (project != null ? project.getId() : null) + ", worker=" + (worker != null ? worker.getId() : null) + ", workDate=" + workDate + ", attendanceStatus=" + attendanceStatus + '}';
    }
}
