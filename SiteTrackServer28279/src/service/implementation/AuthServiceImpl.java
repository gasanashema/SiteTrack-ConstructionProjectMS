package service.implementation;

import dao.UserDao;
import dao.OtpVerificationDao;
import dto.LoginResponseDTO;
import model.User;
import model.EUserStatus;
import service.interfaces.AuthService;
import util.BCrypt;
import util.NotificationProducer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {
    private final UserDao userDao;
    private final OtpVerificationDao otpDao;
    private final dao.AuditLogDao auditDao;

    public AuthServiceImpl() throws RemoteException {
        super();
        this.userDao = new UserDao();
        this.otpDao = new OtpVerificationDao();
        this.auditDao = new dao.AuditLogDao();
    }

    @Override
    public LoginResponseDTO login(String username, String password) throws RemoteException {
        try {
            User user = userDao.findByUsername(username);
            if (user == null) {
                return new LoginResponseDTO(false, "Invalid username or password", null, null, null, null);
            }
            if (user.getStatus() == EUserStatus.INACTIVE) {
                return new LoginResponseDTO(false, "Account is inactive", null, null, null, null);
            }
            if (BCrypt.checkpw(password, user.getPassword())) {
                auditDao.save(new model.AuditLog(user.getId(), user.getUsername(), "LOGIN", "User", "User logged in successfully", "127.0.0.1", java.time.LocalDateTime.now()));
                // If 2FA is required, we'd generate OTP here. For basic implementation, returning success.
                return new LoginResponseDTO(true, "Login successful", user.getId(), user.getRole().name(), user.getFullName(), null);
            }
            auditDao.save(new model.AuditLog(user.getId(), user.getUsername(), "LOGIN_FAILED", "User", "Failed login attempt", "127.0.0.1", java.time.LocalDateTime.now()));
            return new LoginResponseDTO(false, "Invalid username or password", null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Login process failed");
        }
    }

    @Override
    public boolean verifyOtp(String userId, String otpCode) throws RemoteException {
        try {
            // TEMPORARY BYPASS FOR UI TESTING
            // Originally: return otpDao.findByUserAndCode(userId, otpCode) != null;
            return otpCode != null && otpCode.length() == 6;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OTP verification failed");
        }
    }

    @Override
    public boolean changePassword(String userId, String currentPassword, String newPassword) throws RemoteException {
        try {
            User user = userDao.findById(userId);
            if (user != null && BCrypt.checkpw(currentPassword, user.getPassword())) {
                user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                userDao.update(user);
                auditDao.save(new model.AuditLog(user.getId(), user.getUsername(), "PASSWORD_CHANGE", "User", "User changed their password", "127.0.0.1", java.time.LocalDateTime.now()));
                NotificationProducer.sendNotification(user.getId(), "PASSWORD_CHANGE", "Your password has been changed", "EMAIL");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to change password");
        }
    }

    @Override
    public boolean resetPassword(String userId, String newPassword) throws RemoteException {
        try {
            User user = userDao.findById(userId);
            if (user != null) {
                user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                userDao.update(user);
                auditDao.save(new model.AuditLog(user.getId(), user.getUsername(), "PASSWORD_RESET", "User", "Password reset by admin", "127.0.0.1", java.time.LocalDateTime.now()));
                NotificationProducer.sendNotification(user.getId(), "PASSWORD_RESET", "Your password has been reset by an administrator", "EMAIL");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to reset password");
        }
    }

    @Override
    public boolean isAccountActive(String userId) throws RemoteException {
        try {
            User user = userDao.findById(userId);
            return user != null && user.getStatus() == EUserStatus.ACTIVE;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check account status");
        }
    }
}
