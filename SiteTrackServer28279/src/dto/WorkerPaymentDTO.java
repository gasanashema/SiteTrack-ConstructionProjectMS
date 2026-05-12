package dto;

import java.io.Serializable;

public class WorkerPaymentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String workerId;
    private String workerFullName;
    private String attendanceId;
    private java.time.LocalDate workDate;
    private java.math.BigDecimal dailyRate;
    private java.math.BigDecimal amountOwed;
    private java.math.BigDecimal amountPaid;
    private String paymentStatus;
    private String paidByName;
    private String notes;

    public WorkerPaymentDTO() {}

    public WorkerPaymentDTO(String id, String projectId, String projectName, String workerId, String workerFullName, String attendanceId, java.time.LocalDate workDate, java.math.BigDecimal dailyRate, java.math.BigDecimal amountOwed, java.math.BigDecimal amountPaid, String paymentStatus, String paidByName, String notes) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.workerId = workerId;
        this.workerFullName = workerFullName;
        this.attendanceId = attendanceId;
        this.workDate = workDate;
        this.dailyRate = dailyRate;
        this.amountOwed = amountOwed;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.paidByName = paidByName;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getWorkerId() { return workerId; }
    public void setWorkerId(String workerId) { this.workerId = workerId; }
    public String getWorkerFullName() { return workerFullName; }
    public void setWorkerFullName(String workerFullName) { this.workerFullName = workerFullName; }
    public String getAttendanceId() { return attendanceId; }
    public void setAttendanceId(String attendanceId) { this.attendanceId = attendanceId; }
    public java.time.LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(java.time.LocalDate workDate) { this.workDate = workDate; }
    public java.math.BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(java.math.BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public java.math.BigDecimal getAmountOwed() { return amountOwed; }
    public void setAmountOwed(java.math.BigDecimal amountOwed) { this.amountOwed = amountOwed; }
    public java.math.BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(java.math.BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaidByName() { return paidByName; }
    public void setPaidByName(String paidByName) { this.paidByName = paidByName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
