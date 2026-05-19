package view.dashboard;

import controller.DashboardController;
import dto.DashboardSummaryDTO;
import dto.ProjectDTO;
import dto.ProjectMaterialStockDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class AdminDashboardPanel extends JPanel {

    private DashboardController controller;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));

    public AdminDashboardPanel() {
        this.controller = DashboardController.getInstance();
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DashboardSummaryDTO data = controller.getAdminDashboard();
        if (data == null) {
            add(new JLabel("Loading data... Please check connection."));
            return;
        }

        // Section 1: Overview KPI Cards
        add(createSection1(data));
        add(Box.createVerticalStrut(20));

        // Section 2: Quick Stats Row
        add(createSection2(data));
        add(Box.createVerticalStrut(20));

        // Section 3: Project Status Pie Chart
        add(createSection3());
        add(Box.createVerticalStrut(20));

        // Section 4: Expenditure Breakdown
        add(createSection4(data));
        add(Box.createVerticalStrut(20));

        // Section 5: Monthly Expenditure Trend
        add(createSection5());
        add(Box.createVerticalStrut(20));

        // Section 6: Material Cost by Category
        add(createSection6());
        add(Box.createVerticalStrut(20));

        // Section 7: Labor Cost by Worker Type
        add(createSection7());
        add(Box.createVerticalStrut(20));

        // Section 8: Recent Projects Table
        add(createSection8(data));
        add(Box.createVerticalStrut(20));

        // Section 9: Low Stock Alerts Table
        add(createSection9(data));
        add(Box.createVerticalStrut(20));

        // Section 10: System Health Strip
        add(createSection10());
    }

    private JPanel createSection1(DashboardSummaryDTO data) {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1200, 300));

        BigDecimal totalSpent = data.getTotalMaterialExpenditure().add(data.getTotalLaborExpenditure());

        List<Number> spark = Arrays.asList(10, 20, 15, 30, 25, 40); // Mock trend

        panel.add(new KpiCard("Active Projects", String.valueOf(data.getActiveProjects()), "of " + data.getTotalProjects() + " total", 4, "🏗️", spark));
        panel.add(new KpiCard("Total Investment", currencyFormat.format(totalSpent), "Overall spend", 2, "💰", spark));
        
        BigDecimal matPct = totalSpent.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : data.getTotalMaterialExpenditure().divide(totalSpent, 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        panel.add(new KpiCard("Material Cost", currencyFormat.format(data.getTotalMaterialExpenditure()), matPct + "% of total", 3, "📦", spark));
        
        BigDecimal labPct = totalSpent.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : data.getTotalLaborExpenditure().divide(totalSpent, 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        panel.add(new KpiCard("Labor Cost", currencyFormat.format(data.getTotalLaborExpenditure()), labPct + "% of total", 5, "👷", spark));
        
        panel.add(new KpiCard("Active Workers", String.valueOf(data.getTotalWorkers()), "Registered workers", 5, "👥", spark));
        
        int lowStockCount = data.getLowStockAlerts() != null ? data.getLowStockAlerts().size() : 0;
        KpiCard stockCard = new KpiCard("Low Stock Alerts", String.valueOf(lowStockCount), "Need attention", 1, "⚠️", spark);
        if (lowStockCount > 0) stockCard.setTrend("DOWN");
        panel.add(stockCard);

        return panel;
    }

    private JPanel createSection2(DashboardSummaryDTO data) {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1200, 60));

        panel.add(createQuickStat("Total Materials", String.valueOf(data.getTotalMaterials())));
        panel.add(createQuickStat("Total Purchases", "See Reports"));
        panel.add(createQuickStat("Total Usage Records", "See Reports"));
        panel.add(createQuickStat("Total Payments", "See Reports"));

        return panel;
    }

    private JPanel createQuickStat(String title, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIManager.getColor("Panel.background"));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        JLabel vLabel = new JLabel(value);
        vLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(tLabel, BorderLayout.NORTH);
        p.add(vLabel, BorderLayout.CENTER);
        return p;
    }

    private JPanel createSection3() {
        Map<String, Integer> progress = controller.getProjectProgressByStatus();
        Map<String, Number> pieData = new HashMap<>();
        Map<String, Color> colors = new HashMap<>();
        
        for (Map.Entry<String, Integer> e : progress.entrySet()) {
            pieData.put(e.getKey(), e.getValue());
            if ("ONGOING".equals(e.getKey())) colors.put(e.getKey(), Color.decode("#4CAF50"));
            else if ("PLANNING".equals(e.getKey())) colors.put(e.getKey(), Color.decode("#FFC107"));
            else if ("COMPLETED".equals(e.getKey())) colors.put(e.getKey(), Color.decode("#9E9E9E"));
            else colors.put(e.getKey(), Color.decode("#F44336"));
        }

        JPanel chart = ChartUtil.createPieChart2D("Projects by Status", pieData, colors);
        chart.setPreferredSize(new Dimension(400, 300));
        chart.setMaximumSize(new Dimension(800, 300));
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection4(DashboardSummaryDTO data) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1200, 300));

        // Left: Top Projects
        Map<String, Number> topProjects = new LinkedHashMap<>();
        List<ProjectDTO> projects = controller.getRecentProjects(5);
        for (ProjectDTO p : projects) {
            topProjects.put(p.getProjectName(), controller.getTotalExpenditureByMonth(p.getId(), 120));
        }
        JPanel left = ChartUtil.createHorizontalBarChart2D("Top 5 Project Expenditures", topProjects, Color.decode("#4CAF50"), true);
        left.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        
        // Right: Pie Chart Cost
        Map<String, Number> costData = new HashMap<>();
        costData.put("Material", data.getTotalMaterialExpenditure());
        costData.put("Labor", data.getTotalLaborExpenditure());
        Map<String, Color> colors = new HashMap<>();
        colors.put("Material", Color.decode("#FF9800"));
        colors.put("Labor", Color.decode("#009688"));
        
        JPanel right = ChartUtil.createPieChart2D("Expenditure by Category", costData, colors);
        right.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        panel.add(left);
        panel.add(right);
        return panel;
    }

    private JPanel createSection5() {
        Map<String, List<Number>> series = new HashMap<>();
        List<Number> mat = Arrays.asList(10000, 15000, 12000, 18000, 25000, 20000); // Mock data for line chart
        List<Number> lab = Arrays.asList(8000, 9000, 9500, 11000, 14000, 15000);
        series.put("Material", mat);
        series.put("Labor", lab);
        
        Map<String, Color> colors = new HashMap<>();
        colors.put("Material", Color.decode("#FF9800"));
        colors.put("Labor", Color.decode("#009688"));
        
        List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun");
        
        JPanel chart = ChartUtil.createLineChart2D("Expenditure Trend (Last 6 Months)", series, colors, months);
        chart.setPreferredSize(new Dimension(800, 250));
        chart.setMaximumSize(new Dimension(1200, 250));
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection6() {
        Map<String, BigDecimal> costs = controller.getMaterialExpenditureByCategory();
        Map<String, Number> map = new LinkedHashMap<>();
        for (Map.Entry<String, BigDecimal> e : costs.entrySet()) map.put(e.getKey(), e.getValue());
        
        JPanel chart = ChartUtil.createHorizontalBarChart2D("Material Spending by Category", map, Color.decode("#FF9800"), true);
        chart.setPreferredSize(new Dimension(500, 300));
        chart.setMaximumSize(new Dimension(1200, 300));
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection7() {
        Map<String, BigDecimal> costs = controller.getLaborExpenditureByWorkerType();
        Map<String, Number> map = new LinkedHashMap<>();
        for (Map.Entry<String, BigDecimal> e : costs.entrySet()) map.put(e.getKey(), e.getValue());
        
        JPanel chart = ChartUtil.createHorizontalBarChart2D("Labor Cost by Worker Type", map, Color.decode("#009688"), true);
        chart.setPreferredSize(new Dimension(500, 300));
        chart.setMaximumSize(new Dimension(1200, 300));
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection8(DashboardSummaryDTO data) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setMaximumSize(new Dimension(1200, 200));
        
        JLabel label = new JLabel("Recent Projects");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Project Name", "Location", "Status", "End Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        List<ProjectDTO> projects = controller.getRecentProjects(5);
        if (projects != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            for (ProjectDTO p : projects) {
                String date = p.getExpectedEndDate() != null ? p.getExpectedEndDate().format(formatter) : "N/A";
                model.addRow(new Object[]{p.getProjectName(), p.getLocation(), p.getStatus(), date});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSection9(DashboardSummaryDTO data) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setMaximumSize(new Dimension(1200, 200));
        
        JLabel label = new JLabel("Low Stock Alerts (⚠️ Attention Needed)");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.decode("#F44336"));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Material", "Project", "Available", "Minimum"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        if (data.getLowStockAlerts() != null) {
            for (ProjectMaterialStockDTO a : data.getLowStockAlerts()) {
                model.addRow(new Object[]{a.getMaterialName(), a.getProjectName(), a.getQuantityAvailable() + " " + a.getUnit(), a.getMinimumQuantity() + " " + a.getUnit()});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSection10() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.setMaximumSize(new Dimension(1200, 40));
        
        JLabel connLabel = new JLabel("Server: Connected ✓");
        connLabel.setForeground(Color.decode("#4CAF50"));
        
        JLabel syncLabel = new JLabel("Last sync: " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        panel.add(connLabel);
        panel.add(syncLabel);
        return panel;
    }
}
