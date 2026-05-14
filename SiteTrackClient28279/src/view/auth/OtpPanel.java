package view.auth;

import controller.AuthController;
import dto.LoginResponseDTO;
import view.MainFrame;
import view.dashboard.DashboardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class OtpPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthController authController;
    private LoginResponseDTO loginData;
    
    private JTextField otpField;
    private JButton verifyButton;
    private JButton resendButton;
    private JLabel timerLabel;
    private JLabel errorLabel;
    private Timer countdownTimer;
    private int timeLeft = 120;

    public OtpPanel(MainFrame mainFrame, LoginResponseDTO loginData) {
        this.mainFrame = mainFrame;
        this.loginData = loginData;
        this.authController = new AuthController();
        
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
        
        JLabel title = new JLabel("Enter OTP", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 22));
        title.setForeground(Color.decode("#1f242e"));
        formPanel.add(title, gbc);
        gbc.gridy++;
        
        JLabel subtitle = new JLabel("A 6-digit code has been sent to your email", SwingConstants.CENTER);
        subtitle.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitle, gbc);
        gbc.gridy++;
        
        gbc.insets = new Insets(5, 0, 20, 0);
        otpField = new JTextField(6);
        otpField.setPreferredSize(new Dimension(200, 50));
        otpField.setFont(new Font("Ubuntu", Font.BOLD, 24));
        otpField.setHorizontalAlignment(JTextField.CENTER);
        otpField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || otpField.getText().length() >= 6) {
                    e.consume();
                }
            }
        });
        formPanel.add(otpField, gbc);
        gbc.gridy++;
        
        verifyButton = new JButton("Verify Code");
        verifyButton.setPreferredSize(new Dimension(0, 45));
        verifyButton.setBackground(Color.decode("#FF5E14"));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
        verifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyButton.addActionListener(e -> verifyOtp());
        formPanel.add(verifyButton, gbc);
        gbc.gridy++;
        
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 0, 10, 0);
        formPanel.add(errorLabel, gbc);
        gbc.gridy++;
        
        resendButton = new JButton("Resend OTP");
        resendButton.setPreferredSize(new Dimension(0, 35));
        resendButton.addActionListener(e -> resendOtp());
        formPanel.add(resendButton, gbc);
        gbc.gridy++;
        
        timerLabel = new JLabel("Resend available in 120 seconds", SwingConstants.CENTER);
        timerLabel.setForeground(Color.GRAY);
        timerLabel.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        gbc.insets = new Insets(5, 0, 0, 0);
        formPanel.add(timerLabel, gbc);
        
        add(formPanel);
        startTimer();
    }
    
    private void startTimer() {
        timeLeft = 120;
        resendButton.setEnabled(false);
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) {
                countdownTimer.stop();
                timerLabel.setText("You can now resend the OTP");
                resendButton.setEnabled(true);
            } else {
                timerLabel.setText("Resend available in " + timeLeft + " seconds");
            }
        });
        countdownTimer.start();
    }
    
    private void verifyOtp() {
        String code = otpField.getText();
        errorLabel.setText(" ");
        try {
            boolean success = authController.verifyOtp(loginData.getUserId(), code, 
                                loginData.getRole(), loginData.getFullName(), loginData.getOtpId());
            if (success) {
                if (countdownTimer != null) countdownTimer.stop();
                DashboardPanel dashboard = new DashboardPanel(mainFrame);
                mainFrame.addPanel("DashboardPanel", dashboard);
                mainFrame.switchPanel("DashboardPanel");
            } else {
                errorLabel.setText("Invalid OTP");
            }
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }
    
    private void resendOtp() {
        authController.resendOtp(loginData.getUserId());
        startTimer();
    }
}
