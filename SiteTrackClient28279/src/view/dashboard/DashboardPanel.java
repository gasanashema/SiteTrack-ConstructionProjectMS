package view.dashboard;

import controller.DashboardController;
import dto.DashboardSummaryDTO;
import dto.ProjectDTO;
import dto.ProjectMaterialStockDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.List;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private DashboardController controller;
    private JPanel contentPanel;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new DashboardController();
        initUI();
        refreshDashboard();
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
        refreshBtn.addActionListener(e -> refreshDashboard());
        
        headerActions.add(recordUsageBtn);
        headerActions.add(refreshBtn);
        
        headerPanel.add(headerActions, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content Scroll Pane
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshDashboard() {
        contentPanel.removeAll();
        
        if (SessionManager.getInstance().isAdmin()) {
            buildAdminDashboard();
        } else {
            buildSiteManagerDashboard();
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void buildAdminDashboard() {
        DashboardSummaryDTO data = controller.getAdminDashboard();
        if (data == null) {
            JLabel errorLabel = new JLabel("Failed to load dashboard data.");
            errorLabel.setForeground(Color.RED);
            contentPanel.add(errorLabel);
            return;
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));

        // KPI Cards Grid
        JPanel kpiGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        kpiGrid.setMaximumSize(new Dimension(1200, 260));
        kpiGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        kpiGrid.add(new KpiCard("Active Projects", String.valueOf(data.getActiveProjects()), "out of " + data.getTotalProjects() + " total"));
        kpiGrid.add(new KpiCard("Total Workers", String.valueOf(data.getTotalWorkers()), "Active payroll entities"));
        kpiGrid.add(new KpiCard("Total Materials", String.valueOf(data.getTotalMaterials()), "Registered inventory items"));
        
        BigDecimal matCost = data.getTotalMaterialExpenditure() != null ? data.getTotalMaterialExpenditure() : BigDecimal.ZERO;
        kpiGrid.add(new KpiCard("Material Cost", currencyFormat.format(matCost), "Total spent on materials", 1));
        
        BigDecimal labCost = data.getTotalLaborExpenditure() != null ? data.getTotalLaborExpenditure() : BigDecimal.ZERO;
        kpiGrid.add(new KpiCard("Labor Cost", currencyFormat.format(labCost), "Total spent on labor", 2));
        
        BigDecimal totalCost = matCost.add(labCost);
        kpiGrid.add(new KpiCard("Total Expenditure", currencyFormat.format(totalCost), "Overall project costs", 3));

        contentPanel.add(kpiGrid);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Tables Section
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Recent Projects Table
        tablesPanel.add(createRecentProjectsPanel(data.getRecentProjects()));
        
        // Low Stock Table
        tablesPanel.add(createLowStockPanel(data.getLowStockAlerts()));

        contentPanel.add(tablesPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Charts Section
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        ModernPieChartPanel costChart = new ModernPieChartPanel("Expenditure Breakdown", matCost, labCost);
        costChart.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        chartsPanel.add(costChart);

        // Placeholder for second chart
        JPanel placeholderChart = new JPanel(new BorderLayout());
        placeholderChart.setBackground(UIManager.getColor("Panel.background"));
        placeholderChart.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel placeholderLabel = new JLabel("More Analytics Coming Soon", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholderLabel.setForeground(Color.GRAY);
        placeholderChart.add(placeholderLabel, BorderLayout.CENTER);
        chartsPanel.add(placeholderChart);

        contentPanel.add(chartsPanel);
    }

    private void buildSiteManagerDashboard() {
        JLabel label = new JLabel("Site Manager Dashboard - Select a project to view metrics");
        label.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        contentPanel.add(label);
    }

    private JPanel createRecentProjectsPanel(List<ProjectDTO> projects) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        
        JLabel label = new JLabel("Recent Projects");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Name", "Status", "End Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (projects != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            for (ProjectDTO p : projects) {
                String date = p.getExpectedEndDate() != null ? p.getExpectedEndDate().format(formatter) : "N/A";
                model.addRow(new Object[]{p.getProjectName(), p.getStatus(), date});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createLowStockPanel(List<ProjectMaterialStockDTO> alerts) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        
        JLabel label = new JLabel("Low Stock Alerts");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(UIManager.getColor("Component.error.focusedBorderColor"));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Project", "Material", "Qty"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (alerts != null) {
            for (ProjectMaterialStockDTO a : alerts) {
                model.addRow(new Object[]{a.getProjectName(), a.getMaterialName(), a.getQuantityAvailable() + " " + a.getUnit()});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
}
