package view.materials;

import controller.MaterialController;
import dto.MaterialCategoryDTO;
import dto.MaterialDTO;
import dto.MaterialPurchaseDTO;
import dto.ProjectDTO;
import controller.ProjectController;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.List;

public class MaterialPanel extends JPanel {
    private MainFrame mainFrame;
    private MaterialController materialController;
    private JTabbedPane tabbedPane;

    public MaterialPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.materialController = new MaterialController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = new JLabel("Materials Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));

        // If Admin, show all tabs. If Site Manager, just show Materials list.
        if (SessionManager.getInstance().isAdmin()) {
            tabbedPane.addTab("Categories", createCategoriesTab());
            tabbedPane.addTab("Materials Definition", createMaterialsTab(true));
            tabbedPane.addTab("Purchases", createPurchasesTab());
        } else {
            tabbedPane.addTab("Available Materials", createMaterialsTab(false));
        }

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCategoriesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Category Name", "Unit", "Description"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            List<MaterialCategoryDTO> cats = materialController.getAllCategories();
            for (MaterialCategoryDTO c : cats) {
                model.addRow(new Object[]{c.getId(), c.getCategoryName(), c.getUnit(), c.getDescription()});
            }
        });
        JButton addBtn = new JButton("Add Category");
        addBtn.addActionListener(e -> {
            CategoryFormPanel dialog = new CategoryFormPanel(mainFrame, materialController);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshBtn.doClick();
            }
        });
        topPanel.add(refreshBtn);
        topPanel.add(addBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        // Load initial data
        refreshBtn.doClick();

        return panel;
    }

    private JPanel createMaterialsTab(boolean isAdmin) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Unit", "Status"}, 0);
        JTable table = new JTable(model);
        
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        topPanel.add(new JLabel("Search: "));
        JTextField searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        topPanel.add(searchField);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            List<MaterialDTO> mats = isAdmin ? materialController.getAllMaterials() : materialController.getActiveMaterials();
            for (MaterialDTO m : mats) {
                model.addRow(new Object[]{m.getId(), m.getMaterialName(), m.getCategoryName(), m.getUnit(), m.getStatus()});
            }
        });
        topPanel.add(refreshBtn);
        if (isAdmin) {
            JButton addBtn = new JButton("Add Material");
            addBtn.addActionListener(e -> {
                MaterialFormPanel dialog = new MaterialFormPanel(mainFrame, null, materialController);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    refreshBtn.doClick();
                }
            });
            topPanel.add(addBtn);

            JButton editBtn = new JButton("Edit Material");
            editBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(panel, "Please select a material to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String matId = (String) table.getValueAt(row, 0);
                MaterialDTO dto = materialController.getMaterialById(matId);
                if (dto != null) {
                    MaterialFormPanel dialog = new MaterialFormPanel(mainFrame, dto, materialController);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        refreshBtn.doClick();
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to load material details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            topPanel.add(editBtn);
        }
        panel.add(topPanel, BorderLayout.NORTH);

        refreshBtn.doClick();

        return panel;
    }

    private JPanel createPurchasesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Project", "Material", "Qty", "Total Price", "Date"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JComboBox<String> projectCombo = new JComboBox<>();
        ProjectController pc = new ProjectController();
        List<ProjectDTO> projects = pc.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }
        
        JButton refreshBtn = new JButton("Load Purchases");
        refreshBtn.addActionListener(e -> {
            if (projectCombo.getSelectedItem() != null) {
                String selected = (String) projectCombo.getSelectedItem();
                String projId = selected.split(" - ")[0];
                model.setRowCount(0);
                List<MaterialPurchaseDTO> purchases = materialController.getPurchasesByProject(projId);
                for (MaterialPurchaseDTO p : purchases) {
                    model.addRow(new Object[]{p.getId(), p.getProjectName(), p.getMaterialName(), p.getQuantity(), p.getTotalPrice(), p.getPurchaseDate()});
                }
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
        
        topPanel.add(new JLabel("Select Project: "));
        topPanel.add(projectCombo);
        topPanel.add(refreshBtn);
        topPanel.add(addBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }
}
