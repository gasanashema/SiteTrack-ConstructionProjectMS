package view.materials;

import controller.MaterialController;
import dto.MaterialCategoryDTO;
import dto.MaterialDTO;
import dto.MaterialPurchaseDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Category Name", "Description"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            List<MaterialCategoryDTO> cats = materialController.getAllCategories();
            for (MaterialCategoryDTO c : cats) {
                model.addRow(new Object[]{c.getId(), c.getCategoryName(), c.getDescription()});
            }
        });
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        // Load initial data
        refreshBtn.doClick();

        return panel;
    }

    private JPanel createMaterialsTab(boolean isAdmin) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Unit", "Price", "Status"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            List<MaterialDTO> mats = isAdmin ? materialController.getAllMaterials() : materialController.getActiveMaterials();
            for (MaterialDTO m : mats) {
                model.addRow(new Object[]{m.getId(), m.getMaterialName(), m.getCategoryName(), m.getUnit(), m.getCurrentPrice(), m.getStatus()});
            }
        });
        topPanel.add(refreshBtn);
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
        JButton refreshBtn = new JButton("Refresh (Requires Project ID)");
        refreshBtn.addActionListener(e -> {
            String projId = JOptionPane.showInputDialog("Enter Project ID:");
            if (projId != null && !projId.trim().isEmpty()) {
                model.setRowCount(0);
                List<MaterialPurchaseDTO> purchases = materialController.getPurchasesByProject(projId);
                for (MaterialPurchaseDTO p : purchases) {
                    model.addRow(new Object[]{p.getId(), p.getProjectName(), p.getMaterialName(), p.getQuantity(), p.getTotalPrice(), p.getPurchaseDate()});
                }
            }
        });
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }
}
