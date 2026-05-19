package controller;

import config.RMIConnection;
import dto.LoginResponseDTO;
import dto.UserDTO;
import service.interfaces.AuthService;
import session.SessionManager;
import view.MainFrame;

import javax.swing.JOptionPane;
import java.rmi.RemoteException;

public class AuthController {
    
    public LoginResponseDTO attemptLogin(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password required");
        }
        
        try {
            AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
            return authService.login(username, password);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, 
                "Server connection error: " + e.getMessage() + "\nPlease try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return new LoginResponseDTO(false, "Connection error", null, null, null, null);
        }
    }
    
    public boolean verifyOtp(String userId, String otpCode, String role, String fullName, String otpId) {
        if (otpCode == null || otpCode.length() != 6 || !otpCode.matches("\\d+")) {
            throw new IllegalArgumentException("OTP must be 6 digits");
        }
        
        try {
            AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
            boolean isValid = authService.verifyOtp(userId, otpCode);
            if (isValid) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(userId);
                userDTO.setRole(role);
                // We'll map full name into UserDTO later when we know exactly how UserDTO is structured
                // For now, let's just create it and hope it sets properly or at least the session holds it.
                SessionManager.getInstance().setUser(userDTO, otpId);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid OTP. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, 
                "Server connection error: " + e.getMessage() + "\nPlease try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean resendOtp(String userId) {
        try {
            AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
            return authService.resendOtp(userId);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, 
                "Server connection error: " + e.getMessage() + "\nPlease try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void logout(MainFrame mainFrame) {
        SessionManager.getInstance().logout();
        mainFrame.switchPanel("LoginPanel");
    }

    public LoginResponseDTO initiatePasswordReset(String emailOrUsername) {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Email or username is required");
        }
        try {
            AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
            return authService.initiatePasswordReset(emailOrUsername);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Server connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new LoginResponseDTO(false, "Connection error", null, null, null, null);
        }
    }

    public boolean verifyPasswordResetOtp(String userId, String otpCode) {
        if (otpCode == null || otpCode.length() != 6 || !otpCode.matches("\\d+")) {
            throw new IllegalArgumentException("OTP must be 6 digits");
        }
        try {
            AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
            boolean isValid = authService.verifyOtp(userId, otpCode);
            if (!isValid) {
                JOptionPane.showMessageDialog(null, "Invalid OTP. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return isValid;
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Server connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean resetPassword(String userId, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password is required");
        }
        try {
            AuthService authService = RMIConnection.getInstance().getService(AuthService.class);
            return authService.resetPassword(userId, newPassword);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Server connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
