package controller;

import config.RMIConnection;

import service.interfaces.AuditLogService;
import service.interfaces.SystemLogService;
import dto.AuditLogDTO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SystemAdminController {

    public String getServerHost() {
        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/client.properties"));
            return props.getProperty("rmi.server.host", "localhost");
        } catch (Exception e) {
            return "localhost";
        }
    }

    public String getServerPort() {
        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/client.properties"));
            return props.getProperty("rmi.server.port", "1099");
        } catch (Exception e) {
            return "1099";
        }
    }

    public long[] getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory() / (1024 * 1024);
        long freeMem = runtime.freeMemory() / (1024 * 1024);
        long usedMem = totalMem - freeMem;
        return new long[]{usedMem, totalMem};
    }

    public List<AuditLogDTO> getAuditLogs() {
        try {
            AuditLogService service = RMIConnection.getInstance().getService(AuditLogService.class);
            return service.getRecentAuditLogs(100);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch audit logs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<String[]> getSystemLogs() {
        try {
            SystemLogService service = RMIConnection.getInstance().getService(SystemLogService.class);
            return service.getRecentLogs(200);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch system logs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
}
