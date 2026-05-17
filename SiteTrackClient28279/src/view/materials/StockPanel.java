package view.materials;

import controller.StockController;
import dto.MaterialStockMovementDTO;
import dto.MaterialUsageDTO;
import dto.ProjectMaterialStockDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockPanel extends JPanel {
    private MainFrame mainFrame;
    private StockController stockController;
    private JTabbedPane tabbedPane;

    public StockPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.stockController = new StockController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = new JLabel("Stock & Usage Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));

        tabbedPane.addTab("Current Stock", createStockTab());
        
        if (SessionManager.getInstance().isSiteManager()) {
            tabbedPane.addTab("Record Usage", createUsageTab());
        }
        
        tabbedPane.addTab("Stock Movements", createMovementsTab());

        add(tabbedPane, BorderLayout.CENTER);
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
            String projId = JOptionPane.showInputDialog("Enter Project ID:");
            if (projId != null && !projId.trim().isEmpty()) {
                model.setRowCount(0);
                List<ProjectMaterialStockDTO> stock = stockController.getStockByProject(projId);
                for (ProjectMaterialStockDTO s : stock) {
                    model.addRow(new Object[]{s.getMaterialName(), s.getQuantityAvailable(), s.getMinimumQuantity(), s.getAverageUnitPrice()});
                }
            }
        });
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createUsageTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Material", "Qty Used", "Date", "Notes"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Load Usage (Project ID)");
        refreshBtn.addActionListener(e -> {
            String projId = JOptionPane.showInputDialog("Enter Project ID:");
            if (projId != null && !projId.trim().isEmpty()) {
                model.setRowCount(0);
                List<MaterialUsageDTO> usages = stockController.getUsageByProject(projId);
                for (MaterialUsageDTO u : usages) {
                    model.addRow(new Object[]{u.getId(), u.getMaterialName(), u.getQuantityUsed(), u.getUsageDate(), u.getActivityDescription()});
                }
            }
        });
        topPanel.add(refreshBtn);
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
        JButton refreshBtn = new JButton("Load Movements (Project ID)");
        refreshBtn.addActionListener(e -> {
            String projId = JOptionPane.showInputDialog("Enter Project ID:");
            if (projId != null && !projId.trim().isEmpty()) {
                model.setRowCount(0);
                List<MaterialStockMovementDTO> movements = stockController.getMovementsByProject(projId);
                for (MaterialStockMovementDTO m : movements) {
                    model.addRow(new Object[]{m.getMaterialName(), m.getMovementType(), m.getQuantity(), m.getReferenceId(), m.getMovementDate()});
                }
            }
        });
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }
}
