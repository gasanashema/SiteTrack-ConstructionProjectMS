package view.reports;

import controller.PayrollController;
import controller.ProjectController;
import controller.ReportController;
import dto.ProjectDTO;
import dto.WorkerAttendanceDTO;
import view.dashboard.KpiCard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceReportPanel extends JPanel {
    private ReportController reportController;
    private PayrollController payrollController;
    private ProjectController projectController;

    private JComboBox<String> projectCombo;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    private JButton generateBtn;
    
    private JButton exportCSVBtn;
    private JButton exportExcelBtn;
    private JButton exportPDFBtn;

    private JLabel reportHeaderLabel;
    
    private KpiCard totalWorkDaysCard;
    private KpiCard totalPresentCard;
    private KpiCard totalAbsentCard;
    private KpiCard attendanceRateCard;
    private KpiCard uniqueWorkersCard;
    private KpiCard avgDailyWorkersCard;

    private JTable table;
    private DefaultTableModel tableModel;

    private List<WorkerAttendanceDTO> currentReport;

    public AttendanceReportPanel() {
        this.reportController = new ReportController();
        this.payrollController = new PayrollController();
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

        reportHeaderLabel = new JLabel("Attendance Report");
        reportHeaderLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        reportHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableTab.add(reportHeaderLabel, BorderLayout.NORTH);

        String[] columns = {"Date", "Worker Name", "Type", "Status", "Work Description", "Recorded By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 3 && value != null) { // Status
                    setHorizontalAlignment(JLabel.CENTER);
                    String stat = value.toString();
                    if (stat.equals("PRESENT")) {
                        c.setBackground(new Color(230, 245, 230));
                        c.setForeground(Color.decode("#27ae60"));
                    } else if (stat.equals("ABSENT")) {
                        c.setBackground(new Color(250, 230, 230));
                        c.setForeground(Color.decode("#c0392b"));
                    }
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

        tableTab.add(new JScrollPane(table), BorderLayout.CENTER);
        tabbedPane.addTab("Attendance History", tableTab);

        // Tab 2: Statistics
        JPanel statsTab = new JPanel(new BorderLayout(10, 10));
        statsTab.setBackground(UIManager.getColor("Panel.background"));
        statsTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel kpiPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        kpiPanel.setOpaque(false);

        totalWorkDaysCard = new KpiCard("Total Records", "0", "Total attendance entries", 0);
        totalPresentCard = new KpiCard("Total Present", "0", "Days marked present", 2);
        totalAbsentCard = new KpiCard("Total Absent", "0", "Days marked absent", 1);
        attendanceRateCard = new KpiCard("Attendance Rate", "0%", "Present / Total", 0);
        uniqueWorkersCard = new KpiCard("Unique Workers", "0", "Distinct workers", 3);
        avgDailyWorkersCard = new KpiCard("Avg Daily Workers", "0", "Present per day", 2);

        kpiPanel.add(totalWorkDaysCard);
        kpiPanel.add(totalPresentCard);
        kpiPanel.add(totalAbsentCard);
        kpiPanel.add(attendanceRateCard);
        kpiPanel.add(uniqueWorkersCard);
        kpiPanel.add(avgDailyWorkersCard);

        statsTab.add(kpiPanel, BorderLayout.NORTH);

        tabbedPane.addTab("Statistics", statsTab);
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

        LocalDate from = fromDateField.getDate() != null ? fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate to = toDateField.getDate() != null ? toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Please select both From and To dates.");
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        generateBtn.setEnabled(false);

        SwingWorker<List<WorkerAttendanceDTO>, Void> worker = new SwingWorker<List<WorkerAttendanceDTO>, Void>() {
            @Override
            protected List<WorkerAttendanceDTO> doInBackground() {
                return payrollController.getAttendanceByProjectAndDateRange(projId, from, to);
            }

            @Override
            protected void done() {
                try {
                    currentReport = get();
                    if (currentReport != null) {
                        refreshUI();
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

    private void refreshUI() {
        String projName = ((String) projectCombo.getSelectedItem()).split(" - ")[1];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        LocalDate from = fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        reportHeaderLabel.setText("Attendance Report: " + projName + " (" + from.format(fmt) + " to " + to.format(fmt) + ")");

        tableModel.setRowCount(0);
        long presentCount = 0;
        long absentCount = 0;
        
        for (WorkerAttendanceDTO att : currentReport) {
            tableModel.addRow(new Object[]{
                att.getWorkDate(),
                att.getWorkerFullName(),
                att.getWorkerTypeName() != null ? att.getWorkerTypeName() : "Unknown",
                att.getAttendanceStatus(),
                att.getWorkDescription() != null ? att.getWorkDescription() : "",
                att.getRecordedByName()
            });

            if ("PRESENT".equals(att.getAttendanceStatus())) presentCount++;
            else if ("ABSENT".equals(att.getAttendanceStatus())) absentCount++;
        }

        long totalRecords = currentReport.size();
        long uniqueWorkers = currentReport.stream().map(WorkerAttendanceDTO::getWorkerId).distinct().count();
        long uniqueDays = currentReport.stream().map(WorkerAttendanceDTO::getWorkDate).distinct().count();

        totalWorkDaysCard.setValue(String.valueOf(totalRecords));
        totalPresentCard.setValue(String.valueOf(presentCount));
        totalAbsentCard.setValue(String.valueOf(absentCount));

        if (totalRecords > 0) {
            double rate = ((double) presentCount / totalRecords) * 100;
            attendanceRateCard.setValue(String.format("%.1f%%", rate));
        } else {
            attendanceRateCard.setValue("0%");
        }

        uniqueWorkersCard.setValue(String.valueOf(uniqueWorkers));

        if (uniqueDays > 0) {
            double avgDaily = (double) presentCount / uniqueDays;
            avgDailyWorkersCard.setValue(String.format("%.1f", avgDaily));
        } else {
            avgDailyWorkersCard.setValue("0");
        }
    }

    private void exportReport(String format) {
        if (currentReport == null) return;
        
        String path = reportController.openFileDialog(null);
        if (path == null) return;

        String selectedProj = (String) projectCombo.getSelectedItem();
        String projName = selectedProj.split(" - ")[1].replaceAll("[^a-zA-Z0-9]", "_");
        String reportName = "Attendance_" + projName;

        List<?> data = new ArrayList<>(currentReport);

        if ("CSV".equals(format)) {
            reportController.exportToCSV(data, reportName, path);
        } else if ("EXCEL".equals(format)) {
            reportController.exportToExcel(data, reportName, path);
        } else if ("PDF".equals(format)) {
            reportController.exportToPDF(data, reportName, path, "Attendance Report", "Project: " + projName);
        }
    }
}
