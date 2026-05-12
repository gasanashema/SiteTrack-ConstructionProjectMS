package service.interfaces;

import dto.WorkerTypeDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WorkerTypeService extends Remote {
    WorkerTypeDTO createWorkerType(WorkerTypeDTO dto) throws RemoteException;
    WorkerTypeDTO updateWorkerType(WorkerTypeDTO dto) throws RemoteException;
    boolean deleteWorkerType(String workerTypeId) throws RemoteException;
    WorkerTypeDTO getWorkerTypeById(String workerTypeId) throws RemoteException;
    List<WorkerTypeDTO> getAllWorkerTypes() throws RemoteException;
    boolean workerTypeNameExists(String typeName) throws RemoteException;
}
