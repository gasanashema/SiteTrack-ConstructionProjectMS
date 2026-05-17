package view.workers;

import controller.WorkerController;
import dto.SiteWorkerDTO;
import dto.WorkerTypeDTO;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkerTypePanel extends JPanel {
    private MainFrame mainFrame;
    private WorkerController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<WorkerTypeDTO> currentTypes;

    public WorkerTypePanel(MainFrame mainFrame, WorkerController controller) {
        this.mainFrame = mainFrame;
        this.controller = controller;
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        
        initUI();
        loadData();
    }

    private void initUI() {
        // --- Action Bar ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(UIManager.getColor("Panel.background"));
        actionPanel.setPreferredSize(new Dimension(0, 50));

        JButton addBtn = new JButton("➕ New Worker Type");
        addBtn.setBackground(Color.decode("#FF5E14"));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        
        JButton editBtn = new JButton("✏️ Edit");
        editBtn.setEnabled(false);
        
        JButton deleteBtn = new JButton("🗑️ Delete");
        deleteBtn.setEnabled(false);
        
        JButton refreshBtn = new JButton("🔄 Refresh");

        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(refreshBtn);
        
        add(actionPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = {"ID", "Type Name", "Default Daily Rate", "Description", "Active Workers"};
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

        // Currency alignment
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            editBtn.setEnabled(selected);
            deleteBtn.setEnabled(selected);
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Actions ---
        addBtn.addActionListener(e -> {
            WorkerTypeFormPanel dialog = new WorkerTypeFormPanel(mainFrame, controller);
            dialog.setVisible(true);
            if (dialog.isSaved()) loadData();
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                WorkerTypeDTO selectedType = currentTypes.get(row);
                WorkerTypeFormPanel dialog = new WorkerTypeFormPanel(mainFrame, controller, selectedType);
                dialog.setVisible(true);
                if (dialog.isSaved()) loadData();
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                WorkerTypeDTO selectedType = currentTypes.get(row);
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Delete worker type '" + selectedType.getTypeName() + "'?\nActive workers may prevent deletion.", 
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (controller.deleteWorkerType(selectedType.getId())) {
                        loadData();
                    }
                }
            }
        });

        refreshBtn.addActionListener(e -> loadData());
    }

    public void loadData() {
        tableModel.setRowCount(0);
        currentTypes = controller.getAllWorkerTypes();
        List<SiteWorkerDTO> activeWorkers = controller.getActiveWorkers();
        
        // Count active workers per type
        Map<String, Long> countByType = activeWorkers.stream()
                .filter(w -> w.getWorkerTypeId() != null)
                .collect(Collectors.groupingBy(SiteWorkerDTO::getWorkerTypeId, Collectors.counting()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
        
        for (WorkerTypeDTO type : currentTypes) {
            String rateFormatted = type.getDefaultDailyRate() != null ? currencyFormat.format(type.getDefaultDailyRate()) : "0.00";
            long count = countByType.getOrDefault(type.getId(), 0L);
            
            tableModel.addRow(new Object[]{
                type.getId(),
                type.getTypeName(),
                rateFormatted,
                type.getDescription(),
                count
            });
        }
    }
}
