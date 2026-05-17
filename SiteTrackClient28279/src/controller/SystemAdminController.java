package controller;

import config.RMIConnection;

import javax.swing.*;
import java.io.File;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SystemAdminController {

    public boolean testServerConnection() {
        try {
            // Attempt to get a basic service to test connectivity
            RMIConnection.getInstance().getService(service.interfaces.AuthService.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    public List<String[]> getDummyAuditLogs() {
        List<String[]> logs = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        
        logs.add(new String[]{now.minusMinutes(5).format(dtf), "admin_user", "User Created", "User: jdoe", "Role: SITE_MANAGER", "192.168.1.5", "SUCCESS"});
        logs.add(new String[]{now.minusMinutes(30).format(dtf), "admin_user", "Project Updated", "Project: Bangui Mall", "Status: ONGOING", "192.168.1.5", "SUCCESS"});
        logs.add(new String[]{now.minusHours(1).format(dtf), "manager1", "Material Purchased", "Material: Cement", "Qty: +50 bags", "192.168.1.10", "SUCCESS"});
        logs.add(new String[]{now.minusHours(2).format(dtf), "manager1", "Login", "System", "Successful login", "192.168.1.10", "SUCCESS"});
        logs.add(new String[]{now.minusHours(5).format(dtf), "unknown", "Login", "System", "Invalid credentials", "10.0.0.5", "FAILURE"});
        
        return logs;
    }

    public List<String[]> getDummySystemLogs() {
        List<String[]> logs = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        
        logs.add(new String[]{now.minusMinutes(1).format(dtf), "INFO", "AuthService", "User admin logged in.", ""});
        logs.add(new String[]{now.minusMinutes(10).format(dtf), "WARN", "DatabaseManager", "Connection pool nearing capacity (80%).", ""});
        logs.add(new String[]{now.minusHours(1).format(dtf), "ERROR", "RMIConnection", "Timeout waiting for registry.", "java.rmi.ConnectException: Connection refused to host..."});
        logs.add(new String[]{now.minusHours(3).format(dtf), "INFO", "System", "Daily backup completed successfully.", ""});
        logs.add(new String[]{now.minusHours(5).format(dtf), "DEBUG", "ReportService", "Generated MaterialUsageReport for Project P-1001", ""});
        
        return logs;
    }

    public void simulateBackup(Runnable onSuccess, Runnable onError) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate a 3-second backup process
                Thread.sleep(3000);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    onSuccess.run();
                } catch (Exception e) {
                    onError.run();
                }
            }
        };
        worker.execute();
    }
    
    public void exportDataPlaceholder(String dataType, String format) {
        JOptionPane.showMessageDialog(null, 
            "Simulating export of " + dataType + " to " + format + " format.\nIn a real implementation, this would trigger an RMI stream or chunked download.", 
            "Export Simulation", JOptionPane.INFORMATION_MESSAGE);
    }
}
