package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "notification_logs")
public class NotificationLog implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ENotifChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ENotifStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public NotificationLog() {
    }

    public NotificationLog(String id, User user, String eventType, String message, ENotifChannel channel, ENotifStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.eventType = eventType;
        this.message = message;
        this.channel = channel;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public ENotifChannel getChannel() { return channel; }
    public void setChannel(ENotifChannel channel) { this.channel = channel; }
    public ENotifStatus getStatus() { return status; }
    public void setStatus(ENotifStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "NotificationLog{" + "id=" + id + ", user=" + (user != null ? user.getId() : null) + ", eventType=" + eventType + ", channel=" + channel + ", status=" + status + '}';
    }
}
