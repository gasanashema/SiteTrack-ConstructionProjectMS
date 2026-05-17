package view.materials;

import controller.MaterialController;
import dto.MaterialCategoryDTO;

import javax.swing.*;
import java.awt.*;

public class CategoryFormPanel extends JDialog {
    private MaterialController controller;
    private boolean isSaved = false;

    private JTextField nameField;
    private JTextArea descArea;

    public CategoryFormPanel(JFrame parent, MaterialController controller) {
        super(parent, "New Material Category", true);
        this.controller = controller;
        
        setSize(400, 300);
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
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Category Name *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
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
        saveBtn.addActionListener(e -> saveCategory());
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private void saveCategory() {
        String name = nameField.getText().trim();
        String desc = descArea.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MaterialCategoryDTO dto = new MaterialCategoryDTO();
        dto.setCategoryName(name);
        dto.setDescription(desc);

        if (controller.createCategory(dto)) {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Category created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
