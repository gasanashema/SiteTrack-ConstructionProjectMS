package service.interfaces;

import dto.SiteWorkerDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SiteWorkerService extends Remote {
    SiteWorkerDTO createWorker(SiteWorkerDTO dto) throws RemoteException;
    SiteWorkerDTO updateWorker(SiteWorkerDTO dto) throws RemoteException;
    boolean deactivateWorker(String workerId) throws RemoteException;
    boolean activateWorker(String workerId) throws RemoteException;
    SiteWorkerDTO getWorkerById(String workerId) throws RemoteException;
    List<SiteWorkerDTO> getAllWorkers() throws RemoteException;
    List<SiteWorkerDTO> getActiveWorkers() throws RemoteException;
    List<SiteWorkerDTO> getWorkersByType(String workerTypeId) throws RemoteException;
    List<SiteWorkerDTO> getWorkersByProject(String projectId) throws RemoteException;
}
