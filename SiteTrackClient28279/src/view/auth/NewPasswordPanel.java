package view.auth;

import controller.AuthController;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class NewPasswordPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthController authController;
    private String userId;
    
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel errorLabel;
    private JButton saveButton;

    public NewPasswordPanel(MainFrame mainFrame, AuthController authController, String userId) {
        this.mainFrame = mainFrame;
        this.authController = authController;
        this.userId = userId;
        
        setLayout(new GridBagLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1, true),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JLabel title = new JLabel("Set New Password", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 22));
        title.setForeground(Color.decode("#1f242e"));
        formPanel.add(title, gbc);
        gbc.gridy++;
        
        JLabel subtitle = new JLabel("Enter a strong new password", SwingConstants.CENTER);
        subtitle.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitle, gbc);
        gbc.gridy++;
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        
        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        newPassLabel.setForeground(Color.decode("#1f242e"));
        formPanel.add(newPassLabel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        newPasswordField = new JPasswordField(20);
        newPasswordField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(newPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 10);
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        confirmPassLabel.setForeground(Color.decode("#1f242e"));
        formPanel.add(confirmPassLabel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(15, 0, 5, 0);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        saveButton = new JButton("Save Password");
        saveButton.setPreferredSize(new Dimension(0, 45));
        saveButton.setBackground(Color.decode("#FF5E14"));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> attemptSavePassword());
        formPanel.add(saveButton, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(errorLabel, gbc);
        
        add(formPanel);
    }
    
    private void attemptSavePassword() {
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());
        
        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            errorLabel.setText("Please fill out both fields.");
            return;
        }
        
        if (!newPass.equals(confirmPass)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }
        
        if (newPass.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters.");
            return;
        }
        
        saveButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authController.resetPassword(userId, newPass);
            }
            @Override
            protected void done() {
                saveButton.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(mainFrame, 
                            "Password successfully updated. You can now log in.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.switchPanel("LoginPanel");
                    } else {
                        errorLabel.setText("Failed to update password.");
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Error updating password.");
                }
            }
        };
        worker.execute();
    }
}
