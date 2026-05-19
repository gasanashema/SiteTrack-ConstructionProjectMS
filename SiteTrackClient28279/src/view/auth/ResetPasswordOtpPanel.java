package view.auth;

import controller.AuthController;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ResetPasswordOtpPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthController authController;
    private String userId;
    
    private JTextField[] otpFields;
    private JLabel errorLabel;
    private JButton verifyBtn;
    private JLabel timerLabel;
    private JButton resendBtn;
    private Timer resendTimer;
    private int secondsRemaining = 40;

    public ResetPasswordOtpPanel(MainFrame mainFrame, AuthController authController, String userId) {
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
        
        JLabel title = new JLabel("Verify Email", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 22));
        title.setForeground(Color.decode("#1f242e"));
        formPanel.add(title, gbc);
        gbc.gridy++;
        
        JLabel subtitle = new JLabel("Enter the 6-digit code sent to your email", SwingConstants.CENTER);
        subtitle.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitle, gbc);
        gbc.gridy++;
        
        // OTP input boxes
        JPanel otpInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        otpFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            otpFields[i] = new JTextField(1);
            otpFields[i].setPreferredSize(new Dimension(45, 50));
            otpFields[i].setFont(new Font("Ubuntu", Font.BOLD, 24));
            otpFields[i].setHorizontalAlignment(JTextField.CENTER);
            
            final int index = i;
            otpFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    char c = evt.getKeyChar();
                    if (!Character.isDigit(c)) {
                        evt.consume();
                        return;
                    }
                    if (otpFields[index].getText().length() >= 1) {
                        evt.consume();
                        otpFields[index].setText(String.valueOf(c));
                    }
                }
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE) {
                        if (index > 0 && otpFields[index].getText().isEmpty()) {
                            otpFields[index - 1].requestFocus();
                        }
                    } else if (Character.isDigit(evt.getKeyChar())) {
                        if (index < 5) {
                            otpFields[index + 1].requestFocus();
                        } else {
                            attemptVerify();
                        }
                    }
                }
            });
            otpInputPanel.add(otpFields[i]);
        }
        formPanel.add(otpInputPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 10, 0);
        verifyBtn = new JButton("Verify");
        verifyBtn.setPreferredSize(new Dimension(0, 45));
        verifyBtn.setBackground(Color.decode("#FF5E14"));
        verifyBtn.setForeground(Color.WHITE);
        verifyBtn.setFont(new Font("Ubuntu", Font.BOLD, 16));
        verifyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyBtn.addActionListener(e -> attemptVerify());
        formPanel.add(verifyBtn, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 0, 0);
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(errorLabel, gbc);
        
        // Timer / Resend area
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 0, 0);
        JPanel resendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        timerLabel = new JLabel("Resend code in 40s");
        timerLabel.setForeground(Color.GRAY);
        
        resendBtn = new JButton("Resend OTP");
        resendBtn.setContentAreaFilled(false);
        resendBtn.setBorderPainted(false);
        resendBtn.setForeground(Color.decode("#0056b3"));
        resendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resendBtn.setVisible(false);
        resendBtn.addActionListener(e -> attemptResend());
        
        resendPanel.add(timerLabel);
        resendPanel.add(resendBtn);
        formPanel.add(resendPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JLabel backToLogin = new JLabel("Back to Login");
        backToLogin.setFont(new Font("Ubuntu", Font.PLAIN, 13));
        backToLogin.setForeground(Color.decode("#0056b3"));
        backToLogin.setHorizontalAlignment(SwingConstants.CENTER);
        backToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (resendTimer != null) resendTimer.stop();
                mainFrame.switchPanel("LoginPanel");
            }
        });
        formPanel.add(backToLogin, gbc);
        
        add(formPanel);
        
        startResendTimer();
    }
    
    private void attemptVerify() {
        StringBuilder sb = new StringBuilder();
        for (JTextField tf : otpFields) {
            sb.append(tf.getText());
        }
        
        String code = sb.toString();
        if (code.length() < 6) {
            errorLabel.setText("Please enter all 6 digits");
            return;
        }
        
        verifyBtn.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authController.verifyPasswordResetOtp(userId, code);
            }
            @Override
            protected void done() {
                verifyBtn.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
                try {
                    boolean success = get();
                    if (success) {
                        if (resendTimer != null) resendTimer.stop();
                        NewPasswordPanel newPassPanel = new NewPasswordPanel(mainFrame, authController, userId);
                        mainFrame.addPanel("NewPasswordPanel", newPassPanel);
                        mainFrame.switchPanel("NewPasswordPanel");
                    } else {
                        errorLabel.setText("Invalid OTP code.");
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Error verifying OTP.");
                }
            }
        };
        worker.execute();
    }
    
    private void attemptResend() {
        resendBtn.setVisible(false);
        timerLabel.setText("Sending...");
        timerLabel.setVisible(true);
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authController.resendOtp(userId);
            }
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        errorLabel.setForeground(Color.decode("#188038"));
                        errorLabel.setText("OTP resent successfully!");
                        startResendTimer();
                    } else {
                        errorLabel.setForeground(Color.RED);
                        errorLabel.setText("Failed to resend OTP.");
                        resendBtn.setVisible(true);
                        timerLabel.setVisible(false);
                    }
                } catch (Exception ex) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("Error resending OTP.");
                    resendBtn.setVisible(true);
                    timerLabel.setVisible(false);
                }
            }
        };
        worker.execute();
    }
    
    private void startResendTimer() {
        secondsRemaining = 40;
        timerLabel.setVisible(true);
        timerLabel.setText("Resend code in " + secondsRemaining + "s");
        resendBtn.setVisible(false);
        
        if (resendTimer != null) resendTimer.stop();
        
        resendTimer = new Timer(1000, e -> {
            secondsRemaining--;
            if (secondsRemaining <= 0) {
                resendTimer.stop();
                timerLabel.setVisible(false);
                resendBtn.setVisible(true);
            } else {
                timerLabel.setText("Resend code in " + secondsRemaining + "s");
            }
        });
        resendTimer.start();
    }
}
