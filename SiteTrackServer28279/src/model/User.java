package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "USR"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_users_id")
        }
    )
    @Column(name = "id")
    private String id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ERole role;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EUserStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private transient List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private transient List<ProjectManager> projectManagers = new ArrayList<>();

    @OneToMany(mappedBy = "recordedBy", fetch = FetchType.LAZY)
    private transient List<MaterialPurchase> materialPurchases = new ArrayList<>();

    @OneToMany(mappedBy = "recordedBy", fetch = FetchType.LAZY)
    private transient List<MaterialUsage> materialUsages = new ArrayList<>();

    @OneToMany(mappedBy = "recordedBy", fetch = FetchType.LAZY)
    private transient List<MaterialStockMovement> stockMovements = new ArrayList<>();

    @OneToMany(mappedBy = "recordedBy", fetch = FetchType.LAZY)
    private transient List<ProjectActivity> projectActivities = new ArrayList<>();

    @OneToMany(mappedBy = "recordedBy", fetch = FetchType.LAZY)
    private transient List<WorkerAttendance> workerAttendances = new ArrayList<>();

    @OneToMany(mappedBy = "paidBy", fetch = FetchType.LAZY)
    private transient List<WorkerPayment> workerPayments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private transient List<OtpVerification> otpVerifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private transient List<NotificationLog> notificationLogs = new ArrayList<>();

    public User() {
    }

    public User(String id, String fullName, String username, String email, String password, ERole role, String phone, EUserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, List<Project> projects, List<ProjectManager> projectManagers, List<MaterialPurchase> materialPurchases, List<MaterialUsage> materialUsages, List<MaterialStockMovement> stockMovements, List<ProjectActivity> projectActivities, List<WorkerAttendance> workerAttendances, List<WorkerPayment> workerPayments, List<OtpVerification> otpVerifications, List<NotificationLog> notificationLogs) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projects = projects;
        this.projectManagers = projectManagers;
        this.materialPurchases = materialPurchases;
        this.materialUsages = materialUsages;
        this.stockMovements = stockMovements;
        this.projectActivities = projectActivities;
        this.workerAttendances = workerAttendances;
        this.workerPayments = workerPayments;
        this.otpVerifications = otpVerifications;
        this.notificationLogs = notificationLogs;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public ERole getRole() { return role; }
    public void setRole(ERole role) { this.role = role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public EUserStatus getStatus() { return status; }
    public void setStatus(EUserStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
    public List<ProjectManager> getProjectManagers() { return projectManagers; }
    public void setProjectManagers(List<ProjectManager> projectManagers) { this.projectManagers = projectManagers; }
    public List<MaterialPurchase> getMaterialPurchases() { return materialPurchases; }
    public void setMaterialPurchases(List<MaterialPurchase> materialPurchases) { this.materialPurchases = materialPurchases; }
    public List<MaterialUsage> getMaterialUsages() { return materialUsages; }
    public void setMaterialUsages(List<MaterialUsage> materialUsages) { this.materialUsages = materialUsages; }
    public List<MaterialStockMovement> getStockMovements() { return stockMovements; }
    public void setStockMovements(List<MaterialStockMovement> stockMovements) { this.stockMovements = stockMovements; }
    public List<ProjectActivity> getProjectActivities() { return projectActivities; }
    public void setProjectActivities(List<ProjectActivity> projectActivities) { this.projectActivities = projectActivities; }
    public List<WorkerAttendance> getWorkerAttendances() { return workerAttendances; }
    public void setWorkerAttendances(List<WorkerAttendance> workerAttendances) { this.workerAttendances = workerAttendances; }
    public List<WorkerPayment> getWorkerPayments() { return workerPayments; }
    public void setWorkerPayments(List<WorkerPayment> workerPayments) { this.workerPayments = workerPayments; }
    public List<OtpVerification> getOtpVerifications() { return otpVerifications; }
    public void setOtpVerifications(List<OtpVerification> otpVerifications) { this.otpVerifications = otpVerifications; }
    public List<NotificationLog> getNotificationLogs() { return notificationLogs; }
    public void setNotificationLogs(List<NotificationLog> notificationLogs) { this.notificationLogs = notificationLogs; }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", fullName=" + fullName + ", username=" + username + ", email=" + email + ", role=" + role + ", status=" + status + '}';
    }
}
