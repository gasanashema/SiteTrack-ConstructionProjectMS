package config;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;

import service.interfaces.AuthService;
import service.interfaces.MaterialCategoryService;
import service.interfaces.MaterialPurchaseService;
import service.interfaces.MaterialService;
import service.interfaces.MaterialStockService;
import service.interfaces.MaterialUsageService;
import service.interfaces.ProjectActivityService;
import service.interfaces.ProjectService;
import service.interfaces.ReportService;
import service.interfaces.SiteWorkerService;
import service.interfaces.UserService;
import service.interfaces.WorkerAttendanceService;
import service.interfaces.WorkerPaymentService;
import service.interfaces.WorkerTypeService;

public class RMIConnection {
    private static RMIConnection instance;
    private String serverUrl;

    private AuthService authService;
    private UserService userService;
    private ProjectService projectService;
    private MaterialCategoryService materialCategoryService;
    private MaterialService materialService;
    private MaterialPurchaseService materialPurchaseService;
    private MaterialStockService materialStockService;
    private MaterialUsageService materialUsageService;
    private ProjectActivityService projectActivityService;
    private WorkerTypeService workerTypeService;
    private SiteWorkerService siteWorkerService;
    private WorkerAttendanceService workerAttendanceService;
    private WorkerPaymentService workerPaymentService;
    private ReportService reportService;

    private RMIConnection() {
        try {
            Properties props = new Properties();
            InputStream in = getClass().getResourceAsStream("/config/config.properties");
            if (in == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            props.load(in);
            String host = props.getProperty("rmi.server.host", "localhost");
            String port = props.getProperty("rmi.server.port", "4567");
            this.serverUrl = "rmi://" + host + ":" + port;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config properties", e);
        }
    }

    public static RMIConnection getInstance() {
        if (instance == null) {
            instance = new RMIConnection();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T extends Remote> T getService(Class<T> serviceClass) {
        String serviceName = serviceClass.getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
        try {
            return (T) Naming.lookup(serverUrl + "/" + serviceName);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException("Cannot connect to server at " + serverUrl + " for service " + serviceName + ". Please verify server is running and config.properties is correct.", e);
        }
    }

    public void verifyConnection() {
        try {
            // Test connectivity by getting the AuthService stub
            getService(AuthService.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot connect to server at " + serverUrl + ". Please verify server is running and config.properties is correct.", e);
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
