package dto;

import java.io.Serializable;

public class LoginResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private String userId;
    private String role;
    private String fullName;
    private String otpId;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message, String userId, String role, String fullName, String otpId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.role = role;
        this.fullName = fullName;
        this.otpId = otpId;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getOtpId() { return otpId; }
    public void setOtpId(String otpId) { this.otpId = otpId; }
}
