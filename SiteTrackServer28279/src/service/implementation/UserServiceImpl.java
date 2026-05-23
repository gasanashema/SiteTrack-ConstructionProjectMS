package service.implementation;

import dao.UserDao;
import dto.UserDTO;
import model.User;
import model.ERole;
import model.EUserStatus;
import service.interfaces.UserService;
import util.BCrypt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
    private final UserDao dao;
    private final dao.AuditLogDao auditDao;

    public UserServiceImpl() throws RemoteException {
        super();
        this.dao = new UserDao();
        this.auditDao = new dao.AuditLogDao();
    }

    private UserDTO toDTO(User entity) {
        if (entity == null) return null;
        return new UserDTO(
            entity.getId(),
            entity.getFullName(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getRole().name(),
            entity.getPhone(),
            entity.getStatus().name()
        );
    }

    @Override
    public UserDTO createUser(UserDTO dto) throws RemoteException {
        try {
            if (dao.existsByUsername(dto.getUsername())) throw new IllegalArgumentException("Username already exists");
            if (dao.existsByEmail(dto.getEmail())) throw new IllegalArgumentException("Email already exists");

            User entity = new User();
            entity.setFullName(dto.getFullName());
            entity.setUsername(dto.getUsername());
            entity.setEmail(dto.getEmail());
            entity.setPassword(BCrypt.hashpw("defaultPassword123", BCrypt.gensalt())); // default pass
            entity.setRole(ERole.valueOf(dto.getRole()));
            entity.setPhone(dto.getPhone());
            entity.setStatus(EUserStatus.ACTIVE);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            entity = dao.save(entity);
            auditDao.save(new model.AuditLog(entity.getId(), entity.getUsername(), "USER_CREATED", "User", "Created new user: " + entity.getUsername(), "127.0.0.1", java.time.LocalDateTime.now()));
            return toDTO(entity);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create user");
        }
    }

    @Override
    public UserDTO updateUser(UserDTO dto) throws RemoteException {
        try {
            User entity = dao.findById(dto.getId());
            if (entity == null) throw new IllegalArgumentException("User not found");
            
            entity.setFullName(dto.getFullName());
            entity.setEmail(dto.getEmail());
            entity.setPhone(dto.getPhone());
            entity.setRole(ERole.valueOf(dto.getRole()));
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                entity.setStatus(EUserStatus.valueOf(dto.getStatus()));
            }
            entity.setUpdatedAt(LocalDateTime.now());
            entity = dao.update(entity);
            auditDao.save(new model.AuditLog(entity.getId(), entity.getUsername(), "USER_UPDATED", "User", "Updated user details for: " + entity.getUsername(), "127.0.0.1", java.time.LocalDateTime.now()));
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update user");
        }
    }

    @Override
    public boolean deactivateUser(String userId) throws RemoteException {
        try {
            User entity = dao.findById(userId);
            if (entity != null) {
                entity.setStatus(EUserStatus.INACTIVE);
                entity.setUpdatedAt(LocalDateTime.now());
                dao.update(entity);
                auditDao.save(new model.AuditLog(entity.getId(), entity.getUsername(), "USER_DEACTIVATED", "User", "Deactivated user: " + entity.getUsername(), "127.0.0.1", java.time.LocalDateTime.now()));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to deactivate user");
        }
    }

    @Override
    public boolean activateUser(String userId) throws RemoteException {
        try {
            User entity = dao.findById(userId);
            if (entity != null) {
                entity.setStatus(EUserStatus.ACTIVE);
                entity.setUpdatedAt(LocalDateTime.now());
                dao.update(entity);
                auditDao.save(new model.AuditLog(entity.getId(), entity.getUsername(), "USER_ACTIVATED", "User", "Activated user: " + entity.getUsername(), "127.0.0.1", java.time.LocalDateTime.now()));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to activate user");
        }
    }

    @Override
    public UserDTO getUserById(String userId) throws RemoteException {
        try {
            return toDTO(dao.findById(userId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() throws RemoteException {
        try {
            List<UserDTO> list = new ArrayList<>();
            for (User u : dao.findAll()) {
                list.add(toDTO(u));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch users");
        }
    }

    @Override
    public List<UserDTO> getAllActiveUsers() throws RemoteException {
        try {
            List<UserDTO> list = new ArrayList<>();
            for (User u : dao.findAllActive()) {
                list.add(toDTO(u));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch active users");
        }
    }

    @Override
    public List<UserDTO> getUsersByRole(String role) throws RemoteException {
        try {
            List<UserDTO> list = new ArrayList<>();
            for (User u : dao.findByRole(ERole.valueOf(role))) {
                list.add(toDTO(u));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch users by role");
        }
    }

    @Override
    public boolean usernameExists(String username) throws RemoteException {
        try {
            return dao.existsByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check username");
        }
    }

    @Override
    public boolean emailExists(String email) throws RemoteException {
        try {
            return dao.existsByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check email");
        }
    }
}
