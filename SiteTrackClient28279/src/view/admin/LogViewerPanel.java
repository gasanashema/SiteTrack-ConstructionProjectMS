package view.admin;

import controller.SystemAdminController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class LogViewerPanel extends JPanel {
    private SystemAdminController adminController;

    private JComboBox<String> levelCombo;
    private JTextField searchField;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    
    private JButton filterButton;
    private JButton clearButton;
    private JButton deleteLogsButton;
    private JButton exportButton;
    private JButton refreshButton;

    private JTable table;
    private DefaultTableModel tableModel;

    private List<String[]> allLogs;

    public LogViewerPanel() {
        this.adminController = new SystemAdminController();

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

        JLabel titleLabel = new JLabel("System Log Viewer");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2c3e50"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);

        levelCombo = new JComboBox<>(new String[]{"All Levels", "DEBUG", "INFO", "WARN", "ERROR"});
        searchField = new JTextField(15);
        
        fromDateField = new com.toedter.calendar.JDateChooser();
        toDateField = new com.toedter.calendar.JDateChooser();

        filterButton = new JButton("Apply Filter");
        filterButton.setBackground(Color.decode("#3498db"));
        filterButton.setForeground(Color.WHITE);
        
        clearButton = new JButton("Clear Filters");
        deleteLogsButton = new JButton("🗑️ Delete Old Logs");
        exportButton = new JButton("📥 Export Logs");
        refreshButton = new JButton("🔄 Refresh");

        filterPanel.add(new JLabel("Level:"));
        filterPanel.add(levelCombo);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDateField);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDateField);
        filterPanel.add(filterButton);
        filterPanel.add(clearButton);
        filterPanel.add(refreshButton);
        
        JPanel extraActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        extraActionPanel.setOpaque(false);
        extraActionPanel.add(exportButton);
        extraActionPanel.add(deleteLogsButton);

        JPanel controls = new JPanel(new BorderLayout());
        controls.setOpaque(false);
        controls.add(filterPanel, BorderLayout.CENTER);
        controls.add(extraActionPanel, BorderLayout.EAST);

        northPanel.add(controls, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // --- Center Table ---
        String[] columns = {"Timestamp", "Level", "Logger", "Message", "Exception"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1 && value != null) { // Level
                    setHorizontalAlignment(JLabel.CENTER);
                    String lvl = value.toString();
                    if (lvl.equals("ERROR")) {
                        c.setBackground(Color.decode("#c0392b"));
                        c.setForeground(Color.WHITE);
                    } else if (lvl.equals("WARN")) {
                        c.setBackground(Color.decode("#f39c12"));
                        c.setForeground(Color.WHITE);
                    } else if (lvl.equals("INFO")) {
                        c.setBackground(Color.decode("#3498db"));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(Color.LIGHT_GRAY);
                        c.setForeground(Color.BLACK);
                    }
                } else if (column == 4 && value != null && !value.toString().isEmpty()) { // Exception
                    c.setForeground(Color.RED);
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
            levelCombo.setSelectedIndex(0);
            searchField.setText("");
            fromDateField.setDate(null);
            toDateField.setDate(null);
            applyFilters();
        });
        refreshButton.addActionListener(e -> loadData());
        
        exportButton.addActionListener(e -> adminController.exportDataPlaceholder("System Logs", "CSV"));
        deleteLogsButton.addActionListener(e -> {
            String months = JOptionPane.showInputDialog(this, "Delete logs older than (months):", "3");
            if (months != null && !months.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Logs older than " + months + " months have been deleted.");
                loadData();
            }
        });
    }

    private void loadData() {
        allLogs = adminController.getDummySystemLogs();
        applyFilters();
    }

    private void applyFilters() {
        String level = (String) levelCombo.getSelectedItem();
        String search = searchField.getText().toLowerCase();
        
        List<String[]> filtered = allLogs.stream().filter(log -> {
            boolean matchLevel = "All Levels".equals(level) || log[1].equals(level);
            boolean matchSearch = search.isEmpty() || log[3].toLowerCase().contains(search) || log[2].toLowerCase().contains(search);
            return matchLevel && matchSearch;
        }).collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (String[] log : filtered) {
            tableModel.addRow(log);
        }
    }

    private void showDetailDialog() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        JDialog dialog = new JDialog(Window.getWindows()[0], "Log Entry Detail", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel p = new JPanel(new GridLayout(4, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        p.add(new JLabel("Timestamp: " + tableModel.getValueAt(row, 0) + " | Level: " + tableModel.getValueAt(row, 1)));
        p.add(new JLabel("Logger: " + tableModel.getValueAt(row, 2)));
        
        JTextArea msgArea = new JTextArea((String) tableModel.getValueAt(row, 3));
        msgArea.setEditable(false);
        msgArea.setLineWrap(true);
        p.add(new JScrollPane(msgArea));
        
        JTextArea excArea = new JTextArea((String) tableModel.getValueAt(row, 4));
        excArea.setEditable(false);
        excArea.setForeground(Color.RED);
        p.add(new JScrollPane(excArea));

        dialog.add(p, BorderLayout.CENTER);
        
        JButton copyBtn = new JButton("Copy to Clipboard");
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(copyBtn);
        bp.add(closeBtn);
        dialog.add(bp, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
