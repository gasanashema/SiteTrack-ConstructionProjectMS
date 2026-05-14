package controller;

import config.RMIConnection;
import dto.ProjectDTO;
import dto.ProjectManagerDTO;
import dto.ProjectSummaryDTO;
import service.interfaces.ProjectService;
import service.interfaces.ReportService;
import session.SessionManager;

import javax.swing.JOptionPane;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ProjectController {

    private ProjectService getProjectService() throws RemoteException {
        return RMIConnection.getInstance().getService(ProjectService.class);
    }

    public List<ProjectDTO> getAllProjects() {
        try {
            if (SessionManager.getInstance().isAdmin()) {
                return getProjectService().getAllProjects();
            } else {
                return getProjectService().getProjectsManagedBy(SessionManager.getInstance().getCurrentUserId());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch projects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public ProjectDTO getProjectById(String projectId) {
        try {
            return getProjectService().getProjectById(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load project details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        try {
            // Validation
            if (projectDTO.getProjectName() == null || projectDTO.getProjectName().trim().isEmpty() ||
                projectDTO.getLocation() == null || projectDTO.getLocation().trim().isEmpty() ||
                projectDTO.getStartDate() == null || projectDTO.getExpectedEndDate() == null) {
                JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (projectDTO.getExpectedEndDate().isBefore(projectDTO.getStartDate())) {
                JOptionPane.showMessageDialog(null, "End date cannot be before start date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            // Assuming createProject relies on SessionManager on server, but we pass DTO.
            // createdBy logic is typically handled in server, but we can just call it.
            ProjectDTO created = getProjectService().createProject(projectDTO);
            JOptionPane.showMessageDialog(null, "Project created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return created;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create project: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        try {
            // Validation
            if (projectDTO.getId() == null || projectDTO.getId().trim().isEmpty() ||
                projectDTO.getProjectName() == null || projectDTO.getProjectName().trim().isEmpty() ||
                projectDTO.getLocation() == null || projectDTO.getLocation().trim().isEmpty() ||
                projectDTO.getStartDate() == null || projectDTO.getExpectedEndDate() == null) {
                JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (projectDTO.getExpectedEndDate().isBefore(projectDTO.getStartDate())) {
                JOptionPane.showMessageDialog(null, "End date cannot be before start date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            ProjectDTO updated = getProjectService().updateProject(projectDTO);
            JOptionPane.showMessageDialog(null, "Project updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return updated;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update project: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean deleteProject(String projectId) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete this project?\nThis action cannot be undone.", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }

        try {
            boolean success = getProjectService().deleteProject(projectId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Project deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete project: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean changeProjectStatus(String projectId, String newStatus) {
        try {
            boolean success = getProjectService().changeProjectStatus(projectId, newStatus);
            if (success) {
                JOptionPane.showMessageDialog(null, "Project status updated to " + newStatus, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean assignManager(String projectId, String userId) {
        try {
            boolean success = getProjectService().assignManager(projectId, userId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Manager assigned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Assignment Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to assign manager: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean removeManager(String projectId, String userId) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to remove this manager from the project?", 
            "Confirm Removal", JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }

        try {
            boolean success = getProjectService().removeManager(projectId, userId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Manager removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return success;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to remove manager: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<ProjectManagerDTO> getManagersByProject(String projectId) {
        try {
            return getProjectService().getManagersByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch managers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public ProjectSummaryDTO getProjectSummary(String projectId) {
        try {
            ReportService reportService = RMIConnection.getInstance().getService(ReportService.class);
            return reportService.getSiteManagerDashboardSummary(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch project summary: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
