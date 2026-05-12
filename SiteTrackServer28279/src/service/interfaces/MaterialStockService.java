package service.interfaces;

import dto.ProjectMaterialStockDTO;
import dto.MaterialStockMovementDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

public interface MaterialStockService extends Remote {
    List<ProjectMaterialStockDTO> getStockByProject(String projectId) throws RemoteException;
    ProjectMaterialStockDTO getStockByProjectAndMaterial(String projectId, String materialId) throws RemoteException;
    List<ProjectMaterialStockDTO> getLowStockByProject(String projectId) throws RemoteException;
    boolean updateMinimumQuantity(String stockId, BigDecimal minimumQty) throws RemoteException;
    boolean recordStockAdjustment(String projectId, String materialId, BigDecimal newQuantity, String description, String recordedById) throws RemoteException;
    List<MaterialStockMovementDTO> getMovementsByProject(String projectId) throws RemoteException;
    List<MaterialStockMovementDTO> getMovementsByProjectAndMaterial(String projectId, String materialId) throws RemoteException;
    List<MaterialStockMovementDTO> getMovementsByDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException;
}
