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
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class ProjectFormPanel extends javax.swing.JDialog {
    private ProjectDTO project;
    private ProjectController controller;
    private boolean isSaved = false;

    public ProjectFormPanel(JFrame parent, ProjectDTO project, ProjectController controller) {
        super(parent, project == null ? "New Project" : "Edit Project", true);
        this.project = project;
        this.controller = controller;
        
        initComponents();
        
        // Custom styling that cannot be represented in the visual editor properties
        descArea.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        
        setMinimumSize(new Dimension(650, 600));
        pack();
        setLocationRelativeTo(parent);
        
        populateData();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        mainScrollPane = new javax.swing.JScrollPane();
        formPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationField = new javax.swing.JTextField();
        descLabel = new javax.swing.JLabel();
        descScrollPane = new javax.swing.JScrollPane();
        descArea = new javax.swing.JTextArea();
        startDateLabel = new javax.swing.JLabel();
        startDateField = new com.toedter.calendar.JDateChooser();
        endDateLabel = new javax.swing.JLabel();
        endDateField = new com.toedter.calendar.JDateChooser();
        statusLabel = new javax.swing.JLabel();
        statusCombo = new javax.swing.JComboBox<>();
        createdByTitleLabel = new javax.swing.JLabel();
        createdByLabel = new javax.swing.JLabel();
        createdAtTitleLabel = new javax.swing.JLabel();
        createdAtLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        leftButtons = new javax.swing.JPanel();
        manageManagersBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        rightButtons = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 600));

        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setLayout(new java.awt.BorderLayout(10, 10));

        formPanel.setLayout(new java.awt.GridBagLayout());

        nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nameLabel.setText("Project Name *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(nameLabel, gridBagConstraints);

        nameField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameField.setPreferredSize(new java.awt.Dimension(0, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(nameField, gridBagConstraints);

        locationLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        locationLabel.setText("Location *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(locationLabel, gridBagConstraints);

        locationField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        locationField.setPreferredSize(new java.awt.Dimension(0, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(locationField, gridBagConstraints);

        descLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        descLabel.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(descLabel, gridBagConstraints);

        descArea.setColumns(20);
        descArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        descArea.setLineWrap(true);
        descArea.setRows(4);
        descArea.setWrapStyleWord(true);
        descScrollPane.setViewportView(descArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(descScrollPane, gridBagConstraints);

        startDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        startDateLabel.setText("Start Date *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(startDateLabel, gridBagConstraints);

        startDateField.setDateFormatString("yyyy-MM-dd");
        startDateField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        startDateField.setPreferredSize(new java.awt.Dimension(250, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(startDateField, gridBagConstraints);

        endDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        endDateLabel.setText("Expected End Date *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(endDateLabel, gridBagConstraints);

        endDateField.setDateFormatString("yyyy-MM-dd");
        endDateField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        endDateField.setPreferredSize(new java.awt.Dimension(250, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(endDateField, gridBagConstraints);

        statusLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        statusLabel.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(statusLabel, gridBagConstraints);

        statusCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        statusCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PLANNING", "ONGOING", "COMPLETED", "CANCELLED" }));
        statusCombo.setPreferredSize(new java.awt.Dimension(0, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(statusCombo, gridBagConstraints);

        createdByTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        createdByTitleLabel.setText("Created By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(createdByTitleLabel, gridBagConstraints);

        createdByLabel.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(createdByLabel, gridBagConstraints);

        createdAtTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        createdAtTitleLabel.setText("Created At");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(createdAtTitleLabel, gridBagConstraints);

        createdAtLabel.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        formPanel.add(createdAtLabel, gridBagConstraints);

        mainScrollPane.setViewportView(formPanel);

        mainPanel.add(mainScrollPane, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.BorderLayout());

        leftButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        manageManagersBtn.setText("Manage Managers");
        manageManagersBtn.setVisible(false);
        manageManagersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageManagersBtnActionPerformed(evt);
            }
        });
        leftButtons.add(manageManagersBtn);

        deleteBtn.setForeground(new java.awt.Color(255, 0, 0));
        deleteBtn.setText("Delete Project");
        deleteBtn.setVisible(false);
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });
        leftButtons.add(deleteBtn);

        buttonPanel.add(leftButtons, java.awt.BorderLayout.WEST);

        rightButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        rightButtons.add(cancelBtn);

        saveBtn.setBackground(new java.awt.Color(255, 94, 20));
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("Save Project");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        rightButtons.add(saveBtn);

        buttonPanel.add(rightButtons, java.awt.BorderLayout.EAST);

        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    // </editor-fold>//GEN-END:initComponents

    private void manageManagersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageManagersBtnActionPerformed
        openManageManagers();
    }//GEN-LAST:event_manageManagersBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        deleteProject();
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        saveProject();
    }//GEN-LAST:event_saveBtnActionPerformed

    private void populateData() {
        if (project == null) {
            // CREATE MODE
            statusCombo.setSelectedItem("PLANNING");
            createdByLabel.setText(SessionManager.getInstance().getCurrentUserId());
            createdAtLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            startDateField.setDate(java.sql.Date.valueOf(LocalDate.now()));
        } else {
            // EDIT MODE
            nameField.setText(project.getProjectName());
            locationField.setText(project.getLocation());
            descArea.setText(project.getDescription());
            if (project.getStartDate() != null) {
                startDateField.setDate(java.sql.Date.valueOf(project.getStartDate()));
            }
            if (project.getExpectedEndDate() != null) {
                endDateField.setDate(java.sql.Date.valueOf(project.getExpectedEndDate()));
            }
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
            saveBtn.setText("Update Project");
        }
        
        if (startDateField.getDateEditor().getUiComponent() instanceof JTextField) {
            ((JTextField) startDateField.getDateEditor().getUiComponent()).setEditable(false);
        }
        if (endDateField.getDateEditor().getUiComponent() instanceof JTextField) {
            ((JTextField) endDateField.getDateEditor().getUiComponent()).setEditable(false);
        }
    }

    private void saveProject() {
        try {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String desc = descArea.getText().trim();
            
            if (name.length() < 2) {
                JOptionPane.showMessageDialog(this, "Project name must be at least 2 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (name.matches("^[0-9]+$")) {
                JOptionPane.showMessageDialog(this, "Project name cannot only contain numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (startDateField.getDate() == null || endDateField.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select both Start Date and End Date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate start = new java.sql.Date(startDateField.getDate().getTime()).toLocalDate();
            LocalDate end = new java.sql.Date(endDateField.getDate().getTime()).toLocalDate();
            String status = (String) statusCombo.getSelectedItem();

            ProjectDTO dto = project == null ? new ProjectDTO() : project;
            dto.setProjectName(name);
            dto.setLocation(location);
            dto.setDescription(desc);
            dto.setStartDate(start);
            dto.setExpectedEndDate(end);
            dto.setStatus(status);

            if (project == null) {
                dto.setCreatedByName(SessionManager.getInstance().getCurrentUserId());
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    // Variables declaration - do not modify//GEN-BEGIN:initComponents
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel createdAtLabel;
    private javax.swing.JLabel createdAtTitleLabel;
    private javax.swing.JLabel createdByLabel;
    private javax.swing.JLabel createdByTitleLabel;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTextArea descArea;
    private javax.swing.JLabel descLabel;
    private javax.swing.JScrollPane descScrollPane;
    private com.toedter.calendar.JDateChooser endDateField;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel leftButtons;
    private javax.swing.JTextField locationField;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JButton manageManagersBtn;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel rightButtons;
    private javax.swing.JButton saveBtn;
    private com.toedter.calendar.JDateChooser startDateField;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JComboBox<String> statusCombo;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:initComponents
}
