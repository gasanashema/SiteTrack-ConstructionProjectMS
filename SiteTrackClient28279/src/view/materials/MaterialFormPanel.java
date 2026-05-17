package view.materials;

import controller.MaterialController;
import dto.MaterialCategoryDTO;
import dto.MaterialDTO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MaterialFormPanel extends JDialog {
    private MaterialController controller;
    private boolean isSaved = false;

    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private List<MaterialCategoryDTO> categories;
    private JTextField unitField;
    private JTextField priceField;
    private JTextArea descArea;

    public MaterialFormPanel(JFrame parent, MaterialController controller) {
        super(parent, "Define New Material", true);
        this.controller = controller;
        
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

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(0, 35));
        
        categoryCombo = new JComboBox<>();
        categories = controller.getAllCategories();
        for (MaterialCategoryDTO c : categories) {
            categoryCombo.addItem(c.getId() + " - " + c.getCategoryName());
        }
        categoryCombo.setPreferredSize(new Dimension(0, 35));

        unitField = new JTextField();
        unitField.setPreferredSize(new Dimension(0, 35));
        
        priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(0, 35));

        descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        int row = 0;
        addFormField(formPanel, "Material Name *", nameField, gbc, row++);
        addFormField(formPanel, "Category *", categoryCombo, gbc, row++);
        addFormField(formPanel, "Unit (e.g., kg, pcs) *", unitField, gbc, row++);
        addFormField(formPanel, "Initial Unit Price *", priceField, gbc, row++);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(new JScrollPane(descArea), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveMaterial());
        
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

    private void saveMaterial() {
        String name = nameField.getText().trim();
        String unit = unitField.getText().trim();
        String priceStr = priceField.getText().trim();
        String desc = descArea.getText().trim();

        if (name.isEmpty() || unit.isEmpty() || priceStr.isEmpty() || categoryCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valid positive price is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String catSelected = (String) categoryCombo.getSelectedItem();
        String catId = catSelected.split(" - ")[0];

        MaterialDTO dto = new MaterialDTO();
        dto.setMaterialName(name);
        dto.setCategoryId(catId);
        dto.setUnit(unit);
        dto.setCurrentPrice(price);
        dto.setDescription(desc);

        if (controller.createMaterial(dto)) {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Material defined successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
