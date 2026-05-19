package view.dashboard;

import controller.DashboardController;
import dto.DashboardSummaryDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DashboardRefreshManager {
    
    private static DashboardRefreshManager instance;
    private Timer refreshTimer;
    private int refreshIntervalSeconds = 60;
    private List<DashboardPanel> registeredPanels = new ArrayList<>();
    private boolean isAutoRefreshEnabled = true;

    private DashboardRefreshManager() {}

    public static DashboardRefreshManager getInstance() {
        if (instance == null) {
            instance = new DashboardRefreshManager();
        }
        return instance;
    }

    public void registerPanel(DashboardPanel panel) {
        if (!registeredPanels.contains(panel)) {
            registeredPanels.add(panel);
        }
    }

    public void unregisterPanel(DashboardPanel panel) {
        registeredPanels.remove(panel);
    }

    public void setRefreshInterval(int seconds) {
        this.refreshIntervalSeconds = seconds;
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.setDelay(seconds * 1000);
        }
    }

    public void startAutoRefresh() {
        if (refreshTimer == null) {
            refreshTimer = new Timer(refreshIntervalSeconds * 1000, e -> {
                if (isAutoRefreshEnabled) {
                    forceRefresh();
                }
            });
        }
        if (!refreshTimer.isRunning()) {
            refreshTimer.start();
        }
    }

    public void stopAutoRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    public void forceRefresh() {
        for (DashboardPanel panel : new ArrayList<>(registeredPanels)) {
            refresh(panel);
        }
    }

    public void refresh(DashboardPanel panel) {
        // Show loading indicator
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Pre-fetch the data to cache it in the controller
                // The controller will determine if it needs Admin or SiteManager data
                // based on session. Here we just trigger the UI's own refresh logic which calls the controller.
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    panel.refreshDashboard(); // the panel handles rebuilding the UI
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    panel.setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }
}
