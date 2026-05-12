package service.interfaces;

import dto.UserDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserService extends Remote {
    UserDTO createUser(UserDTO userDTO) throws RemoteException;
    UserDTO updateUser(UserDTO userDTO) throws RemoteException;
    boolean deactivateUser(String userId) throws RemoteException;
    boolean activateUser(String userId) throws RemoteException;
    UserDTO getUserById(String userId) throws RemoteException;
    List<UserDTO> getAllUsers() throws RemoteException;
    List<UserDTO> getAllActiveUsers() throws RemoteException;
    List<UserDTO> getUsersByRole(String role) throws RemoteException;
    boolean usernameExists(String username) throws RemoteException;
    boolean emailExists(String email) throws RemoteException;
}
