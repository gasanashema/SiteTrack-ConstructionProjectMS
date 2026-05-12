package service.interfaces;

import dto.MaterialPurchaseDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface MaterialPurchaseService extends Remote {
    MaterialPurchaseDTO recordPurchase(MaterialPurchaseDTO dto) throws RemoteException;
    MaterialPurchaseDTO updatePurchase(MaterialPurchaseDTO dto) throws RemoteException;
    boolean deletePurchase(String purchaseId) throws RemoteException;
    MaterialPurchaseDTO getPurchaseById(String purchaseId) throws RemoteException;
    List<MaterialPurchaseDTO> getPurchasesByProject(String projectId) throws RemoteException;
    List<MaterialPurchaseDTO> getPurchasesByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException;
}
