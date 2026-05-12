package dto;

import java.io.Serializable;

public class NotificationLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String userFullName;
    private String eventType;
    private String message;
    private String channel;
    private String status;
    private java.time.LocalDateTime createdAt;

    public NotificationLogDTO() {}

    public NotificationLogDTO(String id, String userId, String userFullName, String eventType, String message, String channel, String status, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.eventType = eventType;
        this.message = message;
        this.channel = channel;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
}
