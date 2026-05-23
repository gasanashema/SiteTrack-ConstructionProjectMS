package view.materials;

import controller.StockController;
import controller.ProjectController;
import controller.MaterialController;
import dto.MaterialStockMovementDTO;
import dto.MaterialUsageDTO;
import dto.ProjectMaterialStockDTO;
import dto.ProjectDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockPanel extends JPanel {
    private MainFrame mainFrame;
    private StockController stockController;
    private ProjectController projectController;
    private MaterialController materialController;
    private JTabbedPane tabbedPane;
    private JComboBox<String> projectCombo;

    public StockPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.stockController = new StockController();
        this.projectController = new ProjectController();
        this.materialController = new MaterialController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(UIManager.getColor("Panel.background"));
        
        JLabel titleLabel = new JLabel("Stock & Usage Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        topHeaderPanel.add(titleLabel, BorderLayout.NORTH);

        // Project Selection Panel
        JPanel projectSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        projectSelectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 20));
        projectSelectionPanel.setBackground(UIManager.getColor("Panel.background"));
        
        projectSelectionPanel.add(new JLabel("Select Project: "));
        projectCombo = new JComboBox<>();
        Runnable loadProjects = () -> {
            Object selected = projectCombo.getSelectedItem();
            projectCombo.removeAllItems();
            List<ProjectDTO> projs = projectController.getAllProjects();
            for (ProjectDTO p : projs) {
                projectCombo.addItem(p.getId() + " - " + p.getProjectName());
            }
            if (selected != null) {
                projectCombo.setSelectedItem(selected);
            } else if (projectCombo.getItemCount() > 0) {
                projectCombo.setSelectedIndex(0);
            }
        };
        loadProjects.run();
        
        projectCombo.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                loadProjects.run();
            }
            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });
        projectSelectionPanel.add(projectCombo);
        
        topHeaderPanel.add(projectSelectionPanel, BorderLayout.SOUTH);
        
        add(topHeaderPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));

        tabbedPane.addTab("Current Stock", createStockTab());
        tabbedPane.addTab("Material Purchases", createPurchasesTab());
        tabbedPane.addTab("Stock Movements", createMovementsTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private String getSelectedProjectId() {
        if (projectCombo.getSelectedItem() != null) {
            String selected = (String) projectCombo.getSelectedItem();
            return selected.split(" - ")[0];
        }
        return null;
    }

    private JPanel createStockTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Material", "Qty Available", "Min Qty", "Avg Unit Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        JTable table = new JTable(model);
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    try {
                        java.math.BigDecimal qty = new java.math.BigDecimal(table.getModel().getValueAt(row, 2).toString());
                        java.math.BigDecimal min = new java.math.BigDecimal(table.getModel().getValueAt(row, 3).toString());
                        if (min.compareTo(java.math.BigDecimal.ZERO) > 0) {
                            if (qty.compareTo(min.divide(new java.math.BigDecimal("2"))) < 0) {
                                c.setBackground(new Color(255, 204, 204)); // CRITICAL
                            } else if (qty.compareTo(min) < 0) {
                                c.setBackground(new Color(255, 255, 204)); // LOW
                            } else {
                                c.setBackground(new Color(204, 255, 204)); // OK
                            }
                        } else {
                            c.setBackground(table.getBackground());
                        }
                    } catch (Exception e) {
                        c.setBackground(table.getBackground());
                    }
                }
                return c;
            }
        });

        model.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 3) {
                int row = e.getFirstRow();
                String id = (String) model.getValueAt(row, 0);
                String newValStr = model.getValueAt(row, 3).toString();
                try {
                    java.math.BigDecimal minQty = new java.math.BigDecimal(newValStr);
                    if (minQty.compareTo(java.math.BigDecimal.ZERO) < 0) throw new NumberFormatException();
                    stockController.updateMinimumQuantity(id, minQty);
                    table.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid minimum quantity");
                }
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Load Project Stock");
        refreshBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                model.setRowCount(0);
                List<ProjectMaterialStockDTO> stock = stockController.getStockByProject(projId);
                for (ProjectMaterialStockDTO s : stock) {
                    model.addRow(new Object[]{s.getId(), s.getMaterialName(), s.getQuantityAvailable(), s.getMinimumQuantity(), s.getAverageUnitPrice()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton adjustBtn = new JButton("Adjust Stock");
        if (!SessionManager.getInstance().isAdmin()) {
            adjustBtn.setVisible(false);
        }
        adjustBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                StockAdjustmentDialog dialog = new StockAdjustmentDialog(mainFrame, stockController, materialController, projId);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    refreshBtn.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        topPanel.add(refreshBtn);
        topPanel.add(adjustBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createPurchasesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Project", "Material", "Qty", "Total Price", "Date"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshBtn = new JButton("Load Purchases");
        refreshBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                model.setRowCount(0);
                List<dto.MaterialPurchaseDTO> purchases = materialController.getPurchasesByProject(projId);
                for (dto.MaterialPurchaseDTO p : purchases) {
                    model.addRow(new Object[]{p.getId(), p.getProjectName(), p.getMaterialName(), p.getQuantity(), p.getTotalPrice(), p.getPurchaseDate()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton addBtn = new JButton("Record Purchase");
        addBtn.addActionListener(e -> {
            PurchaseFormPanel dialog = new PurchaseFormPanel(mainFrame, materialController);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshBtn.doClick();
            }
        });
        
        topPanel.add(refreshBtn);
        topPanel.add(addBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }


    private JPanel createMovementsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Material", "Type", "Qty", "Ref ID", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        
        table.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null && !isSelected) {
                    String type = value.toString();
                    if (type.equals("IN")) {
                        c.setBackground(new Color(204, 229, 255)); // Blue
                        c.setForeground(Color.BLACK);
                    } else if (type.equals("OUT")) {
                        c.setBackground(new Color(255, 204, 204)); // Red
                        c.setForeground(Color.BLACK);
                    } else if (type.equals("ADJUSTMENT")) {
                        c.setBackground(new Color(255, 229, 204)); // Orange
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }
                }
                return c;
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JComboBox<String> materialFilter = new JComboBox<>();
        materialFilter.addItem("All Materials");
        
        JComboBox<String> typeFilter = new JComboBox<>(new String[]{"All Types", "IN", "OUT", "ADJUSTMENT"});
        
        com.toedter.calendar.JDateChooser fromDate = new com.toedter.calendar.JDateChooser();
        com.toedter.calendar.JDateChooser toDate = new com.toedter.calendar.JDateChooser();
        
        JButton filterBtn = new JButton("Apply Filters");
        
        int x = 0;
        gbc.gridy = 0;
        
        gbc.gridx = x++; topPanel.add(new JLabel("Material:"), gbc);
        gbc.gridx = x++; topPanel.add(materialFilter, gbc);
        
        gbc.gridx = x++; topPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = x++; topPanel.add(typeFilter, gbc);
        
        gbc.gridx = x++; topPanel.add(new JLabel("From:"), gbc);
        gbc.gridx = x++; topPanel.add(fromDate, gbc);
        
        gbc.gridx = x++; topPanel.add(new JLabel("To:"), gbc);
        gbc.gridx = x++; topPanel.add(toDate, gbc);
        
        gbc.gridx = x++; topPanel.add(filterBtn, gbc);

        filterBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                if (materialFilter.getItemCount() == 1) {
                    List<dto.MaterialDTO> mats = materialController.getActiveMaterials();
                    for(dto.MaterialDTO m : mats) materialFilter.addItem(m.getMaterialName());
                }
                
                model.setRowCount(0);
                List<MaterialStockMovementDTO> movements = stockController.getMovementsByProject(projId);
                
                String selMat = (String) materialFilter.getSelectedItem();
                String selType = (String) typeFilter.getSelectedItem();
                java.time.LocalDate fDate = fromDate.getDate() != null ? new java.sql.Date(fromDate.getDate().getTime()).toLocalDate() : null;
                java.time.LocalDate tDate = toDate.getDate() != null ? new java.sql.Date(toDate.getDate().getTime()).toLocalDate() : null;
                
                for (MaterialStockMovementDTO m : movements) {
                    boolean matchMat = selMat.equals("All Materials") || m.getMaterialName().equals(selMat);
                    boolean matchType = selType.equals("All Types") || m.getMovementType().equals(selType);
                    boolean matchFrom = fDate == null || !m.getMovementDate().isBefore(fDate);
                    boolean matchTo = tDate == null || !m.getMovementDate().isAfter(tDate);
                    
                    if (matchMat && matchType && matchFrom && matchTo) {
                        model.addRow(new Object[]{m.getMovementDate(), m.getMaterialName(), m.getMovementType(), m.getQuantity(), m.getReferenceId(), m.getDescription()});
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }
}
