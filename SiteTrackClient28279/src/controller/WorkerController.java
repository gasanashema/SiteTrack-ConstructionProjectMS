package controller;

import config.RMIConnection;
import dto.SiteWorkerDTO;
import dto.WorkerTypeDTO;
import service.interfaces.SiteWorkerService;
import service.interfaces.WorkerTypeService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class WorkerController {

    private WorkerTypeService getWorkerTypeService() throws RemoteException {
        return RMIConnection.getInstance().getService(WorkerTypeService.class);
    }

    private SiteWorkerService getSiteWorkerService() throws RemoteException {
        return RMIConnection.getInstance().getService(SiteWorkerService.class);
    }

    // --- Worker Types ---

    public List<WorkerTypeDTO> getAllWorkerTypes() {
        try {
            return getWorkerTypeService().getAllWorkerTypes();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load worker types: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public WorkerTypeDTO createWorkerType(WorkerTypeDTO dto) {
        try {
            WorkerTypeDTO created = getWorkerTypeService().createWorkerType(dto);
            JOptionPane.showMessageDialog(null, "Worker type created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return created;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create worker type: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public WorkerTypeDTO updateWorkerType(WorkerTypeDTO dto) {
        try {
            WorkerTypeDTO updated = getWorkerTypeService().updateWorkerType(dto);
            JOptionPane.showMessageDialog(null, "Worker type updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return updated;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update worker type: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean deleteWorkerType(String workerTypeId) {
        try {
            boolean deleted = getWorkerTypeService().deleteWorkerType(workerTypeId);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Worker type deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return deleted;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete worker type: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- Site Workers ---

    public List<SiteWorkerDTO> getAllWorkers() {
        try {
            return getSiteWorkerService().getAllWorkers();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load workers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<SiteWorkerDTO> getActiveWorkers() {
        try {
            return getSiteWorkerService().getActiveWorkers();
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public SiteWorkerDTO createWorker(SiteWorkerDTO dto) {
        try {
            SiteWorkerDTO created = getSiteWorkerService().createWorker(dto);
            JOptionPane.showMessageDialog(null, "Worker registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return created;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to register worker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public SiteWorkerDTO updateWorker(SiteWorkerDTO dto) {
        try {
            SiteWorkerDTO updated = getSiteWorkerService().updateWorker(dto);
            JOptionPane.showMessageDialog(null, "Worker updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return updated;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update worker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean deactivateWorker(String workerId) {
        try {
            boolean deactivated = getSiteWorkerService().deactivateWorker(workerId);
            if (deactivated) {
                JOptionPane.showMessageDialog(null, "Worker deactivated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return deactivated;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to deactivate worker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean activateWorker(String workerId) {
        try {
            boolean activated = getSiteWorkerService().activateWorker(workerId);
            if (activated) {
                JOptionPane.showMessageDialog(null, "Worker activated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return activated;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to activate worker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteWorker(String workerId) {
        try {
            boolean deleted = getSiteWorkerService().deleteWorker(workerId); // Assuming service has this
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Worker deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return deleted;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete worker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- Worker Assignments ---

    public List<SiteWorkerDTO> getAssignedWorkersByProject(String projectId) {
        try {
            return getSiteWorkerService().getWorkersByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load assigned workers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<dto.WorkerAssignmentDTO> getActiveAssignments() {
        try {
            return getSiteWorkerService().getActiveAssignments();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load active assignments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public boolean assignWorkers(List<String> workerIds, String projectId, java.time.LocalDate date) {
        try {
            boolean success = getSiteWorkerService().assignWorkers(workerIds, projectId, date);
            if (success) {
                JOptionPane.showMessageDialog(null, "Workers assigned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to assign workers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean transferWorker(String workerId, String toProjectId, java.time.LocalDate date) {
        try {
            boolean success = getSiteWorkerService().transferWorker(workerId, toProjectId, date);
            if (success) {
                JOptionPane.showMessageDialog(null, "Worker transferred successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to transfer worker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
