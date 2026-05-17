package session;

import dto.UserDTO;

public class SessionManager {
    private static SessionManager instance;
    private UserDTO currentUser;
    private String otpId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUser(UserDTO user, String otpId) {
        this.currentUser = user;
        this.otpId = otpId;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    public String getCurrentUserRole() {
        return currentUser != null && currentUser.getRole() != null ? currentUser.getRole() : null;
    }

    public String getCurrentUserName() {
        // Assume getFullName() or similar exists in UserDTO. Let's use getFirstName + getLastName or similar
        // Based on typical user DTOs, let's hope it has getFullName(). If not, compiler will catch it.
        // Wait, the prompt said `currentUser.getFullName()`
        return currentUser != null ? currentUser.getFullName() : null;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(getCurrentUserRole());
    }

    public boolean isSiteManager() {
        return "SITE_MANAGER".equals(getCurrentUserRole());
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
        this.otpId = null;
    }

    public String getOtpId() {
        return otpId;
    }
}
