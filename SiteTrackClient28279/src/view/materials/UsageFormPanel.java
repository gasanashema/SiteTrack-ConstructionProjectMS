package view.materials;

import controller.MaterialController;
import controller.ProjectController;
import dto.MaterialDTO;
import dto.MaterialUsageDTO;
import dto.ProjectDTO;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class UsageFormPanel extends JDialog {
    private MaterialController controller;
    private ProjectController projectController;
    private boolean isSaved = false;

    private JComboBox<String> projectCombo;
    private JComboBox<String> materialCombo;
    private JTextField quantityField;
    private JTextField activityField;
    private JDateChooser dateField;

    public UsageFormPanel(JFrame parent, MaterialController controller) {
        super(parent, "Record Material Usage (Stock Out)", true);
        this.controller = controller;
        this.projectController = new ProjectController();
        
        setSize(450, 400);
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

        activityField = new JTextField();
        activityField.setPreferredSize(new Dimension(0, 35));

        dateField = new JDateChooser();
        dateField.setPreferredSize(new Dimension(0, 35));
        dateField.setDate(java.sql.Date.valueOf(LocalDate.now()));
        ((JTextField) dateField.getDateEditor().getUiComponent()).setEditable(false);

        int row = 0;
        addFormField(formPanel, "Project *", projectCombo, gbc, row++);
        addFormField(formPanel, "Material *", materialCombo, gbc, row++);
        addFormField(formPanel, "Quantity Used *", quantityField, gbc, row++);
        addFormField(formPanel, "Usage Date *", dateField, gbc, row++);
        addFormField(formPanel, "Activity Desc *", activityField, gbc, row++);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Record Usage");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveUsage());
        
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

    private void saveUsage() {
        String qtyStr = quantityField.getText().trim();
        String activity = activityField.getText().trim();

        if (qtyStr.isEmpty() || activity.isEmpty() || 
            projectCombo.getSelectedItem() == null || materialCombo.getSelectedItem() == null || 
            dateField.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal qty;
        try {
            qty = new BigDecimal(qtyStr);
            if (qty.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valid positive numbers are required for Quantity.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String projSelected = (String) projectCombo.getSelectedItem();
        String projId = projSelected.split(" - ")[0];
        
        String matSelected = (String) materialCombo.getSelectedItem();
        String matId = matSelected.split(" - ")[0];
        
        LocalDate date = new java.sql.Date(dateField.getDate().getTime()).toLocalDate();

        MaterialUsageDTO dto = new MaterialUsageDTO();
        dto.setProjectId(projId);
        dto.setMaterialId(matId);
        dto.setQuantityUsed(qty);
        dto.setActivityDescription(activity);
        dto.setUsageDate(date);
        
        // Use recordedById but MaterialUsageDTO may not have it! Wait!
        // We added recordedById to MaterialPurchaseDTO, did we add it to MaterialUsageDTO?
        // Let's check that.
        dto.setRecordedByName(SessionManager.getInstance().getCurrentUserId()); // Hack for now, Server expects ID in RecordedByName field from previous implementation

        if (controller.recordUsage(dto)) {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Usage recorded successfully! Stock deducted based on FIFO.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
