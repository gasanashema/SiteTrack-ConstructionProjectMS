package view.reports;

import controller.ProjectController;
import controller.ReportController;
import dto.MaterialStockMovementDTO;
import dto.ProjectDTO;
import dto.StockMovementReportDTO;
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
import java.util.Map;
import java.util.stream.Collectors;

public class StockMovementReportPanel extends JPanel {
    private ReportController reportController;
    private ProjectController projectController;

    private JComboBox<String> projectCombo;
    private JComboBox<String> typeCombo;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    private JButton generateBtn;
    
    private JButton exportCSVBtn;
    private JButton exportExcelBtn;
    private JButton exportPDFBtn;

    private JLabel reportHeaderLabel;
    private KpiCard totalInCard;
    private KpiCard totalOutCard;
    private KpiCard totalAdjCard;

    private JTable table;
    private DefaultTableModel tableModel;

    private StockMovementReportDTO currentReport;
    private List<MaterialStockMovementDTO> filteredMovements;

    public StockMovementReportPanel() {
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

        typeCombo = new JComboBox<>(new String[]{"All", "IN", "OUT", "ADJUSTMENT"});

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
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeCombo);
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDateField);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDateField);
        filterPanel.add(generateBtn);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(exportCSVBtn);
        filterPanel.add(exportExcelBtn);
        filterPanel.add(exportPDFBtn);

        add(filterPanel, BorderLayout.NORTH);

        // --- Center TabbedPane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 14));

        // Tab 1: Table
        JPanel tableTab = new JPanel(new BorderLayout(10, 10));
        tableTab.setBackground(UIManager.getColor("Panel.background"));
        tableTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        reportHeaderLabel = new JLabel("Stock Movement Report");
        reportHeaderLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        reportHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableTab.add(reportHeaderLabel, BorderLayout.NORTH);

        JPanel kpiPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        totalInCard = new KpiCard("IN Movements", "0", "Total additions", 2);
        totalOutCard = new KpiCard("OUT Movements", "0", "Total consumptions", 1);
        totalAdjCard = new KpiCard("Adjustments", "0", "Total manual corrections", 3);

        kpiPanel.add(totalInCard);
        kpiPanel.add(totalOutCard);
        kpiPanel.add(totalAdjCard);

        JPanel centerContent = new JPanel(new BorderLayout(0, 10));
        centerContent.setOpaque(false);
        centerContent.add(kpiPanel, BorderLayout.NORTH);

        String[] columns = {"Date", "Material Name", "Type", "Quantity", "Unit Price", "Total Price", "Reference", "Recorded By"};
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
                if (column == 2 && value != null) { // Movement Type
                    setHorizontalAlignment(JLabel.CENTER);
                    String type = value.toString();
                    if (type.equals("IN")) {
                        c.setBackground(new Color(230, 245, 230));
                        c.setForeground(Color.decode("#27ae60"));
                    } else if (type.equals("OUT")) {
                        c.setBackground(new Color(250, 230, 230));
                        c.setForeground(Color.decode("#c0392b"));
                    } else {
                        c.setBackground(new Color(250, 240, 220));
                        c.setForeground(Color.decode("#e67e22"));
                    }
                } else if (column == 4 || column == 5) {
                    setHorizontalAlignment(JLabel.RIGHT);
                    if (value != null) {
                        try {
                            setText(format.format(new BigDecimal(value.toString())));
                        } catch (Exception ignored) {}
                    }
                    if (column == 5) setFont(getFont().deriveFont(Font.BOLD));
                    if (!isSelected) { c.setBackground(Color.WHITE); c.setForeground(Color.BLACK); }
                } else if (column == 3) {
                    setHorizontalAlignment(JLabel.RIGHT);
                    if (!isSelected) { c.setBackground(Color.WHITE); c.setForeground(Color.BLACK); }
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                    if (!isSelected) { c.setBackground(Color.WHITE); c.setForeground(Color.BLACK); }
                }
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                return c;
            }
        });

        centerContent.add(new JScrollPane(table), BorderLayout.CENTER);
        tableTab.add(centerContent, BorderLayout.CENTER);

        tabbedPane.addTab("Movement History", tableTab);
        add(tabbedPane, BorderLayout.CENTER);

        // --- Actions ---
        generateBtn.addActionListener(e -> generateReport());
        typeCombo.addActionListener(e -> {
            if (currentReport != null) applyTypeFilter();
        });
        
        exportCSVBtn.addActionListener(e -> exportReport("CSV"));
        exportExcelBtn.addActionListener(e -> exportReport("EXCEL"));
        exportPDFBtn.addActionListener(e -> exportReport("PDF"));
    }

    private void generateReport() {
        if (projectCombo.getSelectedItem() == null) return;
        String selectedProj = (String) projectCombo.getSelectedItem();
        String projId = selectedProj.split(" - ")[0];

        LocalDate from = fromDateField.getDate() != null ? fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate to = toDateField.getDate() != null ? toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Please select both From and To dates.");
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        generateBtn.setEnabled(false);

        SwingWorker<StockMovementReportDTO, Void> worker = new SwingWorker<StockMovementReportDTO, Void>() {
            @Override
            protected StockMovementReportDTO doInBackground() {
                return reportController.getStockMovementReport(projId, from, to);
            }

            @Override
            protected void done() {
                try {
                    currentReport = get();
                    if (currentReport != null) {
                        applyTypeFilter();
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

    private void applyTypeFilter() {
        String typeFilter = (String) typeCombo.getSelectedItem();
        filteredMovements = new ArrayList<>();
        
        long countIn = 0, countOut = 0, countAdj = 0;

        for (MaterialStockMovementDTO mov : currentReport.getMovements()) {
            // Compute KPI counts based on unfiltered dataset to show total period stats
            if ("IN".equals(mov.getMovementType())) countIn++;
            else if ("OUT".equals(mov.getMovementType())) countOut++;
            else if ("ADJUSTMENT".equals(mov.getMovementType())) countAdj++;

            if ("All".equals(typeFilter) || typeFilter.equals(mov.getMovementType())) {
                filteredMovements.add(mov);
            }
        }

        totalInCard.setValue(String.valueOf(countIn));
        totalOutCard.setValue(String.valueOf(countOut));
        totalAdjCard.setValue(String.valueOf(countAdj));

        updateTable();
    }

    private void updateTable() {
        String projName = ((String) projectCombo.getSelectedItem()).split(" - ")[1];
        String typeFilter = (String) typeCombo.getSelectedItem();
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        LocalDate from = fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        reportHeaderLabel.setText("Stock Movement: " + projName + " (" + typeFilter + ") (" + from.format(fmt) + " to " + to.format(fmt) + ")");

        tableModel.setRowCount(0);
        for (MaterialStockMovementDTO mov : filteredMovements) {
            tableModel.addRow(new Object[]{
                mov.getMovementDate(),
                mov.getMaterialName(),
                mov.getMovementType(),
                mov.getQuantity(),
                mov.getUnitPrice(),
                mov.getTotalPrice(),
                mov.getReferenceType() != null ? (mov.getReferenceType() + (mov.getReferenceId() != null ? " - " + mov.getReferenceId() : "")) : "",
                mov.getRecordedByName()
            });
        }
    }

    private void exportReport(String format) {
        if (filteredMovements == null) return;
        
        String path = reportController.openFileDialog(null);
        if (path == null) return;

        String selectedProj = (String) projectCombo.getSelectedItem();
        String projName = selectedProj.split(" - ")[1].replaceAll("[^a-zA-Z0-9]", "_");
        String typeFilter = (String) typeCombo.getSelectedItem();
        String reportName = "StockMovement_" + projName + "_" + typeFilter;

        List<?> data = new ArrayList<>(filteredMovements);

        if ("CSV".equals(format)) {
            reportController.exportToCSV(data, reportName, path);
        } else if ("EXCEL".equals(format)) {
            reportController.exportToExcel(data, reportName, path);
        } else if ("PDF".equals(format)) {
            reportController.exportToPDF(data, reportName, path, "Stock Movement Report (" + typeFilter + ")", "Project: " + projName);
        }
    }
}
