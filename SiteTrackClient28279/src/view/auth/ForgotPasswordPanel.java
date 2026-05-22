package view.auth;

import controller.AuthController;
import dto.LoginResponseDTO;
import view.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ForgotPasswordPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthController authController;
    
    private JTextField emailField;
    private JLabel errorLabel;
    private JButton submitButton;

    public ForgotPasswordPanel(MainFrame mainFrame, AuthController authController) {
        this.mainFrame = mainFrame;
        this.authController = authController;
        
        setLayout(new GridBagLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1, true),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        // LOGO
        try {
            URL logoUrl = getClass().getResource("/resources/logo.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image img = originalIcon.getImage();
                Image resizedImg = img.getScaledInstance(180, -1, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(resizedImg));
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                formPanel.add(logoLabel, gbc);
                gbc.gridy++;
            }
        } catch (Exception e) {}
        
        JLabel title = new JLabel("Forgot Password", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 22));
        title.setForeground(UIManager.getColor("Label.foreground"));
        formPanel.add(title, gbc);
        gbc.gridy++;
        
        JLabel subtitle = new JLabel("Enter your email or username to reset", SwingConstants.CENTER);
        subtitle.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitle, gbc);
        gbc.gridy++;
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        
        JLabel userLabel = new JLabel("Email/Username:");
        userLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        userLabel.setForeground(UIManager.getColor("Label.foreground"));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        submitButton = new JButton("Send Reset OTP");
        submitButton.setPreferredSize(new Dimension(0, 45));
        submitButton.setBackground(Color.decode("#FF5E14"));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> attemptSendOtp());
        formPanel.add(submitButton, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);
        JLabel backToLogin = new JLabel("Back to Login");
        backToLogin.setFont(new Font("Ubuntu", Font.PLAIN, 13));
        backToLogin.setForeground(Color.decode("#0056b3"));
        backToLogin.setHorizontalAlignment(SwingConstants.CENTER);
        backToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainFrame.switchPanel("LoginPanel");
            }
        });
        formPanel.add(backToLogin, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(errorLabel, gbc);
        
        add(formPanel);
    }
    
    private void attemptSendOtp() {
        String identifier = emailField.getText();
        
        if (identifier.trim().isEmpty()) {
            errorLabel.setText("Please enter your email or username");
            return;
        }

        errorLabel.setForeground(Color.decode("#FF5E14"));
        errorLabel.setText("Sending OTP email... Please wait.");
        submitButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<LoginResponseDTO, Void> worker = new SwingWorker<LoginResponseDTO, Void>() {
            @Override
            protected LoginResponseDTO doInBackground() throws Exception {
                return authController.initiatePasswordReset(identifier);
            }

            @Override
            protected void done() {
                submitButton.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
                errorLabel.setForeground(Color.RED);
                
                try {
                    LoginResponseDTO response = get();
                    if (response != null && response.isSuccess()) {
                        emailField.setText("");
                        ResetPasswordOtpPanel otpPanel = new ResetPasswordOtpPanel(mainFrame, authController, response.getUserId());
                        mainFrame.addPanel("ResetPasswordOtpPanel", otpPanel);
                        mainFrame.switchPanel("ResetPasswordOtpPanel");
                    } else {
                        String msg = response != null ? response.getMessage() : "Unknown error";
                        errorLabel.setText(msg);
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
}
