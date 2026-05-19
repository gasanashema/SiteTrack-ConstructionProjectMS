package view.projects;

import config.RMIConnection;
import controller.ProjectController;
import dto.ProjectManagerDTO;
import dto.UserDTO;
import service.interfaces.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class AssignManagerPanel extends JDialog {
    private String projectId;
    private ProjectController controller;
    
    private JComboBox<UserItem> availableManagersCombo;
    private DefaultTableModel tableModel;
    private JTable assignedTable;
    
    public AssignManagerPanel(JFrame parent, String projectId, ProjectController controller) {
        super(parent, "Manage Site Managers", true);
        this.projectId = projectId;
        this.controller = controller;
        
        setSize(550, 450);
        setLocationRelativeTo(parent);
        
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top: Assign new manager
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Assign Manager: "));
        
        availableManagersCombo = new JComboBox<>();
        availableManagersCombo.setPreferredSize(new Dimension(200, 30));
        topPanel.add(availableManagersCombo);
        
        JButton assignBtn = new JButton("Assign");
        assignBtn.setBackground(Color.decode("#188038"));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.addActionListener(e -> assignManager());
        topPanel.add(assignBtn);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center: List of currently assigned managers
        String[] cols = {"User ID", "Manager Name", "Assigned Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        assignedTable = new JTable(tableModel);
        assignedTable.setRowHeight(30);
        // Hide User ID column
        assignedTable.getColumnModel().getColumn(0).setMinWidth(0);
        assignedTable.getColumnModel().getColumn(0).setMaxWidth(0);
        assignedTable.getColumnModel().getColumn(0).setWidth(0);
        
        mainPanel.add(new JScrollPane(assignedTable), BorderLayout.CENTER);

        // Bottom: Remove manager & Close
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.setForeground(Color.RED);
        removeBtn.addActionListener(e -> removeSelectedManager());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        
        bottomPanel.add(removeBtn);
        bottomPanel.add(closeBtn);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private void loadData() {
        try {
            // Load assigned
            List<ProjectManagerDTO> assigned = controller.getManagersByProject(projectId);
            tableModel.setRowCount(0);
            if (assigned != null) {
                for (ProjectManagerDTO pm : assigned) {
                    tableModel.addRow(new Object[]{pm.getUserId(), pm.getUserFullName(), pm.getAssignedDate()});
                }
            }

            // Load available
            UserService userService = RMIConnection.getInstance().getService(UserService.class);
            List<UserDTO> siteManagers = userService.getUsersByRole("SITE_MANAGER");
            
            availableManagersCombo.removeAllItems();
            if (siteManagers != null) {
                for (UserDTO user : siteManagers) {
                    // Check if already assigned
                    boolean isAssigned = false;
                    if (assigned != null) {
                        for (ProjectManagerDTO pm : assigned) {
                            if (pm.getUserId().equals(user.getId())) {
                                isAssigned = true;
                                break;
                            }
                        }
                    }
                    if (!isAssigned) {
                        availableManagersCombo.addItem(new UserItem(user.getId(), user.getFullName()));
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load managers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignManager() {
        UserItem selected = (UserItem) availableManagersCombo.getSelectedItem();
        if (selected != null) {
            if (controller.assignManager(projectId, selected.id)) {
                loadData();
            }
        }
    }

    private void removeSelectedManager() {
        int row = assignedTable.getSelectedRow();
        if (row != -1) {
            String userId = (String) tableModel.getValueAt(row, 0);
            if (controller.removeManager(projectId, userId)) {
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a manager to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static class UserItem {
        String id;
        String name;
        UserItem(String id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }
    }
}
