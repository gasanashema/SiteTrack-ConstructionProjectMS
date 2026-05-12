package dto;

import java.io.Serializable;

public class ProjectActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String activityTitle;
    private String activityDescription;
    private java.time.LocalDate activityDate;
    private int progressPercentage;
    private String recordedByName;

    public ProjectActivityDTO() {}

    public ProjectActivityDTO(String id, String projectId, String projectName, String activityTitle, String activityDescription, java.time.LocalDate activityDate, int progressPercentage, String recordedByName) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.activityTitle = activityTitle;
        this.activityDescription = activityDescription;
        this.activityDate = activityDate;
        this.progressPercentage = progressPercentage;
        this.recordedByName = recordedByName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
    public String getActivityDescription() { return activityDescription; }
    public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }
    public java.time.LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(java.time.LocalDate activityDate) { this.activityDate = activityDate; }
    public int getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
    public String getRecordedByName() { return recordedByName; }
    public void setRecordedByName(String recordedByName) { this.recordedByName = recordedByName; }
}
