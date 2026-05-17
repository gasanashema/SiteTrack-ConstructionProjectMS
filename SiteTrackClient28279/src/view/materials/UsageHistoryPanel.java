package view.materials;

import controller.MaterialController;
import controller.ProjectController;
import controller.StockController;
import dto.MaterialUsageDTO;
import dto.ProjectDTO;
import view.MainFrame;
import view.dashboard.KpiCard;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UsageHistoryPanel extends JPanel {
    private MainFrame mainFrame;
    private MaterialController materialController;
    private ProjectController projectController;
    private JComboBox<String> projectCombo;
    private JDateChooser fromDateField;
    private JDateChooser toDateField;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    
    private KpiCard totalQtyCard;
    private KpiCard totalCostCard;
    private KpiCard avgCostCard;
    private KpiCard numUsagesCard;

    public UsageHistoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.materialController = new MaterialController();
        this.projectController = new ProjectController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(UIManager.getColor("Panel.background"));
        
        JLabel titleLabel = new JLabel("Material Usage History & Summary");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        topHeaderPanel.add(titleLabel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(UIManager.getColor("Panel.background"));
        
        filterPanel.add(new JLabel("Project: "));
        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }
        filterPanel.add(projectCombo);

        filterPanel.add(new JLabel("From:"));
        fromDateField = new JDateChooser();
        fromDateField.setPreferredSize(new Dimension(120, 25));
        fromDateField.setDate(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
        filterPanel.add(fromDateField);

        filterPanel.add(new JLabel("To:"));
        toDateField = new JDateChooser();
        toDateField.setPreferredSize(new Dimension(120, 25));
        toDateField.setDate(java.sql.Date.valueOf(LocalDate.now()));
        filterPanel.add(toDateField);

        JButton filterBtn = new JButton("Apply Filters");
        filterBtn.addActionListener(e -> refreshData());
        filterPanel.add(filterBtn);

        topHeaderPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topHeaderPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));
        tabbedPane.addTab("Summary (KPIs)", createSummaryTab());
        tabbedPane.addTab("History Table", createTableTab());

        add(tabbedPane, BorderLayout.CENTER);
        
        // Initial load
        refreshData();
    }

    private JPanel createSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel kpiPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        
        totalQtyCard = new KpiCard("Total Quantity Used", "0", "Total usage", 0);
        totalCostCard = new KpiCard("Total Cost", "$0.00", "Total value used", 2);
        avgCostCard = new KpiCard("Average Unit Cost", "$0.00", "Cost per unit", 1);
        numUsagesCard = new KpiCard("Number of Usages", "0", "Total records", 3);
        
        kpiPanel.add(totalQtyCard);
        kpiPanel.add(totalCostCard);
        kpiPanel.add(avgCostCard);
        kpiPanel.add(numUsagesCard);

        panel.add(kpiPanel, BorderLayout.NORTH);
        
        // Add a placeholder for future charts
        JLabel placeholder = new JLabel("Chart visualization will appear here.", SwingConstants.CENTER);
        placeholder.setFont(new Font("Ubuntu", Font.ITALIC, 14));
        placeholder.setForeground(Color.GRAY);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTableTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Project", "Material", "Qty Used", "Total Cost", "Date", "Recorded By"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        historyTable = new JTable(tableModel);
        
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = historyTable.getSelectedRow();
                    if (row != -1) {
                        String usageId = (String) tableModel.getValueAt(row, 0);
                        openViewDialog(usageId);
                    }
                }
            }
        });

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        return panel;
    }

    private void refreshData() {
        if (projectCombo.getSelectedItem() == null || fromDateField.getDate() == null || toDateField.getDate() == null) {
            return;
        }

        String projSelected = (String) projectCombo.getSelectedItem();
        String projId = projSelected.split(" - ")[0];
        
        LocalDate from = new java.sql.Date(fromDateField.getDate().getTime()).toLocalDate();
        LocalDate to = new java.sql.Date(toDateField.getDate().getTime()).toLocalDate();

        // Let's use getUsageByProject since getUsageByProjectAndDateRange requires backend method
        List<MaterialUsageDTO> usages = materialController.getUsageByProject(projId);
        
        // Filter locally by date
        usages = usages.stream()
            .filter(u -> !u.getUsageDate().isBefore(from) && !u.getUsageDate().isAfter(to))
            .collect(Collectors.toList());

        // Update Table
        tableModel.setRowCount(0);
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (MaterialUsageDTO u : usages) {
            tableModel.addRow(new Object[]{
                u.getId(), u.getProjectName(), u.getMaterialName(), 
                u.getQuantityUsed(), u.getTotalCost(), u.getUsageDate(), u.getRecordedByName()
            });
            totalQty = totalQty.add(u.getQuantityUsed());
            totalCost = totalCost.add(u.getTotalCost());
        }

        // Update KPIs
        totalQtyCard.setValue(totalQty.toString());
        totalCostCard.setValue(String.format("$%.2f", totalCost));
        numUsagesCard.setValue(String.valueOf(usages.size()));
        
        if (usages.size() > 0 && totalQty.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgCost = totalCost.divide(totalQty, 2, BigDecimal.ROUND_HALF_UP);
            avgCostCard.setValue(String.format("$%.2f", avgCost));
        } else {
            avgCostCard.setValue("$0.00");
        }
    }

    private void openViewDialog(String usageId) {
        String projId = ((String) projectCombo.getSelectedItem()).split(" - ")[0];
        List<MaterialUsageDTO> usages = materialController.getUsageByProject(projId);
        MaterialUsageDTO target = usages.stream().filter(u -> u.getId().equals(usageId)).findFirst().orElse(null);
        
        if (target != null) {
            UsageFormDialog dialog = new UsageFormDialog(mainFrame, materialController, target);
            dialog.setVisible(true);
        }
    }
}
