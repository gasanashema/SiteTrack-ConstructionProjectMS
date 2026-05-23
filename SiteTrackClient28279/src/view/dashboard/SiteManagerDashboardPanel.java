package view.dashboard;

import controller.DashboardController;
import dto.ProjectSummaryDTO;
import dto.ProjectMaterialStockDTO;
import dto.WorkerAttendanceDTO;
import dto.MaterialUsageDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class SiteManagerDashboardPanel extends JPanel {

    private DashboardController controller;
    private controller.MaterialController materialController;
    private controller.StockController stockController;
    private MainFrame mainFrame;
    private String currentProjectId;
    private dto.ProjectDTO projectInfo;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));

    public SiteManagerDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = DashboardController.getInstance();
        this.materialController = new controller.MaterialController();
        this.stockController = new controller.StockController();
        
        List<dto.ProjectDTO> projects = controller.getRecentProjects(1);
        if (projects != null && !projects.isEmpty()) {
            this.projectInfo = projects.get(0);
            this.currentProjectId = this.projectInfo.getId();
        }
        
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (currentProjectId == null) {
            add(new JLabel("No active project assigned to you."));
            return;
        }

        ProjectSummaryDTO data = controller.getSiteManagerDashboard(currentProjectId);
        if (data == null) {
            add(new JLabel("Loading project data..."));
            return;
        }

        add(createSection1(data));
        add(Box.createVerticalStrut(20));

        add(createSection2(data));
        add(Box.createVerticalStrut(20));

        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setOpaque(false);
        // Using a more flexible layout and not restricting width so it shrinks naturally
        row2.add(createSection5());
        row2.add(createSection10(data));
        add(row2);
        add(Box.createVerticalStrut(20));

        add(createSection12());
    }

    private JPanel createSection1(ProjectSummaryDTO data) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.decode("#1B3A6B")); // Navy
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel name = new JLabel(projectInfo != null ? projectInfo.getProjectName() : data.getProjectName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 20));
        name.setForeground(Color.WHITE);
        
        JLabel status = new JLabel("  " + (projectInfo != null ? projectInfo.getStatus() : data.getStatus()) + "  ");
        status.setOpaque(true);
        if ("ONGOING".equalsIgnoreCase(data.getStatus())) status.setBackground(Color.decode("#4CAF50"));
        else status.setBackground(Color.decode("#FFC107"));
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(false);
        top.add(name);
        top.add(status);
        
        p.add(top);
        return p;
    }

    private JPanel createSection2(ProjectSummaryDTO data) {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 0));
        panel.setOpaque(false);

        panel.add(new KpiCard("Total Investment", currencyFormat.format(controller.getTotalExpenditureByMonth(currentProjectId, 120) != null ? controller.getTotalExpenditureByMonth(currentProjectId, 120) : BigDecimal.ZERO), "", 4, "💰", null));
        panel.add(new KpiCard("Material Cost", currencyFormat.format(data.getTotalMaterialCost() != null ? data.getTotalMaterialCost() : BigDecimal.ZERO), "", 3, "📦", null));
        panel.add(new KpiCard("Labor Cost", currencyFormat.format(data.getTotalLaborCost() != null ? data.getTotalLaborCost() : BigDecimal.ZERO), "", 5, "👷", null));
        
        List<WorkerAttendanceDTO> todayAtt = controller.getTodayAttendance(currentProjectId);
        long present = todayAtt.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getAttendanceStatus())).count();
        panel.add(new KpiCard("Workers Today", present + " / " + todayAtt.size(), "On Site", 2, "👥", null));
        
        long days = 0;
        if (projectInfo != null && projectInfo.getStartDate() != null) {
            days = java.time.temporal.ChronoUnit.DAYS.between(projectInfo.getStartDate(), LocalDate.now());
            if (days < 0) days = 0;
        }
        panel.add(new KpiCard("Days Elapsed", String.valueOf(days), "Since start", 0, "📅", null));
        return panel;
    }

    private JPanel createSection5() {
        Map<String, Number> usage = new HashMap<>();
        List<MaterialUsageDTO> allUsages = materialController.getUsageByProject(currentProjectId);
        if (allUsages != null) {
            for (MaterialUsageDTO u : allUsages) {
                String mat = u.getMaterialName();
                if (mat == null || mat.isEmpty()) mat = "Unknown";
                double qty = u.getQuantityUsed() != null ? u.getQuantityUsed().doubleValue() : 0;
                double current = usage.containsKey(mat) ? usage.get(mat).doubleValue() : 0;
                usage.put(mat, current + qty);
            }
        }
        
        if (usage.isEmpty()) {
            usage.put("No Data", 1);
        }
        
        Map<String, Color> colors = new HashMap<>();
        Color[] palette = {Color.decode("#FF9800"), Color.decode("#607D8B"), Color.decode("#F44336"), Color.decode("#FFC107"), Color.decode("#4CAF50"), Color.decode("#9C27B0")};
        int i = 0;
        for (String k : usage.keySet()) {
            colors.put(k, palette[i % palette.length]);
            i++;
        }
        
        JPanel chart = ChartUtil.createPieChart2D("Material Usage (Total Units)", usage, colors);
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection10(ProjectSummaryDTO data) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        JLabel label = new JLabel("Current Stock Levels");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Material", "Available", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        List<ProjectMaterialStockDTO> stockList = stockController.getStockByProject(currentProjectId);
        if (stockList != null) {
            for (ProjectMaterialStockDTO s : stockList) {
                String status = "OK";
                if (s.getQuantityAvailable() != null && s.getMinimumQuantity() != null) {
                    if (s.getQuantityAvailable().compareTo(s.getMinimumQuantity()) <= 0) {
                        status = "LOW";
                    }
                }
                model.addRow(new Object[]{s.getMaterialName(), s.getQuantityAvailable() + " " + s.getUnit(), status});
            }
        }
        
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"No stock found", "-", "-"});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSection12() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        
        JButton btn1 = new JButton("📝 Log Usage");
        btn1.addActionListener(e -> new view.materials.UsageFormDialog(mainFrame, materialController).setVisible(true)); 
        
        panel.add(btn1);
        
        return panel;
    }
}
