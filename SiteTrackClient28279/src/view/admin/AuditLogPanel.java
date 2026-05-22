package view.admin;

import controller.SystemAdminController;
import controller.ReportController;

import dto.AuditLogDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AuditLogPanel extends JPanel {
    private SystemAdminController adminController;
    private ReportController reportController;

    private JComboBox<String> userCombo;
    private JComboBox<String> eventTypeCombo;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    
    private JButton filterButton;
    private JButton clearButton;
    private JButton exportButton;

    private JTable table;
    private DefaultTableModel tableModel;

    private List<AuditLogDTO> allLogs;

    public AuditLogPanel() {
        this.adminController = new SystemAdminController();
        this.reportController = new ReportController();

        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        loadData();
    }

    private void initUI() {
        // --- North Filter Bar ---
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Audit Log");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);

        userCombo = new JComboBox<>(new String[]{"All Users", "admin_user", "manager1"});
        eventTypeCombo = new JComboBox<>(new String[]{
            "All Events", "Login", "User Created", "User Updated", 
            "Project Created", "Project Updated", "Material Purchased"
        });

        fromDateField = new com.toedter.calendar.JDateChooser();
        toDateField = new com.toedter.calendar.JDateChooser();

        filterButton = new JButton("Apply Filter");
        filterButton.setBackground(Color.decode("#3498db"));
        filterButton.setForeground(Color.WHITE);
        
        clearButton = new JButton("Clear Filters");
        exportButton = new JButton("📥 Export CSV");

        filterPanel.add(new JLabel("User:"));
        filterPanel.add(userCombo);
        filterPanel.add(new JLabel("Event:"));
        filterPanel.add(eventTypeCombo);
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDateField);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDateField);
        filterPanel.add(filterButton);
        filterPanel.add(clearButton);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(exportButton);

        northPanel.add(filterPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // --- Center Table ---
        String[] columns = {"Timestamp", "User", "Event Type", "Entity", "Details", "IP/Client", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Ubuntu", Font.PLAIN, 13));
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 6 && value != null) { // Status
                    setHorizontalAlignment(JLabel.CENTER);
                    if (value.toString().equals("SUCCESS")) {
                        c.setBackground(new Color(230, 245, 230));
                        c.setForeground(Color.decode("#27ae60"));
                    } else if (value.toString().equals("FAILURE")) {
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

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showDetailDialog();
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Actions ---
        filterButton.addActionListener(e -> applyFilters());
        clearButton.addActionListener(e -> {
            userCombo.setSelectedIndex(0);
            eventTypeCombo.setSelectedIndex(0);
            fromDateField.setDate(null);
            toDateField.setDate(null);
            applyFilters();
        });
        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Export function coming soon."));
    }

    private void loadData() {
        allLogs = adminController.getAuditLogs();
        applyFilters();
    }

    private void applyFilters() {
        String user = (String) userCombo.getSelectedItem();
        String event = (String) eventTypeCombo.getSelectedItem();
        
        List<AuditLogDTO> filtered = allLogs.stream().filter(log -> {
            boolean matchUser = "All Users".equals(user) || log.getUsername().equals(user);
            boolean matchEvent = "All Events".equals(event) || log.getEventType().equals(event);
            return matchUser && matchEvent;
        }).collect(Collectors.toList());

        tableModel.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (AuditLogDTO log : filtered) {
            tableModel.addRow(new String[]{
                log.getCreatedAt().format(dtf),
                log.getUsername(),
                log.getEventType(),
                log.getEntityName(),
                log.getDetails(),
                log.getIpAddress(),
                "SUCCESS" // Assuming success for now
            });
        }
    }

    private void showDetailDialog() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        JDialog dialog = new JDialog(Window.getWindows()[0], "Audit Log Detail", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel(new GridLayout(7, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] cols = {"Timestamp", "User", "Event Type", "Entity", "Details", "IP/Client", "Status"};
        for (int i = 0; i < cols.length; i++) {
            p.add(new JLabel("<html><b>" + cols[i] + ":</b></html>"));
            p.add(new JLabel((String) tableModel.getValueAt(row, i)));
        }

        dialog.add(p, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bp = new JPanel();
        bp.add(closeBtn);
        dialog.add(bp, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
