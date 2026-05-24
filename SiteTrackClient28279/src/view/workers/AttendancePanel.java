package view.workers;

import controller.PayrollController;
import controller.ProjectController;
import controller.WorkerController;
import dto.ProjectDTO;
import dto.SiteWorkerDTO;
import dto.WorkerAttendanceDTO;
import session.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendancePanel extends JPanel {
    private PayrollController payrollController;
    private ProjectController projectController;
    private WorkerController workerController;
    
    private JComboBox<String> projectCombo;
    private com.toedter.calendar.JDateChooser workDateField;
    private JButton loadButton;
    private JLabel workerCountLabel;
    
    private JTable workerTable;
    private DefaultTableModel tableModel;
    
    private List<SiteWorkerDTO> activeWorkers;
    private List<WorkerAttendanceDTO> existingAttendance;

    public AttendancePanel() {
        this.payrollController = new PayrollController();
        this.projectController = new ProjectController();
        this.workerController = new WorkerController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        
        initUI();
    }

    private void initUI() {
        // --- North Panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }

        workDateField = new com.toedter.calendar.JDateChooser();
        workDateField.setDate(new java.util.Date());
        workDateField.setPreferredSize(new Dimension(150, 30));

        loadButton = new JButton("Load Workers");
        loadButton.setBackground(Color.decode("#3498db"));
        loadButton.setForeground(Color.WHITE);

        workerCountLabel = new JLabel("Workers: 0");
        workerCountLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));

        topPanel.add(new JLabel("Project:"));
        topPanel.add(projectCombo);
        topPanel.add(new JLabel("Work Date:"));
        topPanel.add(workDateField);
        topPanel.add(loadButton);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(workerCountLabel);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel (Table) ---
        String[] columns = {"Worker ID", "Worker Name", "Worker Type", "Daily Rate", "Attendance Status", "Work Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        workerTable = new JTable(tableModel);
        workerTable.setRowHeight(35);
        workerTable.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        
        // Hide Worker ID
        workerTable.getColumnModel().getColumn(0).setMinWidth(0);
        workerTable.getColumnModel().getColumn(0).setMaxWidth(0);
        workerTable.getColumnModel().getColumn(0).setWidth(0);

        // Daily Rate renderer
        workerTable.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    try {
                        value = currencyFormat.format(new BigDecimal(value.toString()));
                    } catch (Exception e) {}
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                return c;
            }
        });

        // Attendance Status combo editor
        JComboBox<String> statusEditorCombo = new JComboBox<>(new String[]{"Not recorded", "PRESENT", "ABSENT"});
        workerTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusEditorCombo));
        
        // Disable table initially
        workerTable.setEnabled(false);

        add(new JScrollPane(workerTable), BorderLayout.CENTER);

        // --- South Panel (Buttons) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());

        JButton saveAllButton = new JButton("💾 Save All Records");
        saveAllButton.setBackground(Color.decode("#27ae60"));
        saveAllButton.setForeground(Color.WHITE);
        saveAllButton.setFont(new Font("Ubuntu", Font.BOLD, 14));
        saveAllButton.setPreferredSize(new Dimension(180, 40));
        saveAllButton.addActionListener(e -> saveAllRecords());

        bottomPanel.add(clearButton);
        bottomPanel.add(saveAllButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        projectCombo.addItemListener(e -> resetTable());
        workDateField.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) resetTable();
        });

        loadButton.addActionListener(e -> loadWorkers());
    }

    private void resetTable() {
        tableModel.setRowCount(0);
        workerTable.setEnabled(false);
        workerCountLabel.setText("Workers: 0");
    }

    private String getSelectedProjectId() {
        if (projectCombo.getSelectedItem() != null) {
            String selected = (String) projectCombo.getSelectedItem();
            return selected.split(" - ")[0];
        }
        return null;
    }

    private LocalDate getSelectedDate() {
        if (workDateField.getDate() != null) {
            return workDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    private void loadWorkers() {
        String projectId = getSelectedProjectId();
        LocalDate workDate = getSelectedDate();

        if (projectId == null || workDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a Project and Work Date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Fetch only workers assigned to this project
        activeWorkers = workerController.getAssignedWorkersByProject(projectId);
        existingAttendance = payrollController.getAttendanceByProjectAndDate(projectId, workDate);

        Map<String, WorkerAttendanceDTO> attendanceMap = new HashMap<>();
        for (WorkerAttendanceDTO a : existingAttendance) {
            attendanceMap.put(a.getWorkerId(), a);
        }

        tableModel.setRowCount(0);
        for (SiteWorkerDTO w : activeWorkers) {
            WorkerAttendanceDTO att = attendanceMap.get(w.getId());
            String status = att != null ? att.getAttendanceStatus() : "Not recorded";
            String desc = att != null && att.getWorkDescription() != null ? att.getWorkDescription() : "";

            tableModel.addRow(new Object[]{
                w.getId(),
                w.getFullName(),
                w.getWorkerTypeName(),
                w.getDailyRate(),
                status,
                desc
            });
        }

        workerTable.setEnabled(true);
        workerCountLabel.setText("Workers: " + activeWorkers.size());
    }

    private void saveAllRecords() {
        if (workerTable.isEditing()) {
            workerTable.getCellEditor().stopCellEditing();
        }

        String projectId = getSelectedProjectId();
        LocalDate workDate = getSelectedDate();
        
        if (projectId == null || workDate == null) {
            return;
        }

        int successCount = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = (String) tableModel.getValueAt(i, 4);
            
            // Only process rows that have a status set
            if (!"Not recorded".equals(status)) {
                String workerId = (String) tableModel.getValueAt(i, 0);
                String desc = (String) tableModel.getValueAt(i, 5);

                // Check if it already exists
                boolean exists = false;
                WorkerAttendanceDTO existingDto = null;
                for (WorkerAttendanceDTO a : existingAttendance) {
                    if (a.getWorkerId().equals(workerId)) {
                        exists = true;
                        existingDto = a;
                        break;
                    }
                }

                if (exists) {
                    // Update if status or desc changed
                    if (!existingDto.getAttendanceStatus().equals(status) || 
                        (desc != null && !desc.equals(existingDto.getWorkDescription()))) {
                        
                        existingDto.setAttendanceStatus(status);
                        existingDto.setWorkDescription(desc);
                        payrollController.updateAttendance(existingDto);
                        successCount++;
                    }
                } else {
                    // Create new
                    WorkerAttendanceDTO dto = new WorkerAttendanceDTO();
                    dto.setProjectId(projectId);
                    dto.setWorkerId(workerId);
                    dto.setWorkDate(workDate);
                    dto.setAttendanceStatus(status);
                    dto.setWorkDescription(desc);
                    dto.setRecordedByName(SessionManager.getInstance().getCurrentUserId());
                    
                    if (payrollController.recordAttendance(dto) != null) {
                        successCount++;
                    }
                }
            }
        }
        
        if (successCount > 0) {
            JOptionPane.showMessageDialog(this, "Successfully saved " + successCount + " attendance records.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadWorkers(); // refresh to show updated state
        } else {
            JOptionPane.showMessageDialog(this, "No new changes to save.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        workDateField.setDate(new java.util.Date());
        resetTable();
    }
}
