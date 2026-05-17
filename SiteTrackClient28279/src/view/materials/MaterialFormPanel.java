package view.materials;

import controller.MaterialController;
import dto.MaterialCategoryDTO;
import dto.MaterialDTO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MaterialFormPanel extends JDialog {
    private MaterialDTO material;
    private MaterialController controller;
    private boolean isSaved = false;

    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private List<MaterialCategoryDTO> categories;
    private JTextField unitField;
    private JTextArea descArea;

    public MaterialFormPanel(JFrame parent, MaterialDTO material, MaterialController controller) {
        super(parent, material == null ? "Define New Material" : "Edit Material", true);
        this.material = material;
        this.controller = controller;
        
        setSize(450, 450);
        setLocationRelativeTo(parent);
        
        initUI();
        populateData();
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
        unitField.setEditable(false);
        
        categoryCombo.addActionListener(e -> {
            int idx = categoryCombo.getSelectedIndex();
            if (idx >= 0 && idx < categories.size()) {
                String catUnit = categories.get(idx).getUnit();
                unitField.setText(catUnit != null ? catUnit : "");
            }
        });
        // Trigger for initial selection
        if (!categories.isEmpty()) {
            categoryCombo.setSelectedIndex(0);
        }

        descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        int row = 0;
        addFormField(formPanel, "Material Name *", nameField, gbc, row++);
        addFormField(formPanel, "Category *", categoryCombo, gbc, row++);
        addFormField(formPanel, "Unit *", unitField, gbc, row++);
        
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

    private void populateData() {
        if (material != null) {
            nameField.setText(material.getMaterialName());
            
            // Find and select category
            for (int i = 0; i < categoryCombo.getItemCount(); i++) {
                if (categoryCombo.getItemAt(i).startsWith(material.getCategoryId() + " -")) {
                    categoryCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            unitField.setText(material.getUnit());
            descArea.setText(material.getDescription());
        }
    }

    private void saveMaterial() {
        String name = nameField.getText().trim();
        String unit = unitField.getText().trim();
        String desc = descArea.getText().trim();

        if (name.isEmpty() || unit.isEmpty() || categoryCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String catSelected = (String) categoryCombo.getSelectedItem();
        String catId = catSelected.split(" - ")[0];

        MaterialDTO dto = material == null ? new MaterialDTO() : material;
        dto.setMaterialName(name);
        dto.setCategoryId(catId);
        dto.setUnit(unit);
        dto.setDescription(desc);

        boolean success;
        if (material == null) {
            success = controller.createMaterial(dto);
        } else {
            success = controller.updateMaterial(dto);
        }

        if (success) {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Material saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
