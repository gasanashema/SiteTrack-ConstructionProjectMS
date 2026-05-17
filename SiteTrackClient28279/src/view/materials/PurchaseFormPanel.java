package view.materials;

import controller.MaterialController;
import controller.ProjectController;
import dto.MaterialDTO;
import dto.MaterialPurchaseDTO;
import dto.ProjectDTO;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class PurchaseFormPanel extends JDialog {
    private MaterialController controller;
    private ProjectController projectController;
    private boolean isSaved = false;

    private JComboBox<String> projectCombo;
    private JComboBox<String> materialCombo;
    private JTextField quantityField;
    private JTextField unitPriceField;
    private JTextField supplierField;
    private JDateChooser dateField;

    public PurchaseFormPanel(JFrame parent, MaterialController controller) {
        super(parent, "Record Material Purchase", true);
        this.controller = controller;
        this.projectController = new ProjectController();
        
        setSize(450, 450);
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

        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }
        projectCombo.setPreferredSize(new Dimension(0, 35));

        materialCombo = new JComboBox<>();
        List<MaterialDTO> materials = controller.getActiveMaterials();
        for (MaterialDTO m : materials) {
            materialCombo.addItem(m.getId() + " - " + m.getMaterialName());
        }
        materialCombo.setPreferredSize(new Dimension(0, 35));

        quantityField = new JTextField();
        quantityField.setPreferredSize(new Dimension(0, 35));
        
        unitPriceField = new JTextField();
        unitPriceField.setPreferredSize(new Dimension(0, 35));

        supplierField = new JTextField();
        supplierField.setPreferredSize(new Dimension(0, 35));

        dateField = new JDateChooser();
        dateField.setPreferredSize(new Dimension(0, 35));
        dateField.setDate(java.sql.Date.valueOf(LocalDate.now()));
        ((JTextField) dateField.getDateEditor().getUiComponent()).setEditable(false);

        int row = 0;
        addFormField(formPanel, "Project *", projectCombo, gbc, row++);
        addFormField(formPanel, "Material *", materialCombo, gbc, row++);
        addFormField(formPanel, "Quantity *", quantityField, gbc, row++);
        addFormField(formPanel, "Unit Price *", unitPriceField, gbc, row++);
        addFormField(formPanel, "Supplier Name *", supplierField, gbc, row++);
        addFormField(formPanel, "Purchase Date *", dateField, gbc, row++);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Record Purchase");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> savePurchase());
        
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

    private void savePurchase() {
        String qtyStr = quantityField.getText().trim();
        String priceStr = unitPriceField.getText().trim();
        String supplier = supplierField.getText().trim();

        if (qtyStr.isEmpty() || priceStr.isEmpty() || supplier.isEmpty() || 
            projectCombo.getSelectedItem() == null || materialCombo.getSelectedItem() == null || 
            dateField.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal qty, price;
        try {
            qty = new BigDecimal(qtyStr);
            price = new BigDecimal(priceStr);
            if (qty.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valid positive numbers are required for Quantity and Price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String projSelected = (String) projectCombo.getSelectedItem();
        String projId = projSelected.split(" - ")[0];
        
        String matSelected = (String) materialCombo.getSelectedItem();
        String matId = matSelected.split(" - ")[0];
        
        LocalDate date = new java.sql.Date(dateField.getDate().getTime()).toLocalDate();

        MaterialPurchaseDTO dto = new MaterialPurchaseDTO();
        dto.setProjectId(projId);
        dto.setMaterialId(matId);
        dto.setQuantity(qty);
        dto.setUnitPrice(price);
        dto.setTotalPrice(qty.multiply(price));
        dto.setSupplierName(supplier);
        dto.setPurchaseDate(date);
        dto.setRecordedByName(SessionManager.getInstance().getCurrentUserName());

        if (controller.recordPurchase(dto)) {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Purchase recorded successfully! Stock updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
