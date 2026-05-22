package view.settings;

import controller.UserController;
import dto.UserDTO;

import javax.swing.*;
import java.awt.*;

public class UserFormPanel extends JDialog {
    private UserController userController;
    private UserDTO user;
    private UserManagementPanel parentPanel;
    private boolean isEditMode;

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> roleCombo;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> statusCombo;
    private JLabel errorLabel;

    public UserFormPanel(Window owner, UserController userController, UserDTO user, UserManagementPanel parentPanel) {
        super(owner, user == null ? "Create New User" : "Edit User", ModalityType.APPLICATION_MODAL);
        this.userController = userController;
        this.user = user;
        this.parentPanel = parentPanel;
        this.isEditMode = (user != null);

        setSize(500, isEditMode ? 550 : 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIManager.getColor("Panel.background"));

        initUI();
        if (isEditMode) {
            populateData();
        }
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(UIManager.getColor("Panel.background"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        fullNameField = new JTextField(20);
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        roleCombo = new JComboBox<>(new String[]{"SITE_MANAGER", "ADMIN"});
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Ubuntu", Font.ITALIC, 12));

        int y = 0;
        addFormField(formPanel, gbc, "Full Name *", fullNameField, y++);
        addFormField(formPanel, gbc, "Username *", usernameField, y++);
        addFormField(formPanel, gbc, "Email *", emailField, y++);
        addFormField(formPanel, gbc, "Phone", phoneField, y++);
        addFormField(formPanel, gbc, "Role *", roleCombo, y++);
        
        if (isEditMode) {
            usernameField.setEditable(false);
            addFormField(formPanel, gbc, "Status *", statusCombo, y++);
            addFormField(formPanel, gbc, "New Password (optional)", passwordField, y++);
        } else {
            addFormField(formPanel, gbc, "Password *", passwordField, y++);
        }
        
        addFormField(formPanel, gbc, "Confirm Password", confirmPasswordField, y++);

        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2;
        formPanel.add(errorLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Action Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.decode("#2ecc71"));
        saveButton.setForeground(Color.WHITE);
        
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0.3;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Ubuntu", Font.BOLD, 14));
        panel.add(l, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void populateData() {
        fullNameField.setText(user.getFullName());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone() != null ? user.getPhone() : "");
        roleCombo.setSelectedItem(user.getRole());
        statusCombo.setSelectedItem(user.getStatus());
    }

    private void saveUser() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        String pass = new String(passwordField.getPassword());
        String conf = new String(confirmPasswordField.getPassword());
        
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            errorLabel.setText("Please fill all required fields (*).");
            return;
        }
        
        if (!phone.isEmpty() && (!phone.matches("\\d+") || phone.length() > 10)) {
            errorLabel.setText("Phone must be numbers only and max 10 digits.");
            return;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorLabel.setText("Invalid email format.");
            return;
        }

        if (!isEditMode) {
            if (pass.isEmpty()) {
                errorLabel.setText("Password is required for new users.");
                return;
            }
            if (userController.checkUsernameExists(username)) {
                errorLabel.setText("Username already exists.");
                return;
            }
            if (userController.checkEmailExists(email)) {
                errorLabel.setText("Email already exists.");
                return;
            }
        }

        if (!pass.isEmpty()) {
            if (pass.length() < 8) {
                errorLabel.setText("Password must be at least 8 characters.");
                return;
            }
            if (!pass.equals(conf)) {
                errorLabel.setText("Passwords do not match.");
                return;
            }
        }

        if (isEditMode) {
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);
            user.setStatus((String) statusCombo.getSelectedItem());
            if (!pass.isEmpty()) {
                userController.resetPassword(user.getId(), pass);
            }
            
            if (userController.updateUser(user) != null) {
                parentPanel.loadData();
                dispose();
            }
        } else {
            UserDTO newUser = new UserDTO();
            newUser.setFullName(fullName);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setRole(role);
            UserDTO createdUser = userController.createUser(newUser);
            if (createdUser != null) {
                if (!pass.isEmpty()) {
                    userController.resetPassword(createdUser.getId(), pass);
                }
                parentPanel.loadData();
                dispose();
            }
        }
    }
}
