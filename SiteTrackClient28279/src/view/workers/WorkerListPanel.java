package view.workers;

import controller.WorkerController;
import dto.SiteWorkerDTO;
import dto.WorkerTypeDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WorkerListPanel extends JPanel {
    private MainFrame mainFrame;
    private WorkerController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<SiteWorkerDTO> allWorkers;
    private List<SiteWorkerDTO> filteredWorkers;
    
    private JComboBox<String> typeFilter;
    private JComboBox<String> statusFilter;
    private JTextField searchField;

    public WorkerListPanel(MainFrame mainFrame, WorkerController controller) {
        this.mainFrame = mainFrame;
        this.controller = controller;
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        
        initUI();
        loadData();
    }

    private void initUI() {
        // --- Filter & Action Bar ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIManager.getColor("Panel.background"));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(UIManager.getColor("Panel.background"));
        
        typeFilter = new JComboBox<>();
        typeFilter.addItem("All Types");
        List<WorkerTypeDTO> types = controller.getAllWorkerTypes();
        for (WorkerTypeDTO t : types) {
            typeFilter.addItem(t.getTypeName());
        }
        
        statusFilter = new JComboBox<>(new String[]{"All", "ACTIVE", "INACTIVE"});
        
        searchField = new JTextField(15);
        searchField.setToolTipText("Search by name or phone");

        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeFilter);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        
        topPanel.add(filterPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton addBtn = new JButton("➕ Register Worker");
        addBtn.setBackground(Color.decode("#27ae60"));
        addBtn.setForeground(Color.WHITE);
        
        JButton editBtn = new JButton("✏️ Edit");
        JButton deactivateBtn = new JButton("⊘ Deactivate");
        JButton activateBtn = new JButton("✓ Activate");
        JButton deleteBtn = new JButton("🗑️ Delete");
        JButton refreshBtn = new JButton("🔄 Refresh");

        boolean isAdmin = SessionManager.getInstance().isAdmin();
        
        actionPanel.add(addBtn);
        if (isAdmin) {
            actionPanel.add(editBtn);
            actionPanel.add(deactivateBtn);
            actionPanel.add(activateBtn);
            actionPanel.add(deleteBtn);
        }
        actionPanel.add(refreshBtn);
        
        topPanel.add(actionPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = {"ID", "Full Name", "Phone", "Type", "Daily Rate", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Ubuntu", Font.BOLD, 14));
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Custom renderer for Status and Rate
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 4 && value != null) { // Daily Rate
                    try {
                        java.math.BigDecimal rate = new java.math.BigDecimal(value.toString());
                        setText(currencyFormat.format(rate));
                        setHorizontalAlignment(JLabel.RIGHT);
                    } catch (Exception e) {}
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }
                
                if (column == 5) { // Status
                    setHorizontalAlignment(JLabel.CENTER);
                    if ("ACTIVE".equals(value)) {
                        c.setBackground(new Color(204, 255, 204)); // Greenish
                        c.setForeground(Color.BLACK);
                    } else if ("INACTIVE".equals(value)) {
                        c.setBackground(new Color(224, 224, 224)); // Grayish
                        c.setForeground(Color.BLACK);
                    }
                } else if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                    c.setForeground(table.getForeground());
                }
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Button Logic ---
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            if (isAdmin) {
                editBtn.setEnabled(selected);
                deleteBtn.setEnabled(selected);
                
                if (selected) {
                    String status = (String) table.getValueAt(table.getSelectedRow(), 5);
                    deactivateBtn.setEnabled("ACTIVE".equals(status));
                    activateBtn.setEnabled("INACTIVE".equals(status));
                } else {
                    deactivateBtn.setEnabled(false);
                    activateBtn.setEnabled(false);
                }
            }
        });
        
        // Initial state
        if (isAdmin) {
            editBtn.setEnabled(false);
            deactivateBtn.setEnabled(false);
            activateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        // --- Listeners ---
        typeFilter.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) filterAndRefresh();
        });
        statusFilter.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) filterAndRefresh();
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { filterAndRefresh(); }
        });

        addBtn.addActionListener(e -> {
            WorkerFormPanel dialog = new WorkerFormPanel(mainFrame, controller);
            dialog.setVisible(true);
            if (dialog.isSaved()) loadData();
        });

        if (isAdmin) {
            editBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    SiteWorkerDTO selectedWorker = filteredWorkers.get(row);
                    WorkerFormPanel dialog = new WorkerFormPanel(mainFrame, controller, selectedWorker);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) loadData();
                }
            });

            deactivateBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    if (controller.deactivateWorker(filteredWorkers.get(row).getId())) loadData();
                }
            });

            activateBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    if (controller.activateWorker(filteredWorkers.get(row).getId())) loadData();
                }
            });

            deleteBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    SiteWorkerDTO selectedWorker = filteredWorkers.get(row);
                    int confirm = JOptionPane.showConfirmDialog(this, 
                            "Delete worker '" + selectedWorker.getFullName() + "'?", 
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.deleteWorker(selectedWorker.getId())) loadData();
                    }
                }
            });
        }

        refreshBtn.addActionListener(e -> loadData());
    }

    public void loadData() {
        allWorkers = controller.getAllWorkers();
        filterAndRefresh();
    }
    
    private void filterAndRefresh() {
        if (allWorkers == null) return;
        
        String tFilter = (String) typeFilter.getSelectedItem();
        String sFilter = (String) statusFilter.getSelectedItem();
        String search = searchField.getText().toLowerCase().trim();
        
        filteredWorkers = new java.util.ArrayList<>();
        for (SiteWorkerDTO w : allWorkers) {
            boolean matchType = tFilter.equals("All Types") || (w.getWorkerTypeName() != null && w.getWorkerTypeName().equals(tFilter));
            boolean matchStatus = sFilter.equals("All") || w.getStatus().equals(sFilter);
            boolean matchSearch = search.isEmpty() || 
                (w.getFullName() != null && w.getFullName().toLowerCase().contains(search)) ||
                (w.getPhone() != null && w.getPhone().toLowerCase().contains(search));
                
            if (matchType && matchStatus && matchSearch) {
                filteredWorkers.add(w);
            }
        }
        
        tableModel.setRowCount(0);
        for (SiteWorkerDTO w : filteredWorkers) {
            tableModel.addRow(new Object[]{
                w.getId(),
                w.getFullName(),
                w.getPhone() != null ? w.getPhone() : "",
                w.getWorkerTypeName(),
                w.getDailyRate() != null ? w.getDailyRate().toString() : "0", // Render handles formatting
                w.getStatus()
            });
        }
    }
}
