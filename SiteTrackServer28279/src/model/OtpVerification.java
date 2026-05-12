package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "otp_verifications")
public class OtpVerification implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "OTP"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_otp_verifications_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public OtpVerification() {
    }

    public OtpVerification(String id, User user, String otpCode, LocalDateTime expiresAt, boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.otpCode = otpCode;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean isUsed) { this.isUsed = isUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "OtpVerification{" + "id=" + id + ", user=" + (user != null ? user.getId() : null) + ", isUsed=" + isUsed + '}';
    }
}
