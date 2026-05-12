package dto;

import java.io.Serializable;

public class ProjectDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectName;
    private String location;
    private String description;
    private java.time.LocalDate startDate;
    private java.time.LocalDate expectedEndDate;
    private String status;
    private String createdByName;
    private java.time.LocalDateTime createdAt;

    public ProjectDTO() {}

    public ProjectDTO(String id, String projectName, String location, String description, java.time.LocalDate startDate, java.time.LocalDate expectedEndDate, String status, String createdByName, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.projectName = projectName;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.status = status;
        this.createdByName = createdByName;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public java.time.LocalDate getStartDate() { return startDate; }
    public void setStartDate(java.time.LocalDate startDate) { this.startDate = startDate; }
    public java.time.LocalDate getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(java.time.LocalDate expectedEndDate) { this.expectedEndDate = expectedEndDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
}
