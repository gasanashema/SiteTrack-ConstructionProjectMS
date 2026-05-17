package service.implementation;

import dao.AuditLogDao;
import dto.AuditLogDTO;
import model.AuditLog;
import service.interfaces.AuditLogService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogServiceImpl extends UnicastRemoteObject implements AuditLogService {
    private final AuditLogDao dao;

    public AuditLogServiceImpl() throws RemoteException {
        super();
        this.dao = new AuditLogDao();
    }

    private AuditLogDTO toDTO(AuditLog entity) {
        if (entity == null) return null;
        return new AuditLogDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getUsername(),
                entity.getEventType(),
                entity.getEntityName(),
                entity.getDetails(),
                entity.getIpAddress(),
                entity.getCreatedAt()
        );
    }

    @Override
    public AuditLogDTO logEvent(String userId, String username, String eventType, String entityName, String details, String ipAddress) throws RemoteException {
        try {
            AuditLog log = new AuditLog(userId, username, eventType, entityName, details, ipAddress, LocalDateTime.now());
            log = dao.save(log);
            return toDTO(log);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save audit log", e);
        }
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs() throws RemoteException {
        try {
            List<AuditLogDTO> list = new ArrayList<>();
            for (AuditLog log : dao.findAll()) {
                list.add(toDTO(log));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch audit logs", e);
        }
    }

    @Override
    public List<AuditLogDTO> getRecentAuditLogs(int maxResults) throws RemoteException {
        try {
            List<AuditLogDTO> list = new ArrayList<>();
            for (AuditLog log : dao.findRecent(maxResults)) {
                list.add(toDTO(log));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch recent audit logs", e);
        }
    }

    @Override
    public int deleteLogsOlderThan(int months) throws RemoteException {
        try {
            return dao.deleteOlderThan(months);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete old audit logs", e);
        }
    }
}
