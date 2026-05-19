package view.materials;

import controller.MaterialController;
import controller.StockController;
import dto.MaterialDTO;
import dto.ProjectMaterialStockDTO;
import session.SessionManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class StockAdjustmentDialog extends JDialog {
    private StockController stockController;
    private MaterialController materialController;
    private String projectId;
    private boolean isSaved = false;

    private JComboBox<String> materialCombo;
    private JLabel currentQtyLabel;
    private JTextField newQtyField;
    private JLabel differenceLabel;
    private JTextArea descriptionArea;
    private BigDecimal currentStock = BigDecimal.ZERO;

    public StockAdjustmentDialog(JFrame parent, StockController stockController, MaterialController materialController, String projectId) {
        super(parent, "Physical Stock Adjustment", true);
        this.stockController = stockController;
        this.materialController = materialController;
        this.projectId = projectId;
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        materialCombo = new JComboBox<>();
        List<MaterialDTO> materials = materialController.getActiveMaterials();
        for (MaterialDTO m : materials) {
            materialCombo.addItem(m.getId() + " - " + m.getMaterialName());
        }
        materialCombo.setPreferredSize(new Dimension(0, 35));
        
        currentQtyLabel = new JLabel("0.00");
        currentQtyLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        currentQtyLabel.setForeground(new Color(41, 128, 185));

        newQtyField = new JTextField();
        newQtyField.setPreferredSize(new Dimension(0, 35));

        differenceLabel = new JLabel("0.00");
        differenceLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);

        materialCombo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateCurrentStock();
                calculateDifference();
            }
        });

        newQtyField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { calculateDifference(); }
            public void removeUpdate(DocumentEvent e) { calculateDifference(); }
            public void insertUpdate(DocumentEvent e) { calculateDifference(); }
        });

        int row = 0;
        addFormField(formPanel, "Material *", materialCombo, gbc, row++);
        addFormField(formPanel, "Current Quantity", currentQtyLabel, gbc, row++);
        addFormField(formPanel, "New Quantity *", newQtyField, gbc, row++);
        addFormField(formPanel, "Difference", differenceLabel, gbc, row++);
        
        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Description *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(descScroll, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        updateCurrentStock();

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Record Adjustment");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveAdjustment());
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    
    private void addFormField(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void updateCurrentStock() {
        if (materialCombo.getSelectedItem() != null) {
            String matSelected = (String) materialCombo.getSelectedItem();
            String matId = matSelected.split(" - ")[0];
            
            ProjectMaterialStockDTO stock = stockController.getStockByProjectAndMaterial(projectId, matId);
            if (stock != null && stock.getQuantityAvailable() != null) {
                currentStock = stock.getQuantityAvailable();
                currentQtyLabel.setText(currentStock.toString() + " " + stock.getUnit());
            } else {
                currentStock = BigDecimal.ZERO;
                currentQtyLabel.setText("0.00");
            }
        }
    }

    private void calculateDifference() {
        String newStr = newQtyField.getText().trim();
        if (newStr.isEmpty()) {
            differenceLabel.setText("0.00");
            differenceLabel.setForeground(Color.BLACK);
            return;
        }

        try {
            BigDecimal newQty = new BigDecimal(newStr);
            BigDecimal diff = newQty.subtract(currentStock);
            
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                differenceLabel.setText("+" + diff.toString());
                differenceLabel.setForeground(new Color(39, 174, 96)); // Green
            } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
                differenceLabel.setText(diff.toString());
                differenceLabel.setForeground(new Color(192, 57, 43)); // Red
            } else {
                differenceLabel.setText("0.00");
                differenceLabel.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            differenceLabel.setText("Invalid input");
            differenceLabel.setForeground(Color.RED);
        }
    }

    private void saveAdjustment() {
        String qtyStr = newQtyField.getText().trim();
        String desc = descriptionArea.getText().trim();

        if (qtyStr.isEmpty() || desc.isEmpty() || materialCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal newQty;
        try {
            newQty = new BigDecimal(qtyStr);
            if (newQty.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valid non-negative number required for New Quantity.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String matSelected = (String) materialCombo.getSelectedItem();
        String matId = matSelected.split(" - ")[0];

        if (stockController.recordStockAdjustment(projectId, matId, newQty, desc, SessionManager.getInstance().getCurrentUserId())) {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Stock adjusted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
