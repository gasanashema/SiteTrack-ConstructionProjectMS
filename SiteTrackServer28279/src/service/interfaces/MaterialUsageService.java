package service.interfaces;

import dto.MaterialUsageDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface MaterialUsageService extends Remote {
    MaterialUsageDTO recordUsage(MaterialUsageDTO dto) throws RemoteException;
    MaterialUsageDTO updateUsage(MaterialUsageDTO dto) throws RemoteException;
    boolean deleteUsage(String usageId) throws RemoteException;
    MaterialUsageDTO getUsageById(String usageId) throws RemoteException;
    List<MaterialUsageDTO> getUsageByProject(String projectId) throws RemoteException;
    List<MaterialUsageDTO> getUsageByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException;
}
