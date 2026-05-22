package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "AUD"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_audit_logs_id")
        }
    )
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "username")
    private String username;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at", nullable = false)
    @javax.persistence.Convert(converter = util.LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public AuditLog(String userId, String username, String eventType, String entityName, String details, String ipAddress, LocalDateTime createdAt) {
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
