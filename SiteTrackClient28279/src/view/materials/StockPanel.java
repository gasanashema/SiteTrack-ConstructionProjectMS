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
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }
        projectSelectionPanel.add(projectCombo);
        
        topHeaderPanel.add(projectSelectionPanel, BorderLayout.SOUTH);
        
        add(topHeaderPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));

        tabbedPane.addTab("Current Stock", createStockTab());
        tabbedPane.addTab("Material Purchases", createPurchasesTab());
        tabbedPane.addTab("Material Usage", createUsagesTab());
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

        DefaultTableModel model = new DefaultTableModel(new String[]{"Material", "Qty Available", "Min Qty", "Avg Unit Price"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Load Project Stock");
        refreshBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                model.setRowCount(0);
                List<ProjectMaterialStockDTO> stock = stockController.getStockByProject(projId);
                for (ProjectMaterialStockDTO s : stock) {
                    model.addRow(new Object[]{s.getMaterialName(), s.getQuantityAvailable(), s.getMinimumQuantity(), s.getAverageUnitPrice()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        topPanel.add(refreshBtn);
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

    private JPanel createUsagesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Project", "Material", "Qty Used", "Date", "Activity"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshBtn = new JButton("Load Usages");
        refreshBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                model.setRowCount(0);
                List<dto.MaterialUsageDTO> usages = materialController.getUsageByProject(projId);
                for (dto.MaterialUsageDTO u : usages) {
                    model.addRow(new Object[]{u.getId(), u.getProjectName(), u.getMaterialName(), u.getQuantityUsed(), u.getUsageDate(), u.getActivityDescription()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton addBtn = new JButton("Record Usage");
        addBtn.addActionListener(e -> {
            UsageFormPanel dialog = new UsageFormPanel(mainFrame, materialController);
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

        DefaultTableModel model = new DefaultTableModel(new String[]{"Material", "Type", "Qty Change", "Ref ID", "Date"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Load Movements");
        refreshBtn.addActionListener(e -> {
            String projId = getSelectedProjectId();
            if (projId != null) {
                model.setRowCount(0);
                List<MaterialStockMovementDTO> movements = stockController.getMovementsByProject(projId);
                for (MaterialStockMovementDTO m : movements) {
                    model.addRow(new Object[]{m.getMaterialName(), m.getMovementType(), m.getQuantity(), m.getReferenceId(), m.getMovementDate()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }
}
