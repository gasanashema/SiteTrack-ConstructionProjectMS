package dto;

import java.io.Serializable;
import java.time.LocalDate;

public class WorkerAssignmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String workerId;
    private String workerName;
    private String workerTypeName;
    private String projectId;
    private String projectName;
    private String status;
    private LocalDate assignedDate;
    private LocalDate endDate;

    public WorkerAssignmentDTO() {}

    public WorkerAssignmentDTO(String id, String workerId, String workerName, String workerTypeName, String projectId, String projectName, String status, LocalDate assignedDate, LocalDate endDate) {
        this.id = id;
        this.workerId = workerId;
        this.workerName = workerName;
        this.workerTypeName = workerTypeName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.status = status;
        this.assignedDate = assignedDate;
        this.endDate = endDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkerId() { return workerId; }
    public void setWorkerId(String workerId) { this.workerId = workerId; }

    public String getWorkerName() { return workerName; }
    public void setWorkerName(String workerName) { this.workerName = workerName; }

    public String getWorkerTypeName() { return workerTypeName; }
    public void setWorkerTypeName(String workerTypeName) { this.workerTypeName = workerTypeName; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
