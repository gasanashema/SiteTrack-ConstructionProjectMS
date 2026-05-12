package service.interfaces;

import dto.MaterialDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MaterialService extends Remote {
    MaterialDTO createMaterial(MaterialDTO dto) throws RemoteException;
    MaterialDTO updateMaterial(MaterialDTO dto) throws RemoteException;
    boolean deactivateMaterial(String materialId) throws RemoteException;
    boolean activateMaterial(String materialId) throws RemoteException;
    boolean deleteMaterial(String materialId) throws RemoteException;
    MaterialDTO getMaterialById(String materialId) throws RemoteException;
    List<MaterialDTO> getAllMaterials() throws RemoteException;
    List<MaterialDTO> getActiveMaterials() throws RemoteException;
    List<MaterialDTO> getMaterialsByCategory(String categoryId) throws RemoteException;
}
