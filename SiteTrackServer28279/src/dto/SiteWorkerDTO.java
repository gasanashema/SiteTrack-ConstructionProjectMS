package dto;

import java.io.Serializable;

public class SiteWorkerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String workerTypeId;
    private String workerTypeName;
    private String fullName;
    private String phone;
    private java.math.BigDecimal dailyRate;
    private String status;

    public SiteWorkerDTO() {}

    public SiteWorkerDTO(String id, String workerTypeId, String workerTypeName, String fullName, String phone, java.math.BigDecimal dailyRate, String status) {
        this.id = id;
        this.workerTypeId = workerTypeId;
        this.workerTypeName = workerTypeName;
        this.fullName = fullName;
        this.phone = phone;
        this.dailyRate = dailyRate;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkerTypeId() { return workerTypeId; }
    public void setWorkerTypeId(String workerTypeId) { this.workerTypeId = workerTypeId; }
    public String getWorkerTypeName() { return workerTypeName; }
    public void setWorkerTypeName(String workerTypeName) { this.workerTypeName = workerTypeName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public java.math.BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(java.math.BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
