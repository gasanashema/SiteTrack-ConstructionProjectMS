package view.workers;

import controller.WorkerController;
import dto.WorkerTypeDTO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class WorkerTypeFormPanel extends JDialog {
    private WorkerController controller;
    private WorkerTypeDTO workerType;
    private boolean isEditMode;
    private boolean isSaved = false;

    private JTextField typeNameField;
    private JTextField defaultDailyRateField;
    private JTextArea descriptionArea;

    public WorkerTypeFormPanel(JFrame parent, WorkerController controller) {
        super(parent, "New Worker Type", true);
        this.controller = controller;
        this.isEditMode = false;
        
        setSize(400, 280);
        setLocationRelativeTo(parent);
        
        initUI();
    }

    public WorkerTypeFormPanel(JFrame parent, WorkerController controller, WorkerTypeDTO workerType) {
        super(parent, "Edit Worker Type", true);
        this.controller = controller;
        this.workerType = workerType;
        this.isEditMode = true;
        
        setSize(400, 280);
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

        typeNameField = new JTextField();
        typeNameField.setPreferredSize(new Dimension(0, 35));

        defaultDailyRateField = new JTextField();
        defaultDailyRateField.setPreferredSize(new Dimension(0, 35));

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);

        int row = 0;
        addFormField(formPanel, "Type Name *", typeNameField, gbc, row++);
        addFormField(formPanel, "Default Daily Rate *", defaultDailyRateField, gbc, row++);
        
        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(descScroll, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        if (isEditMode) {
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setBackground(new Color(192, 57, 43));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.addActionListener(e -> deleteType());
            buttonPanel.add(deleteBtn);
        }

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveType());
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    
    private void addFormField(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Ubuntu", Font.BOLD, 12));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void populateData() {
        typeNameField.setText(workerType.getTypeName());
        if (workerType.getDefaultDailyRate() != null) {
            defaultDailyRateField.setText(workerType.getDefaultDailyRate().toString());
        }
        descriptionArea.setText(workerType.getDescription());
    }

    private void saveType() {
        String typeName = typeNameField.getText().trim();
        String rateStr = defaultDailyRateField.getText().trim();
        String desc = descriptionArea.getText().trim();

        if (typeName.isEmpty() || rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal rate;
        try {
            rate = new BigDecimal(rateStr);
            if (rate.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valid positive numbers are required for Daily Rate.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        WorkerTypeDTO dto = new WorkerTypeDTO();
        dto.setTypeName(typeName);
        dto.setDefaultDailyRate(rate);
        dto.setDescription(desc);

        if (isEditMode) {
            dto.setId(workerType.getId());
            if (controller.updateWorkerType(dto) != null) {
                isSaved = true;
                dispose();
            }
        } else {
            if (controller.createWorkerType(dto) != null) {
                isSaved = true;
                dispose();
            }
        }
    }
    
    private void deleteType() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this worker type?\nActive workers may prevent deletion.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteWorkerType(workerType.getId())) {
                isSaved = true;
                dispose();
            }
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
