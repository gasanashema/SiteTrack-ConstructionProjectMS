package view.auth;

import controller.AuthController;
import dto.LoginResponseDTO;
import view.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthController authController;
    private boolean forgotPasswordPanelAdded = false;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authController = new AuthController();
        
        initComponents();
        
        // Dynamically load and scale logo image if it exists
        try {
            URL logoUrl = getClass().getResource("/resources/logo_colored.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image img = originalIcon.getImage();
                Image resizedImg = img.getScaledInstance(180, -1, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(resizedImg));
            }
        } catch (Exception e) {}
        
        // Dynamic compound border
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1, true),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        formPanel = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        subtitle = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        forgotPasswordLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        formPanel.setLayout(new java.awt.GridBagLayout());

        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        formPanel.add(logoLabel, gridBagConstraints);

        title.setFont(new java.awt.Font("Ubuntu", 1, 22)); // NOI18N
        title.setText("SiteTrack Construction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        formPanel.add(title, gridBagConstraints);

        subtitle.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        subtitle.setForeground(java.awt.Color.GRAY);
        subtitle.setText("Login to your account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 0);
        formPanel.add(subtitle, gridBagConstraints);

        userLabel.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        userLabel.setText("Username:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        formPanel.add(userLabel, gridBagConstraints);

        usernameField.setColumns(20);
        usernameField.setPreferredSize(new java.awt.Dimension(250, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        formPanel.add(usernameField, gridBagConstraints);

        passLabel.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        passLabel.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 5, 10);
        formPanel.add(passLabel, gridBagConstraints);

        passwordField.setColumns(20);
        passwordField.setPreferredSize(new java.awt.Dimension(250, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 5, 0);
        formPanel.add(passwordField, gridBagConstraints);

        loginButton.setBackground(new java.awt.Color(255, 94, 20));
        loginButton.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new java.awt.Dimension(0, 45));
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 10, 0);
        formPanel.add(loginButton, gridBagConstraints);

        forgotPasswordLabel.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        forgotPasswordLabel.setForeground(new java.awt.Color(0, 86, 179));
        forgotPasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        forgotPasswordLabel.setText("Forgot Password?");
        forgotPasswordLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forgotPasswordLabelMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        formPanel.add(forgotPasswordLabel, gridBagConstraints);

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        errorLabel.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        formPanel.add(errorLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = -1;
        gridBagConstraints.gridy = -1;
        add(formPanel, gridBagConstraints);
    }
    // </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void forgotPasswordLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPasswordLabelMouseClicked
        if (!forgotPasswordPanelAdded) {
            mainFrame.addPanel("ForgotPasswordPanel", new ForgotPasswordPanel(mainFrame, authController));
            forgotPasswordPanelAdded = true;
        }
        mainFrame.switchPanel("ForgotPasswordPanel");
    }//GEN-LAST:event_forgotPasswordLabelMouseClicked
    
    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        errorLabel.setForeground(Color.decode("#FF5E14"));
        errorLabel.setText("Authenticating and sending OTP... Please wait.");
        loginButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<LoginResponseDTO, Void> worker = new SwingWorker<LoginResponseDTO, Void>() {
            @Override
            protected LoginResponseDTO doInBackground() throws Exception {
                return authController.attemptLogin(username, password);
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
                errorLabel.setForeground(Color.RED);
                
                try {
                    LoginResponseDTO response = get();
                    if (response != null && response.isSuccess()) {
                        passwordField.setText("");
                        OtpPanel otpPanel = new OtpPanel(mainFrame, response);
                        mainFrame.addPanel("OtpPanel", otpPanel);
                        mainFrame.switchPanel("OtpPanel");
                    } else {
                        String msg = response != null ? response.getMessage() : "Unknown error";
                        errorLabel.setText(msg);
                        passwordField.setText("");
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Error: " + ex.getMessage());
                    passwordField.setText("");
                }
            }
        };
        worker.execute();
    }

    // Variables declaration - do not modify//GEN-BEGIN:initComponents
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel forgotPasswordLabel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel subtitle;
    private javax.swing.JLabel title;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:initComponents
}
