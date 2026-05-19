package view.reports;

import controller.ProjectController;
import controller.ReportController;
import dto.ProjectDTO;
import dto.ProjectSummaryDTO;
import view.dashboard.KpiCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjectSummaryReportPanel extends JPanel {
    private ReportController reportController;
    private ProjectController projectController;

    private JComboBox<String> projectCombo;
    private JButton generateBtn;
    
    private JButton exportCSVBtn;
    private JButton exportExcelBtn;
    private JButton exportPDFBtn;

    private JPanel reportContainer;
    
    // Overview components
    private JLabel projNameLabel;
    private JLabel statusLabel;
    private JLabel dateLabel;
    
    // KPI Cards
    private KpiCard materialCostCard;
    private KpiCard laborCostCard;
    private KpiCard totalCostCard;
    private KpiCard workforceCard;

    private ProjectSummaryDTO currentSummary;
    private ProjectDTO currentProject;

    public ProjectSummaryReportPanel() {
        this.reportController = new ReportController();
        this.projectController = new ProjectController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        initUI();
    }

    private void initUI() {
        // --- North Project Selector ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(UIManager.getColor("Panel.background"));

        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }

        generateBtn = new JButton("Generate Report");
        generateBtn.setBackground(Color.decode("#3498db"));
        generateBtn.setForeground(Color.WHITE);

        topPanel.add(new JLabel("Project:"));
        topPanel.add(projectCombo);
        topPanel.add(generateBtn);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Scrollable Report ---
        reportContainer = new JPanel();
        reportContainer.setLayout(new BoxLayout(reportContainer, BoxLayout.Y_AXIS));
        reportContainer.setBackground(UIManager.getColor("Panel.background"));
        reportContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(reportContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- Report Sections ---
        createOverviewSection();
        createFinancialSection();

        // --- South Export Bar ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(UIManager.getColor("Panel.background"));

        exportCSVBtn = new JButton("📋 Export as CSV (Stats)");
        exportExcelBtn = new JButton("📊 Export as Excel (Detailed)");
        exportPDFBtn = new JButton("📄 Export as PDF (Full Report)");
        
        exportCSVBtn.setEnabled(false);
        exportExcelBtn.setEnabled(false);
        exportPDFBtn.setEnabled(false);

        bottomPanel.add(exportCSVBtn);
        bottomPanel.add(exportExcelBtn);
        bottomPanel.add(exportPDFBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Actions ---
        generateBtn.addActionListener(e -> generateReport());
        exportCSVBtn.addActionListener(e -> exportReport("CSV"));
        exportExcelBtn.addActionListener(e -> exportReport("EXCEL"));
        exportPDFBtn.addActionListener(e -> exportReport("PDF"));
    }

    private void createOverviewSection() {
        JPanel overviewPanel = new JPanel(new BorderLayout(10, 10));
        overviewPanel.setOpaque(false);
        overviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        projNameLabel = new JLabel("Select a project");
        projNameLabel.setFont(new Font("Ubuntu", Font.BOLD, 28));
        projNameLabel.setForeground(Color.decode("#1B3A6B"));

        statusLabel = new JLabel("STATUS");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("Ubuntu", Font.BOLD, 12));

        titlePanel.add(projNameLabel);
        titlePanel.add(Box.createHorizontalStrut(10));
        titlePanel.add(statusLabel);

        dateLabel = new JLabel("Duration: - to -");
        dateLabel.setFont(new Font("Ubuntu", Font.PLAIN, 14));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(titlePanel);
        leftPanel.add(dateLabel);

        overviewPanel.add(leftPanel, BorderLayout.WEST);
        
        reportContainer.add(overviewPanel);
        reportContainer.add(Box.createVerticalStrut(20));
        reportContainer.add(new JSeparator());
        reportContainer.add(Box.createVerticalStrut(20));
    }

    private void createFinancialSection() {
        JLabel sectionTitle = new JLabel("Summary Indicators");
        sectionTitle.setFont(new Font("Ubuntu", Font.BOLD, 18));
        sectionTitle.setForeground(Color.decode("#d35400")); // Gold/Orange
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(sectionTitle);

        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        materialCostCard = new KpiCard("Material Cost", "RWF 0.00", "Total materials");
        laborCostCard = new KpiCard("Labor Cost", "RWF 0.00", "Total labor");
        totalCostCard = new KpiCard("Total Expenditure", "RWF 0.00", "Overall cost");
        workforceCard = new KpiCard("Workforce", "0", "Total workers");

        kpiPanel.add(materialCostCard);
        kpiPanel.add(laborCostCard);
        kpiPanel.add(totalCostCard);
        kpiPanel.add(workforceCard);

        reportContainer.add(titlePanel);
        reportContainer.add(kpiPanel);
    }

    private void generateReport() {
        if (projectCombo.getSelectedItem() == null) return;
        String selectedProj = (String) projectCombo.getSelectedItem();
        String projId = selectedProj.split(" - ")[0];

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        generateBtn.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                currentProject = projectController.getAllProjects().stream()
                        .filter(p -> p.getId().equals(projId)).findFirst().orElse(null);
                currentSummary = reportController.getProjectSummary(projId);
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (currentSummary != null && currentProject != null) {
                        refreshUI();
                        exportCSVBtn.setEnabled(true);
                        exportExcelBtn.setEnabled(true);
                        exportPDFBtn.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(ProjectSummaryReportPanel.this, "Could not load summary for project.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                    generateBtn.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void refreshUI() {
        projNameLabel.setText(currentSummary.getProjectName());
        statusLabel.setText(currentSummary.getStatus());
        
        if ("COMPLETED".equals(currentSummary.getStatus())) {
            statusLabel.setBackground(new Color(230, 245, 230));
            statusLabel.setForeground(Color.decode("#27ae60"));
        } else if ("IN_PROGRESS".equals(currentSummary.getStatus())) {
            statusLabel.setBackground(new Color(230, 240, 250));
            statusLabel.setForeground(Color.decode("#2980b9"));
        } else {
            statusLabel.setBackground(Color.LIGHT_GRAY);
            statusLabel.setForeground(Color.DARK_GRAY);
        }

        LocalDate start = currentProject.getStartDate();
        LocalDate end = currentProject.getExpectedEndDate();
        long totalDays = start != null && end != null ? ChronoUnit.DAYS.between(start, end) : 0;
        long elapsedDays = start != null ? ChronoUnit.DAYS.between(start, LocalDate.now()) : 0;
        
        dateLabel.setText(String.format("Location: %s | Duration: %s to %s | %d/%d days elapsed",
                currentProject.getLocation(), start, end, elapsedDays, totalDays));

        NumberFormat currFmt = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
        materialCostCard.setValue(currFmt.format(currentSummary.getTotalMaterialCost()));
        laborCostCard.setValue(currFmt.format(currentSummary.getTotalLaborCost()));
        totalCostCard.setValue(currFmt.format(currentSummary.getTotalExpenditure()));
        workforceCard.setValue(String.valueOf(currentSummary.getTotalWorkers()));
    }

    private void exportReport(String format) {
        if (currentSummary == null) return;
        
        String path = reportController.openFileDialog(null);
        if (path == null) return;

        String projName = currentSummary.getProjectName().replaceAll("[^a-zA-Z0-9]", "_");
        String reportName = "ProjectSummary_" + projName;

        List<ProjectSummaryDTO> data = new ArrayList<>();
        data.add(currentSummary);

        if ("CSV".equals(format)) {
            reportController.exportToCSV(data, reportName, path);
        } else if ("EXCEL".equals(format)) {
            reportController.exportToExcel(data, reportName, path);
        } else if ("PDF".equals(format)) {
            reportController.exportToPDF(data, reportName, path, "Project Summary", "Project: " + currentSummary.getProjectName());
        }
    }
}
