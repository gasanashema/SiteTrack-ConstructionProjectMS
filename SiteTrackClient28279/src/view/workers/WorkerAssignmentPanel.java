package view.workers;

import controller.ProjectController;
import controller.WorkerController;
import dto.ProjectDTO;
import dto.SiteWorkerDTO;
import dto.WorkerAssignmentDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class WorkerAssignmentPanel extends JPanel {
    private final WorkerController workerController;
    private final ProjectController projectController;

    private JTabbedPane tabbedPane;

    // Assign Tab Components
    private JComboBox<String> assignProjectCombo;
    private com.toedter.calendar.JDateChooser assignDateField;
    private JTable unassignedWorkersTable;
    private DefaultTableModel unassignedTableModel;

    // Transfer Tab Components
    private JComboBox<String> transferToProjectCombo;
    private com.toedter.calendar.JDateChooser transferDateField;
    private JTable activeAssignmentsTable;
    private DefaultTableModel activeAssignmentsTableModel;

    private List<SiteWorkerDTO> allWorkersCache;
    private List<WorkerAssignmentDTO> activeAssignmentsCache;

    public WorkerAssignmentPanel() {
        this.workerController = new WorkerController();
        this.projectController = new ProjectController();

        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        initUI();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 14));

        tabbedPane.addTab("Assign Workers to Project", createAssignTab());
        tabbedPane.addTab("Transfer Worker", createTransferTab());

        tabbedPane.addChangeListener(e -> refreshData());

        add(tabbedPane, BorderLayout.CENTER);

        // Initial load
        SwingUtilities.invokeLater(this::refreshData);
    }

    private JPanel createAssignTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        assignProjectCombo = new JComboBox<>();
        assignDateField = new com.toedter.calendar.JDateChooser();
        assignDateField.setDate(new java.util.Date());
        assignDateField.setPreferredSize(new Dimension(150, 30));

        topPanel.add(new JLabel("Select Project:"));
        topPanel.add(assignProjectCombo);
        topPanel.add(new JLabel("Assignment Date:"));
        topPanel.add(assignDateField);

        // Center Table (Workers to assign)
        String[] columns = {"Select", "Worker ID", "Name", "Type"};
        unassignedTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        unassignedWorkersTable = new JTable(unassignedTableModel);
        unassignedWorkersTable.setRowHeight(30);

        // Hide ID column
        unassignedWorkersTable.getColumnModel().getColumn(1).setMinWidth(0);
        unassignedWorkersTable.getColumnModel().getColumn(1).setMaxWidth(0);
        unassignedWorkersTable.getColumnModel().getColumn(1).setWidth(0);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(unassignedWorkersTable), BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton assignBtn = new JButton("Assign Selected Workers");
        assignBtn.setBackground(Color.decode("#3498db"));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setFont(new Font("Ubuntu", Font.BOLD, 14));
        assignBtn.addActionListener(e -> executeAssignment());
        bottomPanel.add(assignBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTransferTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Info
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select an active assignment from the list below to transfer them to a new project."));

        // Center Table (Active Assignments)
        String[] columns = {"Worker ID", "Name", "Current Project", "Assigned Date"};
        activeAssignmentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        activeAssignmentsTable = new JTable(activeAssignmentsTableModel);
        activeAssignmentsTable.setRowHeight(30);
        activeAssignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        activeAssignmentsTable.getColumnModel().getColumn(0).setMinWidth(0);
        activeAssignmentsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        activeAssignmentsTable.getColumnModel().getColumn(0).setWidth(0);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(activeAssignmentsTable), BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        transferToProjectCombo = new JComboBox<>();
        transferDateField = new com.toedter.calendar.JDateChooser();
        transferDateField.setDate(new java.util.Date());
        transferDateField.setPreferredSize(new Dimension(150, 30));

        JButton transferBtn = new JButton("Transfer Worker");
        transferBtn.setBackground(Color.decode("#e67e22"));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.setFont(new Font("Ubuntu", Font.BOLD, 14));
        transferBtn.addActionListener(e -> executeTransfer());

        bottomPanel.add(new JLabel("Destination Project:"));
        bottomPanel.add(transferToProjectCombo);
        bottomPanel.add(new JLabel("Transfer Date:"));
        bottomPanel.add(transferDateField);
        bottomPanel.add(transferBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    public void refreshData() {
        List<ProjectDTO> projects = projectController.getAllProjects();
        assignProjectCombo.removeAllItems();
        transferToProjectCombo.removeAllItems();
        for (ProjectDTO p : projects) {
            String item = p.getId() + " - " + p.getProjectName();
            assignProjectCombo.addItem(item);
            transferToProjectCombo.addItem(item);
        }

        allWorkersCache = workerController.getActiveWorkers();
        activeAssignmentsCache = workerController.getActiveAssignments();

        // Populate Assign Table (Workers without an active assignment)
        unassignedTableModel.setRowCount(0);
        for (SiteWorkerDTO w : allWorkersCache) {
            boolean isAssigned = activeAssignmentsCache.stream().anyMatch(a -> a.getWorkerId().equals(w.getId()));
            if (!isAssigned) {
                unassignedTableModel.addRow(new Object[]{false, w.getId(), w.getFullName(), w.getWorkerTypeName()});
            }
        }

        // Populate Transfer Table
        activeAssignmentsTableModel.setRowCount(0);
        for (WorkerAssignmentDTO a : activeAssignmentsCache) {
            activeAssignmentsTableModel.addRow(new Object[]{a.getWorkerId(), a.getWorkerName(), a.getProjectName(), a.getAssignedDate().toString()});
        }
    }

    private void executeAssignment() {
        if (assignProjectCombo.getSelectedItem() == null || assignDateField.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a project and date.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String projectId = ((String) assignProjectCombo.getSelectedItem()).split(" - ")[0];
        LocalDate date = assignDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<String> selectedWorkerIds = new ArrayList<>();
        for (int i = 0; i < unassignedTableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) unassignedTableModel.getValueAt(i, 0);
            if (selected != null && selected) {
                selectedWorkerIds.add((String) unassignedTableModel.getValueAt(i, 1));
            }
        }

        if (selectedWorkerIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one worker to assign.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (workerController.assignWorkers(selectedWorkerIds, projectId, date)) {
            refreshData();
        }
    }

    private void executeTransfer() {
        int row = activeAssignmentsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an active assignment from the table.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (transferToProjectCombo.getSelectedItem() == null || transferDateField.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a destination project and date.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String workerId = (String) activeAssignmentsTableModel.getValueAt(row, 0);
        String toProjectId = ((String) transferToProjectCombo.getSelectedItem()).split(" - ")[0];
        LocalDate date = transferDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (workerController.transferWorker(workerId, toProjectId, date)) {
            refreshData();
        }
    }
}
