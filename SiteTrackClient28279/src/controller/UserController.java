package controller;

import config.RMIConnection;
import dto.UserDTO;
import service.interfaces.AuthService;
import service.interfaces.UserService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private UserService getUserService() throws RemoteException {
        return RMIConnection.getInstance().getService(UserService.class);
    }

    private AuthService getAuthService() throws RemoteException {
        return RMIConnection.getInstance().getService(AuthService.class);
    }

    public List<UserDTO> getAllUsers() {
        try {
            return getUserService().getAllUsers();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public UserDTO getUserById(String userId) {
        try {
            return getUserService().getUserById(userId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserDTO createUser(UserDTO userDTO) {
        try {
            UserDTO created = getUserService().createUser(userDTO);
            JOptionPane.showMessageDialog(null, "User created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return created;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {
        try {
            UserDTO updated = getUserService().updateUser(userDTO);
            JOptionPane.showMessageDialog(null, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return updated;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean deactivateUser(String userId) {
        try {
            boolean success = getUserService().deactivateUser(userId);
            if (success) {
                JOptionPane.showMessageDialog(null, "User deactivated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to deactivate user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean activateUser(String userId) {
        try {
            boolean success = getUserService().activateUser(userId);
            if (success) {
                JOptionPane.showMessageDialog(null, "User activated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to activate user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean resetPassword(String userId, String newPassword) {
        try {
            boolean success = getAuthService().resetPassword(userId, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(null, "Password reset successfully. The user should change it on their next login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to reset password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean changePassword(String userId, String currentPassword, String newPassword) {
        try {
            boolean success = getAuthService().changePassword(userId, currentPassword, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(null, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to change password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        try {
            return getUserService().usernameExists(username);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkEmailExists(String email) {
        try {
            return getUserService().emailExists(email);
        } catch (RemoteException e) {
            return false;
        }
    }
}
