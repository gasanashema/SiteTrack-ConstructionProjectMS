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
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authController = new AuthController();
        
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1, true),
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
                Image resizedImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(resizedImg));
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                formPanel.add(logoLabel, gbc);
                gbc.gridy++;
            }
        } catch (Exception e) {}
        
        JLabel title = new JLabel("SiteTrack Construction", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 22));
        title.setForeground(Color.decode("#1f242e"));
        formPanel.add(title, gbc);
        gbc.gridy++;
        
        JLabel subtitle = new JLabel("Login to your account", SwingConstants.CENTER);
        subtitle.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitle, gbc);
        gbc.gridy++;
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        userLabel.setForeground(Color.decode("#1f242e"));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 10);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        passLabel.setForeground(Color.decode("#1f242e"));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(15, 0, 5, 0);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(250, 40));
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(0, 45));
        loginButton.setBackground(Color.decode("#FF5E14"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> attemptLogin());
        formPanel.add(loginButton, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(errorLabel, gbc);
        
        add(formPanel);
    }
    
    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        errorLabel.setText(" ");
        try {
            LoginResponseDTO response = authController.attemptLogin(username, password);
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
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }
}
