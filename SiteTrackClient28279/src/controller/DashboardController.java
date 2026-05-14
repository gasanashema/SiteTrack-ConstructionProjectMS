package controller;

import config.RMIConnection;
import dto.DashboardSummaryDTO;
import dto.ProjectDTO;
import dto.ProjectMaterialStockDTO;
import dto.ProjectSummaryDTO;
import service.interfaces.ReportService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    
    private DashboardSummaryDTO cachedAdminDashboard;

    public DashboardSummaryDTO getAdminDashboard() {
        try {
            ReportService reportService = RMIConnection.getInstance().getService(ReportService.class);
            cachedAdminDashboard = reportService.getAdminDashboardSummary();
            return cachedAdminDashboard;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load admin dashboard data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ProjectSummaryDTO getSiteManagerDashboard(String projectId) {
        try {
            ReportService reportService = RMIConnection.getInstance().getService(ReportService.class);
            return reportService.getSiteManagerDashboardSummary(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load project dashboard data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<ProjectDTO> getRecentProjects() {
        if (cachedAdminDashboard != null && cachedAdminDashboard.getRecentProjects() != null) {
            return cachedAdminDashboard.getRecentProjects();
        }
        return new ArrayList<>();
    }

    public List<ProjectMaterialStockDTO> getLowStockAlerts() {
        if (cachedAdminDashboard != null && cachedAdminDashboard.getLowStockAlerts() != null) {
            return cachedAdminDashboard.getLowStockAlerts();
        }
        return new ArrayList<>();
    }
}
