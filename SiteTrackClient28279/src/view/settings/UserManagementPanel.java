package view.settings;

import controller.UserController;
import dto.UserDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

public class UserManagementPanel extends JPanel {
    private UserController userController;

    private JComboBox<String> roleFilter;
    private JComboBox<String> statusFilter;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deactivateButton;
    private JButton activateButton;
    private JButton resetPasswordButton;
    private JButton refreshButton;

    private JTable table;
    private DefaultTableModel tableModel;

    private List<UserDTO> allUsers;
    private List<UserDTO> filteredUsers;

    public UserManagementPanel() {
        this.userController = new UserController();
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        loadData();
    }

    private void initUI() {
        // --- North Panel ---
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2c3e50"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel filterActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterActionPanel.setOpaque(false);

        roleFilter = new JComboBox<>(new String[]{"All Roles", "ADMIN", "SITE_MANAGER"});
        statusFilter = new JComboBox<>(new String[]{"All Status", "ACTIVE", "INACTIVE"});
        searchField = new JTextField(15);
        searchField.setToolTipText("Search by name, email, or username");

        addButton = new JButton("➕ New User");
        addButton.setBackground(Color.decode("#2ecc71"));
        addButton.setForeground(Color.WHITE);
        
        editButton = new JButton("✏️ Edit");
        deactivateButton = new JButton("⊘ Deactivate");
        activateButton = new JButton("✓ Activate");
        resetPasswordButton = new JButton("🔑 Reset Password");
        refreshButton = new JButton("🔄 Refresh");

        editButton.setEnabled(false);
        deactivateButton.setEnabled(false);
        activateButton.setEnabled(false);
        resetPasswordButton.setEnabled(false);

        filterActionPanel.add(new JLabel("Role:"));
        filterActionPanel.add(roleFilter);
        filterActionPanel.add(new JLabel("Status:"));
        filterActionPanel.add(statusFilter);
        filterActionPanel.add(new JLabel("Search:"));
        filterActionPanel.add(searchField);
        filterActionPanel.add(Box.createHorizontalStrut(10));
        filterActionPanel.add(addButton);
        filterActionPanel.add(editButton);
        filterActionPanel.add(deactivateButton);
        filterActionPanel.add(activateButton);
        filterActionPanel.add(resetPasswordButton);
        filterActionPanel.add(refreshButton);

        northPanel.add(filterActionPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // --- Center Table ---
        String[] columns = {"ID", "Full Name", "Username", "Email", "Role", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        table.removeColumn(table.getColumnModel().getColumn(0)); // hide ID
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int modelColumn = table.convertColumnIndexToModel(column);
                
                if (modelColumn == 4 && value != null) { // Role
                    setHorizontalAlignment(JLabel.CENTER);
                    if (value.toString().equals("ADMIN")) {
                        c.setBackground(new Color(230, 240, 250));
                        c.setForeground(Color.decode("#2980b9"));
                    } else {
                        c.setBackground(new Color(230, 245, 230));
                        c.setForeground(Color.decode("#27ae60"));
                    }
                } else if (modelColumn == 5 && value != null) { // Status
                    setHorizontalAlignment(JLabel.CENTER);
                    if (value.toString().equals("ACTIVE")) {
                        c.setBackground(new Color(230, 245, 230));
                        c.setForeground(Color.decode("#27ae60"));
                    } else {
                        c.setBackground(new Color(240, 240, 240));
                        c.setForeground(Color.GRAY);
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

        table.getSelectionModel().addListSelectionListener(e -> updateButtonStates());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Event Listeners ---
        refreshButton.addActionListener(e -> loadData());
        
        roleFilter.addActionListener(e -> applyFilters());
        statusFilter.addActionListener(e -> applyFilters());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { applyFilters(); }
        });

        addButton.addActionListener(e -> openForm(null));
        
        editButton.addActionListener(e -> {
            UserDTO selected = getSelectedUser();
            if (selected != null) openForm(selected);
        });

        deactivateButton.addActionListener(e -> {
            UserDTO selected = getSelectedUser();
            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Deactivate " + selected.getUsername() + "? They cannot log in after this.", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (userController.deactivateUser(selected.getId())) loadData();
                }
            }
        });

        activateButton.addActionListener(e -> {
            UserDTO selected = getSelectedUser();
            if (selected != null) {
                if (userController.activateUser(selected.getId())) loadData();
            }
        });

        resetPasswordButton.addActionListener(e -> {
            UserDTO selected = getSelectedUser();
            if (selected != null) {
                String newPass = JOptionPane.showInputDialog(this, "Enter new password for " + selected.getUsername() + ":");
                if (newPass != null && !newPass.trim().isEmpty()) {
                    if (newPass.length() < 8) {
                        JOptionPane.showMessageDialog(this, "Password must be at least 8 characters.");
                        return;
                    }
                    userController.resetPassword(selected.getId(), newPass);
                }
            }
        });
    }

    public void loadData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<List<UserDTO>, Void> worker = new SwingWorker<List<UserDTO>, Void>() {
            @Override
            protected List<UserDTO> doInBackground() {
                return userController.getAllUsers();
            }
            @Override
            protected void done() {
                try {
                    allUsers = get();
                    applyFilters();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    private void applyFilters() {
        if (allUsers == null) return;
        
        String role = (String) roleFilter.getSelectedItem();
        String status = (String) statusFilter.getSelectedItem();
        String search = searchField.getText().toLowerCase();

        filteredUsers = new ArrayList<>();
        for (UserDTO user : allUsers) {
            boolean matchRole = role.equals("All Roles") || user.getRole().equals(role);
            boolean matchStatus = status.equals("All Status") || user.getStatus().equals(status);
            boolean matchSearch = search.isEmpty() || 
                (user.getFullName() != null && user.getFullName().toLowerCase().contains(search)) ||
                (user.getUsername() != null && user.getUsername().toLowerCase().contains(search)) ||
                (user.getEmail() != null && user.getEmail().toLowerCase().contains(search));
                
            if (matchRole && matchStatus && matchSearch) {
                filteredUsers.add(user);
            }
        }
        
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (UserDTO user : filteredUsers) {
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
            });
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        int selectedRow = table.getSelectedRow();
        boolean hasSelection = selectedRow != -1;
        
        editButton.setEnabled(hasSelection);
        resetPasswordButton.setEnabled(hasSelection);
        
        if (hasSelection) {
            String status = (String) tableModel.getValueAt(selectedRow, 5);
            deactivateButton.setEnabled("ACTIVE".equals(status));
            activateButton.setEnabled("INACTIVE".equals(status));
        } else {
            deactivateButton.setEnabled(false);
            activateButton.setEnabled(false);
        }
    }

    private UserDTO getSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return null;
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        return filteredUsers.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
    }

    private void openForm(UserDTO user) {
        UserFormPanel form = new UserFormPanel(Window.getWindows()[0], userController, user, this);
        form.setVisible(true);
    }
}
