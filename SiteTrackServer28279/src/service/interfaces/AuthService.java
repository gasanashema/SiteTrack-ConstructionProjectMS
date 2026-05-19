package service.interfaces;

import dto.LoginResponseDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthService extends Remote {
    LoginResponseDTO login(String username, String password) throws RemoteException;
    boolean verifyOtp(String userId, String otpCode) throws RemoteException;
    boolean changePassword(String userId, String currentPassword, String newPassword) throws RemoteException;
    boolean resetPassword(String userId, String newPassword) throws RemoteException;
    boolean isAccountActive(String userId) throws RemoteException;
    boolean resendOtp(String userId) throws RemoteException;
    LoginResponseDTO initiatePasswordReset(String emailOrUsername) throws RemoteException;
}
