package controller;

import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import service.implementation.AuthServiceImpl;
import service.implementation.UserServiceImpl;
import service.implementation.ProjectServiceImpl;
import service.implementation.MaterialCategoryServiceImpl;
import service.implementation.MaterialServiceImpl;
import service.implementation.MaterialPurchaseServiceImpl;
import service.implementation.MaterialStockServiceImpl;
import service.implementation.MaterialUsageServiceImpl;
import service.implementation.ProjectActivityServiceImpl;
import service.implementation.WorkerTypeServiceImpl;
import service.implementation.SiteWorkerServiceImpl;
import service.implementation.WorkerAttendanceServiceImpl;
import service.implementation.WorkerPaymentServiceImpl;
import service.implementation.ReportServiceImpl;
import service.implementation.AuditLogServiceImpl;
import service.implementation.SystemLogServiceImpl;
import util.HibernateUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * SiteTrack Construction Manager - RMI Server Entry Point.
 *
 * Reads RMI port and hostname from config.properties, starts the RMI
 * registry, binds all 14 service implementations, and keeps the JVM alive.
 *
 * Mirrors the pattern used in LibraryManagementSystemServer/Server.java.
 */
public class Server {

    public static void main(String[] args) {
        setupLogger();
        
        try {
            // ── Step 1: Load configuration ──────────────────────────────────
            Properties config = new Properties();
            InputStream in = Server.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            if (in == null) {
                throw new RuntimeException(
                    "config.properties not found on classpath. " +
                    "Make sure src/config.properties exists.");
            }
            config.load(in);
            in.close();

            int    port     = Integer.parseInt(config.getProperty("rmi.port",     "4567"));
            String hostname = config.getProperty("rmi.hostname", "127.0.0.1");

            // ── Step 2: Configure RMI server hostname ────────────────────────
            System.setProperty("java.rmi.server.hostname", hostname);

            // ── Step 3: Warm up Hibernate (validates DB connection early) ────
            System.out.println("[SiteTrack] Initializing Hibernate / PostgreSQL connection...");
            HibernateUtil.getSessionFactory(); // triggers static init block
            System.out.println("[SiteTrack] Hibernate SessionFactory ready.");

            // ── Step 4: Create the RMI registry ─────────────────────────────
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("[SiteTrack] RMI registry started on port " + port);
            Logger.getLogger("SiteTrack").info("RMI registry started on port " + port);

            // ── Step 5: Bind all 14 service implementations ──────────────────
            registry.rebind("auth-service",              new AuthServiceImpl());
            registry.rebind("user-service",              new UserServiceImpl());
            registry.rebind("project-service",           new ProjectServiceImpl());
            registry.rebind("material-category-service", new MaterialCategoryServiceImpl());
            registry.rebind("material-service",          new MaterialServiceImpl());
            registry.rebind("material-purchase-service", new MaterialPurchaseServiceImpl());
            registry.rebind("material-stock-service",    new MaterialStockServiceImpl());
            registry.rebind("material-usage-service",    new MaterialUsageServiceImpl());
            registry.rebind("project-activity-service",  new ProjectActivityServiceImpl());
            registry.rebind("worker-type-service",       new WorkerTypeServiceImpl());
            registry.rebind("site-worker-service",       new SiteWorkerServiceImpl());
            registry.rebind("worker-attendance-service", new WorkerAttendanceServiceImpl());
            registry.rebind("worker-payment-service",    new WorkerPaymentServiceImpl());
            registry.rebind("report-service",            new ReportServiceImpl());
            registry.rebind("audit-log-service",         new AuditLogServiceImpl());
            registry.rebind("system-log-service",        new SystemLogServiceImpl());

            // ── Step 6: Confirm startup ──────────────────────────────────────
            System.out.println("========================================");
            System.out.println("  SiteTrack Construction Manager");
            System.out.println("  Server is RUNNING on port " + port);
            System.out.println("  14 services registered successfully.");
            System.out.println("========================================");

            // Keep the JVM alive (RMI threads are daemon threads by default)
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("[SiteTrack] Server startup FAILED:");
            e.printStackTrace();
            Logger.getLogger("SiteTrack").log(Level.SEVERE, "Server startup FAILED", e);
        }
    }
    
    private static void setupLogger() {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) logDir.mkdir();

            FileHandler fh = new FileHandler("logs/server.log", 5 * 1024 * 1024, 1, true);
            fh.setFormatter(new Formatter() {
                private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(df.format(new Date(record.getMillis()))).append("] ");
                    sb.append("[").append(record.getLevel().getName()).append("] ");
                    sb.append("[").append(record.getLoggerName()).append("] ");
                    sb.append(formatMessage(record));

                    if (record.getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        record.getThrown().printStackTrace(pw);
                        sb.append("||").append(sw.toString());
                    } else {
                        sb.append("||");
                    }
                    sb.append("\n");
                    return sb.toString();
                }
            });

            Logger rootLogger = Logger.getLogger("");
            // Remove default console handlers to prevent double logging if desired
            // for (Handler h : rootLogger.getHandlers()) rootLogger.removeHandler(h);
            rootLogger.addHandler(fh);
            rootLogger.setLevel(Level.INFO);
            
            Logger.getLogger("SiteTrack").info("Application logger initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
