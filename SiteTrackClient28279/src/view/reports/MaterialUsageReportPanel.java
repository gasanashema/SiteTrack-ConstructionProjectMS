package view.reports;

import controller.ProjectController;
import controller.ReportController;
import dto.MaterialUsageDTO;
import dto.MaterialUsageReportDTO;
import dto.ProjectDTO;
import view.dashboard.KpiCard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaterialUsageReportPanel extends JPanel {
    private ReportController reportController;
    private ProjectController projectController;

    private JComboBox<String> projectCombo;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    private JButton generateBtn;
    
    private JButton exportCSVBtn;
    private JButton exportExcelBtn;
    private JButton exportPDFBtn;

    private JLabel reportHeaderLabel;
    private KpiCard totalMaterialsCard;
    private KpiCard totalQuantityCard;
    private KpiCard totalCostCard;
    private KpiCard avgCostCard;

    private JTable table;
    private DefaultTableModel tableModel;

    private MaterialUsageReportDTO currentReport;

    public MaterialUsageReportPanel() {
        this.reportController = new ReportController();
        this.projectController = new ProjectController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        initUI();
    }

    private void initUI() {
        // --- North Filter Bar ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(UIManager.getColor("Panel.background"));

        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }

        fromDateField = new com.toedter.calendar.JDateChooser();
        toDateField = new com.toedter.calendar.JDateChooser();

        generateBtn = new JButton("Generate Report");
        generateBtn.setBackground(Color.decode("#3498db"));
        generateBtn.setForeground(Color.WHITE);

        exportCSVBtn = new JButton("📥 CSV");
        exportExcelBtn = new JButton("📥 Excel");
        exportPDFBtn = new JButton("📥 PDF");
        
        exportCSVBtn.setEnabled(false);
        exportExcelBtn.setEnabled(false);
        exportPDFBtn.setEnabled(false);

        filterPanel.add(new JLabel("Project:"));
        filterPanel.add(projectCombo);
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDateField);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDateField);
        filterPanel.add(generateBtn);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(exportCSVBtn);
        filterPanel.add(exportExcelBtn);
        filterPanel.add(exportPDFBtn);

        add(filterPanel, BorderLayout.NORTH);

        // --- Center TabbedPane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 14));

        JPanel tableTab = new JPanel(new BorderLayout(10, 10));
        tableTab.setBackground(UIManager.getColor("Panel.background"));
        tableTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        reportHeaderLabel = new JLabel("Material Usage Report");
        reportHeaderLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        reportHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableTab.add(reportHeaderLabel, BorderLayout.NORTH);

        // KPI Cards
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        totalMaterialsCard = new KpiCard("Unique Materials", "0", "Items used", 0);
        totalQuantityCard = new KpiCard("Total Quantity", "0", "Units consumed", 1);
        totalCostCard = new KpiCard("Total Cost", "RWF 0.00", "Overall expenditure", 2);
        avgCostCard = new KpiCard("Avg Unit Cost", "RWF 0.00", "Cost per unit", 3);

        kpiPanel.add(totalMaterialsCard);
        kpiPanel.add(totalQuantityCard);
        kpiPanel.add(totalCostCard);
        kpiPanel.add(avgCostCard);

        JPanel centerContent = new JPanel(new BorderLayout(0, 10));
        centerContent.setOpaque(false);
        centerContent.add(kpiPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Date", "Material Name", "Unit", "Qty Used", "Unit Price", "Total Cost", "Recorded By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 4 || column == 5) {
                    setHorizontalAlignment(JLabel.RIGHT);
                    if (value != null) {
                        try {
                            setText(format.format(new BigDecimal(value.toString())));
                        } catch (Exception ignored) {}
                    }
                    if (column == 5) setFont(getFont().deriveFont(Font.BOLD));
                } else if (column == 3) {
                    setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }
                return c;
            }
        });

        centerContent.add(new JScrollPane(table), BorderLayout.CENTER);
        tableTab.add(centerContent, BorderLayout.CENTER);

        tabbedPane.addTab("Report Data", tableTab);
        add(tabbedPane, BorderLayout.CENTER);

        // --- Actions ---
        generateBtn.addActionListener(e -> generateReport());
        
        exportCSVBtn.addActionListener(e -> exportReport("CSV"));
        exportExcelBtn.addActionListener(e -> exportReport("EXCEL"));
        exportPDFBtn.addActionListener(e -> exportReport("PDF"));
    }

    private void generateReport() {
        if (projectCombo.getSelectedItem() == null) return;
        String selectedProj = (String) projectCombo.getSelectedItem();
        String projId = selectedProj.split(" - ")[0];
        String projName = selectedProj.split(" - ")[1];

        LocalDate from = fromDateField.getDate() != null ? fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate to = toDateField.getDate() != null ? toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Please select both From and To dates.");
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        generateBtn.setEnabled(false);

        SwingWorker<MaterialUsageReportDTO, Void> worker = new SwingWorker<MaterialUsageReportDTO, Void>() {
            @Override
            protected MaterialUsageReportDTO doInBackground() {
                return reportController.getMaterialUsageReport(projId, from, to);
            }

            @Override
            protected void done() {
                try {
                    currentReport = get();
                    if (currentReport != null) {
                        updateUI(projName, from, to);
                        exportCSVBtn.setEnabled(true);
                        exportExcelBtn.setEnabled(true);
                        exportPDFBtn.setEnabled(true);
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

    private void updateUI(String projName, LocalDate from, LocalDate to) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        reportHeaderLabel.setText("Material Usage Report: " + projName + " (" + from.format(fmt) + " to " + to.format(fmt) + ")");

        NumberFormat currFmt = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));

        // Unique materials count
        long uniqueMaterials = currentReport.getUsageRecords().stream()
                .map(MaterialUsageDTO::getMaterialId).distinct().count();

        totalMaterialsCard.setValue(String.valueOf(uniqueMaterials));
        
        BigDecimal totalQty = currentReport.getUsageRecords().stream()
                .map(MaterialUsageDTO::getQuantityUsed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalQuantityCard.setValue(totalQty.toString());
        
        BigDecimal totalCost = currentReport.getTotalCost();
        totalCostCard.setValue(currFmt.format(totalCost));

        BigDecimal avgCost = totalQty.compareTo(BigDecimal.ZERO) > 0 ? 
                totalCost.divide(totalQty, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        avgCostCard.setValue(currFmt.format(avgCost));

        tableModel.setRowCount(0);
        for (MaterialUsageDTO usage : currentReport.getUsageRecords()) {
            tableModel.addRow(new Object[]{
                usage.getUsageDate(),
                usage.getMaterialName(),
                usage.getUnit(),
                usage.getQuantityUsed(),
                usage.getUnitPrice(),
                usage.getTotalCost(),
                usage.getRecordedByName()
            });
        }
    }

    private void exportReport(String format) {
        if (currentReport == null) return;
        
        String path = reportController.openFileDialog(null);
        if (path == null) return;

        String selectedProj = (String) projectCombo.getSelectedItem();
        String projName = selectedProj.split(" - ")[1].replaceAll("[^a-zA-Z0-9]", "_");
        String reportName = "MaterialUsage_" + projName;

        // Note: For actual export, we might need a simplified list of objects, 
        // but ExportUtil uses reflection, so passing the DTO list works perfectly.
        List<?> data = new ArrayList<>(currentReport.getUsageRecords());

        if ("CSV".equals(format)) {
            reportController.exportToCSV(data, reportName, path);
        } else if ("EXCEL".equals(format)) {
            reportController.exportToExcel(data, reportName, path);
        } else if ("PDF".equals(format)) {
            reportController.exportToPDF(data, reportName, path, "Material Usage Report", "Project: " + projName);
        }
    }
}
