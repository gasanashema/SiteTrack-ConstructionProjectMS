package dto;

import java.io.Serializable;

public class ProjectManagerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String userId;
    private String userFullName;
    private java.time.LocalDate assignedDate;
    private String status;

    public ProjectManagerDTO() {}

    public ProjectManagerDTO(String id, String projectId, String projectName, String userId, String userFullName, java.time.LocalDate assignedDate, String status) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.userId = userId;
        this.userFullName = userFullName;
        this.assignedDate = assignedDate;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public java.time.LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(java.time.LocalDate assignedDate) { this.assignedDate = assignedDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
