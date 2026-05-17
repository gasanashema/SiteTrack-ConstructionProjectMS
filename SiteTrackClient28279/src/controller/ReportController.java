package controller;

import dto.DashboardSummaryDTO;
import dto.LaborCostReportDTO;
import dto.MaterialUsageReportDTO;
import dto.ProjectSummaryDTO;
import dto.StockMovementReportDTO;
import config.RMIConnection;
import service.interfaces.ReportService;
import util.ExportUtil;
import util.ReportHistoryManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    private ReportService getService() {
        return RMIConnection.getInstance().getService(ReportService.class);
    }

    public DashboardSummaryDTO getAdminDashboard() {
        String cacheKey = "admin_dashboard";
        Object cached = ReportHistoryManager.getInstance().getCachedReport(cacheKey);
        if (cached != null) return (DashboardSummaryDTO) cached;

        try {
            DashboardSummaryDTO dto = getService().getAdminDashboardSummary();
            ReportHistoryManager.getInstance().cacheReport(cacheKey, dto, "Admin Dashboard Summary");
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ProjectSummaryDTO getProjectSummary(String projectId) {
        if (projectId == null || projectId.isEmpty()) return null;

        String cacheKey = "project_summary_" + projectId;
        Object cached = ReportHistoryManager.getInstance().getCachedReport(cacheKey);
        if (cached != null) return (ProjectSummaryDTO) cached;

        try {
            ProjectSummaryDTO dto = getService().getSiteManagerDashboardSummary(projectId);
            ReportHistoryManager.getInstance().cacheReport(cacheKey, dto, "Project Summary: " + projectId);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load project summary: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public MaterialUsageReportDTO getMaterialUsageReport(String projectId, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            JOptionPane.showMessageDialog(null, "'From' date must be before or equal to 'To' date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String cacheKey = "mat_usage_" + projectId + "_" + from + "_" + to;
        Object cached = ReportHistoryManager.getInstance().getCachedReport(cacheKey);
        if (cached != null) return (MaterialUsageReportDTO) cached;

        try {
            MaterialUsageReportDTO dto = getService().getMaterialUsageReport(projectId, from, to);
            ReportHistoryManager.getInstance().cacheReport(cacheKey, dto, "Material Usage Report");
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load material usage report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public StockMovementReportDTO getStockMovementReport(String projectId, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            JOptionPane.showMessageDialog(null, "'From' date must be before or equal to 'To' date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String cacheKey = "stock_move_" + projectId + "_" + from + "_" + to;
        Object cached = ReportHistoryManager.getInstance().getCachedReport(cacheKey);
        if (cached != null) return (StockMovementReportDTO) cached;

        try {
            StockMovementReportDTO dto = getService().getStockMovementReport(projectId, from, to);
            ReportHistoryManager.getInstance().cacheReport(cacheKey, dto, "Stock Movement Report");
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load stock movement report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public LaborCostReportDTO getLaborCostReport(String projectId, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            JOptionPane.showMessageDialog(null, "'From' date must be before or equal to 'To' date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String cacheKey = "labor_cost_" + projectId + "_" + from + "_" + to;
        Object cached = ReportHistoryManager.getInstance().getCachedReport(cacheKey);
        if (cached != null) return (LaborCostReportDTO) cached;

        try {
            LaborCostReportDTO dto = getService().getLaborCostReport(projectId, from, to);
            ReportHistoryManager.getInstance().cacheReport(cacheKey, dto, "Labor Cost Report");
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load labor cost report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean exportToCSV(List<?> data, String reportName, String filePath) {
        if (filePath == null || filePath.trim().isEmpty() || reportName == null || reportName.trim().isEmpty()) {
            return false;
        }
        try {
            String csvData = ExportUtil.generateCSV(data);
            ExportUtil.writeCSVFile(csvData, filePath, reportName);
            JOptionPane.showMessageDialog(null, "Report exported successfully to: " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean exportToExcel(List<?> data, String reportName, String filePath) {
        if (filePath == null || filePath.trim().isEmpty() || reportName == null || reportName.trim().isEmpty()) {
            return false;
        }
        try {
            ExportUtil.writeExcelFile(data, filePath, reportName, "Data");
            JOptionPane.showMessageDialog(null, "Report exported successfully to: " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean exportToPDF(List<?> data, String reportName, String filePath, String title, String subtitle) {
        if (filePath == null || filePath.trim().isEmpty() || reportName == null || reportName.trim().isEmpty()) {
            return false;
        }
        try {
            ExportUtil.writePDFFile(data, filePath, reportName, title, subtitle);
            JOptionPane.showMessageDialog(null, "Report exported successfully to: " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String openFileDialog(String initialPath) {
        JFileChooser chooser = new JFileChooser(initialPath != null ? initialPath : System.getProperty("user.home") + "/Downloads");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chooser.getSelectedFile();
            return selectedFolder.getAbsolutePath();
        }
        return null;
    }
}
