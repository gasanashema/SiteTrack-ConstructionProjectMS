package dto;

import java.io.Serializable;

public class OtpVerificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String otpCode;
    private java.time.LocalDateTime expiresAt;
    private boolean isUsed;

    public OtpVerificationDTO() {}

    public OtpVerificationDTO(String id, String userId, String otpCode, java.time.LocalDateTime expiresAt, boolean isUsed) {
        this.id = id;
        this.userId = userId;
        this.otpCode = otpCode;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    public java.time.LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(java.time.LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isIsUsed() { return isUsed; }
    public void setIsUsed(boolean isUsed) { this.isUsed = isUsed; }
}
