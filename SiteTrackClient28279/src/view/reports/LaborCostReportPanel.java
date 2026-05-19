package view.reports;

import controller.ProjectController;
import controller.ReportController;
import dto.LaborCostReportDTO;
import dto.ProjectDTO;
import dto.WorkerPaymentDTO;
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

public class LaborCostReportPanel extends JPanel {
    private ReportController reportController;
    private ProjectController projectController;

    private JComboBox<String> projectCombo;
    private JComboBox<String> statusCombo;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    private JButton generateBtn;
    
    private JButton exportCSVBtn;
    private JButton exportExcelBtn;
    private JButton exportPDFBtn;

    private JLabel reportHeaderLabel;
    private KpiCard totalWorkersCard;
    private KpiCard totalWorkDaysCard;
    private KpiCard amountOwedCard;
    private KpiCard amountPaidCard;
    private KpiCard amountPendingCard;

    private JTable table;
    private DefaultTableModel tableModel;

    private LaborCostReportDTO currentReport;
    private List<WorkerPaymentDTO> filteredPayments;

    public LaborCostReportPanel() {
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

        statusCombo = new JComboBox<>(new String[]{"All", "PENDING", "PAID"});

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
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusCombo);
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

        reportHeaderLabel = new JLabel("Labor Cost Report");
        reportHeaderLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        reportHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableTab.add(reportHeaderLabel, BorderLayout.NORTH);

        JPanel kpiPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        totalWorkersCard = new KpiCard("Total Workers", "0", "Distinct engaged", 0);
        totalWorkDaysCard = new KpiCard("Total Work Days", "0", "Shifts worked", 1);
        amountOwedCard = new KpiCard("Total Owed", "RWF 0.00", "Total accrued cost", 2);
        amountPaidCard = new KpiCard("Total Paid", "RWF 0.00", "Total disbursed", 2);
        amountPendingCard = new KpiCard("Amount Pending", "RWF 0.00", "Unpaid wages", 3);

        kpiPanel.add(totalWorkersCard);
        kpiPanel.add(totalWorkDaysCard);
        kpiPanel.add(amountOwedCard);
        kpiPanel.add(amountPaidCard);
        kpiPanel.add(amountPendingCard);

        JPanel centerContent = new JPanel(new BorderLayout(0, 10));
        centerContent.setOpaque(false);
        centerContent.add(kpiPanel, BorderLayout.NORTH);

        String[] columns = {"Date", "Worker Name", "Daily Rate", "Amount Owed", "Amount Paid", "Status", "Paid By"};
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
                if (column == 5 && value != null) { // Status
                    setHorizontalAlignment(JLabel.CENTER);
                    String stat = value.toString();
                    if (stat.equals("PAID")) {
                        c.setBackground(new Color(230, 245, 230));
                        c.setForeground(Color.decode("#27ae60"));
                    } else if (stat.equals("PENDING")) {
                        c.setBackground(new Color(250, 240, 220));
                        c.setForeground(Color.decode("#e67e22"));
                    }
                } else if (column >= 2 && column <= 4) { // Currency columns
                    setHorizontalAlignment(JLabel.RIGHT);
                    if (value != null) {
                        try {
                            setText(format.format(new BigDecimal(value.toString())));
                        } catch (Exception ignored) {}
                    }
                    if (column == 3 || column == 4) setFont(getFont().deriveFont(Font.BOLD));
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

        tabbedPane.addTab("Payment History", tableTab);
        add(tabbedPane, BorderLayout.CENTER);

        // --- Actions ---
        generateBtn.addActionListener(e -> generateReport());
        statusCombo.addActionListener(e -> {
            if (currentReport != null) applyStatusFilter();
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

        SwingWorker<LaborCostReportDTO, Void> worker = new SwingWorker<LaborCostReportDTO, Void>() {
            @Override
            protected LaborCostReportDTO doInBackground() {
                return reportController.getLaborCostReport(projId, from, to);
            }

            @Override
            protected void done() {
                try {
                    currentReport = get();
                    if (currentReport != null) {
                        applyStatusFilter();
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

    private void applyStatusFilter() {
        String statusFilter = (String) statusCombo.getSelectedItem();
        filteredPayments = new ArrayList<>();
        
        for (WorkerPaymentDTO pay : currentReport.getPaymentRecords()) {
            if ("All".equals(statusFilter) || statusFilter.equals(pay.getPaymentStatus())) {
                filteredPayments.add(pay);
            }
        }

        updateKPIs();
        updateTable();
    }

    private void updateKPIs() {
        NumberFormat currFmt = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
        
        long totalWorkers = filteredPayments.stream().map(WorkerPaymentDTO::getWorkerId).distinct().count();
        long totalDays = filteredPayments.size();
        
        BigDecimal totalOwed = filteredPayments.stream()
            .map(p -> p.getAmountOwed() != null ? p.getAmountOwed() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalPaid = filteredPayments.stream()
            .map(p -> p.getAmountPaid() != null ? p.getAmountPaid() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalPending = totalOwed.subtract(totalPaid);

        totalWorkersCard.setValue(String.valueOf(totalWorkers));
        totalWorkDaysCard.setValue(String.valueOf(totalDays));
        amountOwedCard.setValue(currFmt.format(totalOwed));
        amountPaidCard.setValue(currFmt.format(totalPaid));
        amountPendingCard.setValue(currFmt.format(totalPending));
    }

    private void updateTable() {
        String projName = ((String) projectCombo.getSelectedItem()).split(" - ")[1];
        String statusFilter = (String) statusCombo.getSelectedItem();
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        LocalDate from = fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        reportHeaderLabel.setText("Labor Cost: " + projName + " (" + statusFilter + ") (" + from.format(fmt) + " to " + to.format(fmt) + ")");

        tableModel.setRowCount(0);
        for (WorkerPaymentDTO pay : filteredPayments) {
            tableModel.addRow(new Object[]{
                pay.getWorkDate(),
                pay.getWorkerFullName(),
                pay.getDailyRate(),
                pay.getAmountOwed(),
                pay.getAmountPaid(),
                pay.getPaymentStatus(),
                pay.getPaidByName() != null ? pay.getPaidByName() : ""
            });
        }
    }

    private void exportReport(String format) {
        if (filteredPayments == null) return;
        
        String path = reportController.openFileDialog(null);
        if (path == null) return;

        String selectedProj = (String) projectCombo.getSelectedItem();
        String projName = selectedProj.split(" - ")[1].replaceAll("[^a-zA-Z0-9]", "_");
        String statusFilter = (String) statusCombo.getSelectedItem();
        String reportName = "LaborCost_" + projName + "_" + statusFilter;

        List<?> data = new ArrayList<>(filteredPayments);

        if ("CSV".equals(format)) {
            reportController.exportToCSV(data, reportName, path);
        } else if ("EXCEL".equals(format)) {
            reportController.exportToExcel(data, reportName, path);
        } else if ("PDF".equals(format)) {
            reportController.exportToPDF(data, reportName, path, "Labor Cost Report (" + statusFilter + ")", "Project: " + projName);
        }
    }
}
