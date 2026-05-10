package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "site_workers")
public class SiteWorker implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_type_id")
    private WorkerType workerType;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EWorkerStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY)
    private transient List<WorkerAttendance> workerAttendances = new ArrayList<>();

    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY)
    private transient List<WorkerPayment> workerPayments = new ArrayList<>();

    public SiteWorker() {
    }

    public SiteWorker(String id, WorkerType workerType, String fullName, String phone, BigDecimal dailyRate, EWorkerStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, List<WorkerAttendance> workerAttendances, List<WorkerPayment> workerPayments) {
        this.id = id;
        this.workerType = workerType;
        this.fullName = fullName;
        this.phone = phone;
        this.dailyRate = dailyRate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.workerAttendances = workerAttendances;
        this.workerPayments = workerPayments;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public WorkerType getWorkerType() { return workerType; }
    public void setWorkerType(WorkerType workerType) { this.workerType = workerType; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public EWorkerStatus getStatus() { return status; }
    public void setStatus(EWorkerStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<WorkerAttendance> getWorkerAttendances() { return workerAttendances; }
    public void setWorkerAttendances(List<WorkerAttendance> workerAttendances) { this.workerAttendances = workerAttendances; }
    public List<WorkerPayment> getWorkerPayments() { return workerPayments; }
    public void setWorkerPayments(List<WorkerPayment> workerPayments) { this.workerPayments = workerPayments; }

    @Override
    public String toString() {
        return "SiteWorker{" + "id=" + id + ", fullName=" + fullName + ", phone=" + phone + ", status=" + status + '}';
    }
}
