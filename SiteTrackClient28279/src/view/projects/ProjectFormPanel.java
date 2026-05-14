package view.projects;

import controller.ProjectController;
import dto.ProjectDTO;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ProjectFormPanel extends JDialog {
    private ProjectDTO project;
    private ProjectController controller;
    private boolean isSaved = false;

    private JTextField nameField;
    private JTextField locationField;
    private JTextArea descArea;
    private JTextField startDateField;
    private JTextField endDateField;
    private JComboBox<String> statusCombo;
    private JLabel createdByLabel;
    private JLabel createdAtLabel;
    private JButton saveBtn;
    private JButton deleteBtn;
    private JButton manageManagersBtn;

    public ProjectFormPanel(JFrame parent, ProjectDTO project, ProjectController controller) {
        super(parent, project == null ? "New Project" : "Edit Project", true);
        this.project = project;
        this.controller = controller;
        
        setSize(550, 600);
        setLocationRelativeTo(parent);
        
        initUI();
        populateData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weightx = 1.0;

        nameField = createTextField();
        locationField = createTextField();
        
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        
        startDateField = createTextField();
        startDateField.putClientProperty("JTextField.placeholderText", "YYYY-MM-DD");
        
        endDateField = createTextField();
        endDateField.putClientProperty("JTextField.placeholderText", "YYYY-MM-DD");
        
        statusCombo = new JComboBox<>(new String[]{"PLANNING", "ONGOING", "COMPLETED", "CANCELLED"});
        statusCombo.setPreferredSize(new Dimension(0, 35));
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        createdByLabel = new JLabel("-");
        createdAtLabel = new JLabel("-");

        int row = 0;
        addFormField(formPanel, "Project Name *", nameField, gbc, row++);
        addFormField(formPanel, "Location *", locationField, gbc, row++);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(new JScrollPane(descArea), gbc);
        row++;

        addFormField(formPanel, "Start Date (YYYY-MM-DD) *", startDateField, gbc, row++);
        addFormField(formPanel, "Expected End Date *", endDateField, gbc, row++);
        addFormField(formPanel, "Status", statusCombo, gbc, row++);
        addFormField(formPanel, "Created By", createdByLabel, gbc, row++);
        addFormField(formPanel, "Created At", createdAtLabel, gbc, row++);

        mainPanel.add(new JScrollPane(formPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        deleteBtn = new JButton("Delete Project");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setVisible(false); // Only for EDIT mode and ADMIN
        deleteBtn.addActionListener(e -> deleteProject());
        
        manageManagersBtn = new JButton("Manage Managers");
        manageManagersBtn.setVisible(false);
        manageManagersBtn.addActionListener(e -> openManageManagers());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        saveBtn = new JButton("Save");
        saveBtn.setBackground(Color.decode("#FF5E14"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveProject());
        
        buttonPanel.add(manageManagersBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(Box.createHorizontalStrut(20)); // Spacer
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, 35));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return tf;
    }

    private void addFormField(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void populateData() {
        if (project == null) {
            // CREATE MODE
            statusCombo.setSelectedItem("PLANNING");
            createdByLabel.setText(SessionManager.getInstance().getCurrentUserName());
            createdAtLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            startDateField.setText(LocalDate.now().toString());
        } else {
            // EDIT MODE
            nameField.setText(project.getProjectName());
            locationField.setText(project.getLocation());
            descArea.setText(project.getDescription());
            startDateField.setText(project.getStartDate().toString());
            endDateField.setText(project.getExpectedEndDate().toString());
            statusCombo.setSelectedItem(project.getStatus());
            createdByLabel.setText(project.getCreatedByName());
            
            if (project.getCreatedAt() != null) {
                createdAtLabel.setText(project.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            }

            // Role check for Edit/Delete/Manage
            boolean isAdmin = SessionManager.getInstance().isAdmin();
            if (isAdmin) {
                deleteBtn.setVisible(true);
                manageManagersBtn.setVisible(true);
            }
        }
    }

    private void saveProject() {
        try {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String desc = descArea.getText().trim();
            LocalDate start = LocalDate.parse(startDateField.getText().trim());
            LocalDate end = LocalDate.parse(endDateField.getText().trim());
            String status = (String) statusCombo.getSelectedItem();

            ProjectDTO dto = project == null ? new ProjectDTO() : project;
            dto.setProjectName(name);
            dto.setLocation(location);
            dto.setDescription(desc);
            dto.setStartDate(start);
            dto.setExpectedEndDate(end);
            dto.setStatus(status);

            if (project == null) {
                ProjectDTO created = controller.createProject(dto);
                if (created != null) {
                    isSaved = true;
                    dispose();
                }
            } else {
                ProjectDTO updated = controller.updateProject(dto);
                if (updated != null) {
                    isSaved = true;
                    dispose();
                }
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Dates must be in YYYY-MM-DD format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProject() {
        if (project != null && project.getId() != null) {
            if (controller.deleteProject(project.getId())) {
                isSaved = true;
                dispose();
            }
        }
    }
    
    private void openManageManagers() {
        if (project != null && project.getId() != null) {
            AssignManagerPanel dialog = new AssignManagerPanel((JFrame) getOwner(), project.getId(), controller);
            dialog.setVisible(true);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
