package view.dashboard;

import controller.DashboardController;
import dto.ProjectSummaryDTO;
import dto.ProjectMaterialStockDTO;
import dto.WorkerAttendanceDTO;
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
    private MainFrame mainFrame;
    private String currentProjectId;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));

    public SiteManagerDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = DashboardController.getInstance();
        
        // Find assigned project (For demo, we assume the site manager has a project assigned, or we fetch the first one)
        // Ideally we get this from SessionManager
        List<dto.ProjectDTO> projects = controller.getRecentProjects(1);
        if (projects != null && !projects.isEmpty()) {
            this.currentProjectId = projects.get(0).getId();
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

        // 1. Project Header
        add(createSection1(data));
        add(Box.createVerticalStrut(20));

        // 2. Key Project KPIs
        add(createSection2(data));
        add(Box.createVerticalStrut(20));

        // 3. Today's Overview
        add(createSection3(data));
        add(Box.createVerticalStrut(20));

        // 4. Daily Activity (Line Chart)
        add(createSection4());
        add(Box.createVerticalStrut(20));

        // 5 & 6. Side by Side: Material Usage (Pie) & Budget Status (Gauge)
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setOpaque(false);
        row2.setMaximumSize(new Dimension(1200, 300));
        row2.add(createSection5());
        row2.add(createSection6());
        add(row2);
        add(Box.createVerticalStrut(20));

        // 7 & 8. Side by Side: Daily Material Consumption & Labor Cost
        JPanel row3 = new JPanel(new GridLayout(1, 2, 20, 0));
        row3.setOpaque(false);
        row3.setMaximumSize(new Dimension(1200, 300));
        row3.add(createSection7());
        row3.add(createSection8());
        add(row3);
        add(Box.createVerticalStrut(20));

        // 9 & 10. Tables: Timeline & Stock Status
        JPanel row4 = new JPanel(new GridLayout(1, 2, 20, 0));
        row4.setOpaque(false);
        row4.setMaximumSize(new Dimension(1200, 250));
        row4.add(createSection9());
        row4.add(createSection10(data));
        add(row4);
        add(Box.createVerticalStrut(20));

        // 11. Upcoming Events
        add(createSection11());
        add(Box.createVerticalStrut(20));

        // 12. Quick Actions
        add(createSection12());
    }

    private JPanel createSection1(ProjectSummaryDTO data) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.decode("#1B3A6B")); // Navy
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        p.setMaximumSize(new Dimension(1200, 80));
        
        JLabel name = new JLabel(data.getProjectName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 20));
        name.setForeground(Color.WHITE);
        
        JLabel status = new JLabel("  " + data.getStatus() + "  ");
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
        panel.setMaximumSize(new Dimension(1200, 100));

        panel.add(new KpiCard("Total Investment", currencyFormat.format(controller.getTotalExpenditureByMonth(currentProjectId, 120)), "", 4, "💰", null));
        panel.add(new KpiCard("Material Cost", currencyFormat.format(data.getTotalMaterialCost()), "", 3, "📦", null));
        panel.add(new KpiCard("Labor Cost", currencyFormat.format(data.getTotalLaborCost()), "", 5, "👷", null));
        
        List<WorkerAttendanceDTO> todayAtt = controller.getTodayAttendance(currentProjectId);
        long present = todayAtt.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getAttendanceStatus())).count();
        panel.add(new KpiCard("Workers Today", present + " / " + todayAtt.size(), "On Site", 2, "👥", null));
        
        panel.add(new KpiCard("Days Elapsed", "45", "Since start", 0, "📅", null)); // Mocked
        return panel;
    }

    private JPanel createSection3(ProjectSummaryDTO data) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1200, 100));

        panel.add(createInfoBox("Today's Attendance", "85% Present", "45 / 53 Total"));
        panel.add(createInfoBox("Today's Material Usage", "12 Items Used", currencyFormat.format(new BigDecimal("150000"))));
        
        JPanel pBox = createInfoBox("Pending Payments", "8 Workers", currencyFormat.format(new BigDecimal("40000")));
        JButton payBtn = new JButton("Process Payments");
        payBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame, "Please navigate to the Payroll module from the sidebar to process payments.", "Process Payments", JOptionPane.INFORMATION_MESSAGE);
        });
        pBox.add(payBtn, BorderLayout.SOUTH);
        panel.add(pBox);

        return panel;
    }

    private JPanel createInfoBox(String title, String val1, String val2) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(UIManager.getColor("Panel.background"));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#2196F3")),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(tLabel, BorderLayout.NORTH);
        
        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setOpaque(false);
        center.add(new JLabel(val1));
        center.add(new JLabel(val2));
        p.add(center, BorderLayout.CENTER);
        
        return p;
    }

    private JPanel createSection4() {
        Map<LocalDate, Integer> trend = controller.getAttendanceTrendByDate(currentProjectId, 30);
        Map<String, List<Number>> series = new HashMap<>();
        List<Number> vals = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        
        for (Map.Entry<LocalDate, Integer> e : trend.entrySet()) {
            vals.add(e.getValue());
            labels.add(e.getKey().format(dtf));
        }
        series.put("Present Workers", vals);
        
        Map<String, Color> colors = new HashMap<>();
        colors.put("Present Workers", Color.decode("#2196F3"));
        
        JPanel chart = ChartUtil.createLineChart2D("Attendance Trend (Last 30 Days)", series, colors, labels);
        chart.setPreferredSize(new Dimension(800, 200));
        chart.setMaximumSize(new Dimension(1200, 200));
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection5() {
        Map<String, Number> usage = new HashMap<>();
        usage.put("Cement", 500);
        usage.put("Steel", 250);
        usage.put("Bricks", 1200);
        usage.put("Sand", 300);
        
        Map<String, Color> colors = new HashMap<>();
        colors.put("Cement", Color.decode("#FF9800"));
        colors.put("Steel", Color.decode("#607D8B"));
        colors.put("Bricks", Color.decode("#F44336"));
        colors.put("Sand", Color.decode("#FFC107"));
        
        JPanel chart = ChartUtil.createPieChart2D("Material Usage (Units)", usage, colors);
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection6() {
        Map<String, BigDecimal> util = controller.getProjectBudgetUtilization(currentProjectId);
        double pct = util.getOrDefault("Percentage", BigDecimal.ZERO).doubleValue();
        String footer = "Spent: " + currencyFormat.format(util.get("Used")) + " | Rem: " + currencyFormat.format(util.get("Remaining"));
        
        JPanel chart = ChartUtil.createGaugeChart2D("Budget Utilization", pct, footer);
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection7() {
        Map<String, Number> map = new LinkedHashMap<>();
        map.put("Cement (Bags)", 45);
        map.put("Steel (Bars)", 30);
        map.put("Sand (Trucks)", 2);
        
        JPanel chart = ChartUtil.createHorizontalBarChart2D("Material Usage by Type", map, Color.decode("#FF9800"), false);
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection8() {
        Map<String, Number> map = new LinkedHashMap<>();
        map.put("Masons", 45000);
        map.put("Helpers", 20000);
        map.put("Electricians", 15000);
        
        JPanel chart = ChartUtil.createHorizontalBarChart2D("Labor Cost by Type", map, Color.decode("#009688"), true);
        chart.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        return chart;
    }

    private JPanel createSection9() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        JLabel label = new JLabel("Project Timeline (Last 10 Days)");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Date", "Activity", "Progress", "Recorded By"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        model.addRow(new Object[]{"Today", "Foundation laying", "↑ 15%", "Site Admin"});
        model.addRow(new Object[]{"Yesterday", "Site clearance", "↑ 100%", "Site Admin"});

        JTable table = new JTable(model);
        table.setRowHeight(30);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSection10(ProjectSummaryDTO data) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        JLabel label = new JLabel("Current Stock Levels");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        String[] cols = {"Material", "Available", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        // Just mocking for now, should ideally fetch material stocks for this project
        model.addRow(new Object[]{"Cement", "50 Bags", "LOW"});
        model.addRow(new Object[]{"Steel", "200 Bars", "OK"});

        JTable table = new JTable(model);
        table.setRowHeight(30);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSection11() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setMaximumSize(new Dimension(1200, 100));
        JLabel label = new JLabel("Upcoming Milestones");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);
        
        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        cards.setOpaque(false);
        
        JPanel card1 = new JPanel();
        card1.setBackground(Color.decode("#FFF3E0"));
        card1.add(new JLabel("Roofing Start - Next Week"));
        cards.add(card1);
        
        panel.add(cards, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSection12() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1200, 50));
        
        JButton btn1 = new JButton("📝 Log Usage");
        btn1.addActionListener(e -> new view.materials.UsageFormDialog(mainFrame, new controller.MaterialController()).setVisible(true)); 
        
        JButton btn2 = new JButton("✓ Record Attendance");
        JButton btn3 = new JButton("💳 Process Payment");
        JButton btn4 = new JButton("📊 Generate Report");
        
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);
        
        return panel;
    }
}
