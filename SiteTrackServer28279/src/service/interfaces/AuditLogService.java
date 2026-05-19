package service.interfaces;

import dto.AuditLogDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AuditLogService extends Remote {
    AuditLogDTO logEvent(String userId, String username, String eventType, String entityName, String details, String ipAddress) throws RemoteException;
    List<AuditLogDTO> getAllAuditLogs() throws RemoteException;
    List<AuditLogDTO> getRecentAuditLogs(int maxResults) throws RemoteException;
    int deleteLogsOlderThan(int months) throws RemoteException;
}
