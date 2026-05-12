package dto;

import java.io.Serializable;

public class WorkerAttendanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String workerId;
    private String workerFullName;
    private String workerTypeName;
    private java.time.LocalDate workDate;
    private String attendanceStatus;
    private String workDescription;
    private String recordedByName;

    public WorkerAttendanceDTO() {}

    public WorkerAttendanceDTO(String id, String projectId, String projectName, String workerId, String workerFullName, String workerTypeName, java.time.LocalDate workDate, String attendanceStatus, String workDescription, String recordedByName) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.workerId = workerId;
        this.workerFullName = workerFullName;
        this.workerTypeName = workerTypeName;
        this.workDate = workDate;
        this.attendanceStatus = attendanceStatus;
        this.workDescription = workDescription;
        this.recordedByName = recordedByName;
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
    public String getWorkerTypeName() { return workerTypeName; }
    public void setWorkerTypeName(String workerTypeName) { this.workerTypeName = workerTypeName; }
    public java.time.LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(java.time.LocalDate workDate) { this.workDate = workDate; }
    public String getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getWorkDescription() { return workDescription; }
    public void setWorkDescription(String workDescription) { this.workDescription = workDescription; }
    public String getRecordedByName() { return recordedByName; }
    public void setRecordedByName(String recordedByName) { this.recordedByName = recordedByName; }
}
