package dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String username;
    private String eventType;
    private String entityName;
    private String details;
    private String ipAddress;
    private LocalDateTime createdAt;

    public AuditLogDTO() {}

    public AuditLogDTO(String id, String userId, String username, String eventType, String entityName, String details, String ipAddress, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.eventType = eventType;
        this.entityName = entityName;
        this.details = details;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
