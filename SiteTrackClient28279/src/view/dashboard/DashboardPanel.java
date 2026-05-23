package view.dashboard;

import controller.DashboardController;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel contentPanel;
    private DashboardRefreshManager refreshManager;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.refreshManager = DashboardRefreshManager.getInstance();
        initUI();
        
        // Initial build
        refreshDashboard();
        
        // Register for auto refresh
        refreshManager.registerPanel(this);
        refreshManager.startAutoRefresh();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerActions.setOpaque(false);
        
        JButton recordUsageBtn = new JButton("+ Record Daily Usage");
        recordUsageBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        recordUsageBtn.setBackground(Color.decode("#FF5E14"));
        recordUsageBtn.setForeground(Color.WHITE);
        recordUsageBtn.setFocusPainted(false);
        recordUsageBtn.addActionListener(e -> {
            new view.materials.UsageFormDialog(mainFrame, new controller.MaterialController()).setVisible(true);
        });
        
        JButton refreshBtn = new JButton("↻ Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshManager.refresh(this));
        
        headerActions.add(recordUsageBtn);
        headerActions.add(refreshBtn);
        
        headerPanel.add(headerActions, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content Scroll Pane
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshDashboard() {
        contentPanel.removeAll();
        
        if (SessionManager.getInstance().isAdmin()) {
            contentPanel.add(new AdminDashboardPanel(), BorderLayout.CENTER);
        } else {
            contentPanel.add(new SiteManagerDashboardPanel(mainFrame), BorderLayout.CENTER);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
