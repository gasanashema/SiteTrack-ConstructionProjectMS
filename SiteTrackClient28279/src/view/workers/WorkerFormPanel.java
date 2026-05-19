package view.workers;

import controller.WorkerController;
import dto.SiteWorkerDTO;
import dto.WorkerTypeDTO;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class WorkerFormPanel extends JDialog {
    private WorkerController controller;
    private SiteWorkerDTO worker;
    private boolean isEditMode;
    private boolean isSaved = false;

    private JTextField fullNameField;
    private JTextField phoneField;
    private JComboBox<String> typeCombo;
    private JTextField dailyRateField;
    private JComboBox<String> statusCombo;
    
    private List<WorkerTypeDTO> workerTypes;

    public WorkerFormPanel(JFrame parent, WorkerController controller) {
        super(parent, "Register Worker", true);
        this.controller = controller;
        this.isEditMode = false;
        
        setSize(450, 320);
        setLocationRelativeTo(parent);
        
        initUI();
    }

    public WorkerFormPanel(JFrame parent, WorkerController controller, SiteWorkerDTO worker) {
        super(parent, "Edit Worker", true);
        this.controller = controller;
        this.worker = worker;
        this.isEditMode = true;
        
        setSize(450, 320);
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

        fullNameField = new JTextField();
        fullNameField.setPreferredSize(new Dimension(0, 35));

        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(0, 35));

        typeCombo = new JComboBox<>();
        workerTypes = controller.getAllWorkerTypes();
        for (WorkerTypeDTO t : workerTypes) {
            typeCombo.addItem(t.getId() + " - " + t.getTypeName());
        }
        typeCombo.setPreferredSize(new Dimension(0, 35));

        dailyRateField = new JTextField();
        dailyRateField.setPreferredSize(new Dimension(0, 35));

        statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        statusCombo.setPreferredSize(new Dimension(0, 35));

        typeCombo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED && !isEditMode) {
                int idx = typeCombo.getSelectedIndex();
                if (idx >= 0) {
                    WorkerTypeDTO t = workerTypes.get(idx);
                    if (t.getDefaultDailyRate() != null) {
                        dailyRateField.setText(t.getDefaultDailyRate().toString());
                    }
                }
            }
        });
        
        if (!isEditMode && workerTypes.size() > 0) {
            typeCombo.setSelectedIndex(0);
        }

        int row = 0;
        addFormField(formPanel, "Full Name *", fullNameField, gbc, row++);
        addFormField(formPanel, "Phone", phoneField, gbc, row++);
        addFormField(formPanel, "Worker Type *", typeCombo, gbc, row++);
        addFormField(formPanel, "Daily Rate *", dailyRateField, gbc, row++);
        
        if (isEditMode) {
            addFormField(formPanel, "Status *", statusCombo, gbc, row++);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        if (isEditMode && SessionManager.getInstance().isAdmin()) {
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setBackground(new Color(192, 57, 43));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.addActionListener(e -> deleteWorker());
            buttonPanel.add(deleteBtn);
        }

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveWorker());
        
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
        fullNameField.setText(worker.getFullName());
        phoneField.setText(worker.getPhone());
        
        for (int i = 0; i < workerTypes.size(); i++) {
            if (workerTypes.get(i).getId().equals(worker.getWorkerTypeId())) {
                typeCombo.setSelectedIndex(i);
                break;
            }
        }
        
        if (worker.getDailyRate() != null) {
            dailyRateField.setText(worker.getDailyRate().toString());
        }
        
        statusCombo.setSelectedItem(worker.getStatus());
    }

    private void saveWorker() {
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String rateStr = dailyRateField.getText().trim();

        if (fullName.isEmpty() || rateStr.isEmpty() || typeCombo.getSelectedItem() == null) {
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
        
        int typeIdx = typeCombo.getSelectedIndex();
        String typeId = workerTypes.get(typeIdx).getId();

        SiteWorkerDTO dto = new SiteWorkerDTO();
        dto.setFullName(fullName);
        dto.setPhone(phone);
        dto.setWorkerTypeId(typeId);
        dto.setDailyRate(rate);

        if (isEditMode) {
            dto.setId(worker.getId());
            dto.setStatus((String) statusCombo.getSelectedItem());
            if (controller.updateWorker(dto) != null) {
                isSaved = true;
                dispose();
            }
        } else {
            if (controller.createWorker(dto) != null) {
                isSaved = true;
                dispose();
            }
        }
    }
    
    private void deleteWorker() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this worker?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteWorker(worker.getId())) { // assuming delete is on controller
                isSaved = true;
                dispose();
            }
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
