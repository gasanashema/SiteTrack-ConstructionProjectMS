package controller;

import config.RMIConnection;
import dto.MaterialStockMovementDTO;
import dto.MaterialUsageDTO;
import dto.ProjectMaterialStockDTO;
import service.interfaces.MaterialStockService;
import service.interfaces.MaterialUsageService;

import javax.swing.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class StockController {

    private MaterialStockService getStockService() throws RemoteException {
        return RMIConnection.getInstance().getService(MaterialStockService.class);
    }

    private MaterialUsageService getUsageService() throws RemoteException {
        return RMIConnection.getInstance().getService(MaterialUsageService.class);
    }

    // --- Stock ---
    public List<ProjectMaterialStockDTO> getStockByProject(String projectId) {
        try {
            return getStockService().getStockByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public ProjectMaterialStockDTO getStockByProjectAndMaterial(String projectId, String materialId) {
        try {
            return getStockService().getStockByProjectAndMaterial(projectId, materialId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProjectMaterialStockDTO> getLowStockByProject(String projectId) {
        try {
            return getStockService().getLowStockByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateMinimumQuantity(String stockId, BigDecimal minimumQty) {
        try {
            return getStockService().updateMinimumQuantity(stockId, minimumQty);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update min quantity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean recordStockAdjustment(String projectId, String materialId, BigDecimal newQuantity, String description, String recordedById) {
        try {
            return getStockService().recordStockAdjustment(projectId, materialId, newQuantity, description, recordedById);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to adjust stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- Movements ---
    public List<MaterialStockMovementDTO> getMovementsByProject(String projectId) {
        try {
            return getStockService().getMovementsByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // --- Usage ---
    public boolean recordUsage(MaterialUsageDTO dto) {
        try {
            getUsageService().recordUsage(dto);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to record usage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<MaterialUsageDTO> getUsageByProject(String projectId) {
        try {
            return getUsageService().getUsageByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
