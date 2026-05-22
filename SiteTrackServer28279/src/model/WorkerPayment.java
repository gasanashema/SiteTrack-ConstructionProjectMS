package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "worker_payments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"attendance_id"}))
public class WorkerPayment implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "PAY"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_worker_payments_id")
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attendance_id")
    private WorkerAttendance attendance;

    @Column(name = "work_date", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateAttributeConverter.class)
    private LocalDate workDate;

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @Column(name = "amount_owed", nullable = false)
    private BigDecimal amountOwed;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private EPaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paid_by")
    private User paidBy;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime updatedAt;

    public WorkerPayment() {
    }

    public WorkerPayment(String id, Project project, SiteWorker worker, WorkerAttendance attendance, LocalDate workDate, BigDecimal dailyRate, BigDecimal amountOwed, BigDecimal amountPaid, EPaymentStatus paymentStatus, User paidBy, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.project = project;
        this.worker = worker;
        this.attendance = attendance;
        this.workDate = workDate;
        this.dailyRate = dailyRate;
        this.amountOwed = amountOwed;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.paidBy = paidBy;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public SiteWorker getWorker() { return worker; }
    public void setWorker(SiteWorker worker) { this.worker = worker; }
    public WorkerAttendance getAttendance() { return attendance; }
    public void setAttendance(WorkerAttendance attendance) { this.attendance = attendance; }
    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public BigDecimal getAmountOwed() { return amountOwed; }
    public void setAmountOwed(BigDecimal amountOwed) { this.amountOwed = amountOwed; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public EPaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(EPaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public User getPaidBy() { return paidBy; }
    public void setPaidBy(User paidBy) { this.paidBy = paidBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "WorkerPayment{" + "id=" + id + ", project=" + (project != null ? project.getId() : null) + ", worker=" + (worker != null ? worker.getId() : null) + ", amountPaid=" + amountPaid + ", paymentStatus=" + paymentStatus + '}';
    }
}
