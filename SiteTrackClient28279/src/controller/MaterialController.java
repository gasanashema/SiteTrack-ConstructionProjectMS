package controller;

import config.RMIConnection;
import dto.MaterialCategoryDTO;
import dto.MaterialDTO;
import dto.MaterialPurchaseDTO;
import dto.MaterialUsageDTO;
import service.interfaces.MaterialCategoryService;
import service.interfaces.MaterialPurchaseService;
import service.interfaces.MaterialService;
import service.interfaces.MaterialUsageService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaterialController {

    private MaterialCategoryService getCategoryService() throws RemoteException {
        return RMIConnection.getInstance().getService(MaterialCategoryService.class);
    }

    private MaterialService getMaterialService() throws RemoteException {
        return RMIConnection.getInstance().getService(MaterialService.class);
    }

    private MaterialPurchaseService getPurchaseService() throws RemoteException {
        return RMIConnection.getInstance().getService(MaterialPurchaseService.class);
    }

    private MaterialUsageService getUsageService() throws RemoteException {
        return RMIConnection.getInstance().getService(MaterialUsageService.class);
    }

    // --- Categories ---
    public List<MaterialCategoryDTO> getAllCategories() {
        try {
            return getCategoryService().getAllCategories();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public boolean createCategory(MaterialCategoryDTO dto) {
        try {
            getCategoryService().createCategory(dto);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- Materials ---
    public List<MaterialDTO> getAllMaterials() {
        try {
            return getMaterialService().getAllMaterials();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load materials: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<MaterialDTO> getActiveMaterials() {
        try {
            return getMaterialService().getActiveMaterials();
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean createMaterial(MaterialDTO dto) {
        try {
            getMaterialService().createMaterial(dto);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create material: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateMaterial(MaterialDTO dto) {
        try {
            getMaterialService().updateMaterial(dto);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update material: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public MaterialDTO getMaterialById(String id) {
        try {
            return getMaterialService().getMaterialById(id);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean deactivateMaterial(String id) {
        try {
            return getMaterialService().deactivateMaterial(id);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activateMaterial(String id) {
        try {
            return getMaterialService().activateMaterial(id);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Purchases ---
    public boolean recordPurchase(MaterialPurchaseDTO dto) {
        try {
            getPurchaseService().recordPurchase(dto);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to record purchase: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<MaterialPurchaseDTO> getPurchasesByProject(String projectId) {
        try {
            return getPurchaseService().getPurchasesByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // --- Usages ---
    public boolean recordUsage(MaterialUsageDTO dto) {
        try {
            getUsageService().recordUsage(dto);
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
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
