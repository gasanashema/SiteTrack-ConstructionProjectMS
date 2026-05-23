package view.settings;

import controller.SystemAdminController;
import controller.UserController;
import dto.UserDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsPanel extends JPanel {
    private MainFrame mainFrame;
    private SystemAdminController adminController;
    private UserController userController;

    private JTabbedPane tabbedPane;

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adminController = new SystemAdminController();
        this.userController = new UserController();

        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Settings & Configuration");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.BOLD, 14));

        if (SessionManager.getInstance().isAdmin()) {
            tabbedPane.addTab("Application Settings", createApplicationSettingsTab());
        }
        tabbedPane.addTab("User Settings", createUserSettingsTab());
        
        if (SessionManager.getInstance().isAdmin()) {
            tabbedPane.addTab("System Information", createSystemInfoTab());
        }

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createApplicationSettingsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Group 1: Appearance
        JPanel appearancePanel = new JPanel(new GridBagLayout());
        appearancePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Appearance", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        appearancePanel.setBackground(UIManager.getColor("Panel.background"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<String> themeCombo = new JComboBox<>(new String[]{"Light", "Dark"});
        themeCombo.addActionListener(e -> {
            String selected = (String) themeCombo.getSelectedItem();
            mainFrame.setTheme(selected);
        });

        JSlider fontSizeSlider = new JSlider(8, 16, 14);
        fontSizeSlider.setMajorTickSpacing(2);
        fontSizeSlider.setPaintTicks(true);
        fontSizeSlider.setPaintLabels(true);

        JComboBox<String> langCombo = new JComboBox<>(new String[]{"English"});
        JCheckBox notifCheck = new JCheckBox("Show system notifications", true);
        JCheckBox soundCheck = new JCheckBox("Enable sound notifications", true);

        gbc.gridx = 0; gbc.gridy = 0; appearancePanel.add(new JLabel("Theme:"), gbc);
        gbc.gridx = 1; appearancePanel.add(themeCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; appearancePanel.add(new JLabel("Font Size:"), gbc);
        gbc.gridx = 1; appearancePanel.add(fontSizeSlider, gbc);
        gbc.gridx = 0; gbc.gridy = 2; appearancePanel.add(new JLabel("Language:"), gbc);
        gbc.gridx = 1; appearancePanel.add(langCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; appearancePanel.add(notifCheck, gbc);
        gbc.gridy = 4; appearancePanel.add(soundCheck, gbc);

        // Group 2: Application
        JPanel appPanel = new JPanel(new GridBagLayout());
        appPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Application", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        appPanel.setBackground(UIManager.getColor("Panel.background"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField hostField = new JTextField(adminController.getServerHost(), 15);
        hostField.setEditable(false);
        JTextField portField = new JTextField(adminController.getServerPort(), 15);
        portField.setEditable(false);
        JSpinner autoSaveSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        JCheckBox updateCheck = new JCheckBox("Check for updates on startup", true);
        JButton checkUpdateBtn = new JButton("Check for Updates Now");

        gbc.gridx = 0; gbc.gridy = 0; appPanel.add(new JLabel("Server Host:"), gbc);
        gbc.gridx = 1; appPanel.add(hostField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; appPanel.add(new JLabel("Server Port:"), gbc);
        gbc.gridx = 1; appPanel.add(portField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; appPanel.add(new JLabel("Auto-save interval (min):"), gbc);
        gbc.gridx = 1; appPanel.add(autoSaveSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; appPanel.add(updateCheck, gbc);
        gbc.gridy = 4; appPanel.add(checkUpdateBtn, gbc);

        // Group 3: Data
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        dataPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Data Management", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        dataPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton clearCacheBtn = new JButton("Clear Cache");
        JButton exportDataBtn = new JButton("Export Application Data");
        JButton resetBtn = new JButton("Reset to Defaults");
        resetBtn.setForeground(Color.RED);

        dataPanel.add(clearCacheBtn);
        dataPanel.add(exportDataBtn);
        dataPanel.add(resetBtn);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setOpaque(false);
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(Color.decode("#2ecc71"));
        saveBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("Cancel");

        actionPanel.add(cancelBtn);
        actionPanel.add(saveBtn);

        panel.add(appearancePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(appPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(dataPanel);
        panel.add(Box.createVerticalGlue());
        panel.add(actionPanel);

        return panel;
    }

    private JPanel createUserSettingsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        UserDTO currentUser = SessionManager.getInstance().getCurrentUser();

        // Group 1: Personal
        JPanel personalPanel = new JPanel(new GridBagLayout());
        personalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Personal Information", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        personalPanel.setBackground(UIManager.getColor("Panel.background"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nameField = new JTextField(currentUser.getFullName(), 20);
        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);

        JPasswordField currentPassField = new JPasswordField(20);
        JPasswordField newPassField = new JPasswordField(20);
        JPasswordField confirmPassField = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0; personalPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; personalPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; personalPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; personalPanel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; personalPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; personalPanel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; personalPanel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1; personalPanel.add(currentPassField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; personalPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1; personalPanel.add(newPassField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; personalPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; personalPanel.add(confirmPassField, gbc);

        // Group 2: Preferences
        JPanel prefsPanel = new JPanel(new GridBagLayout());
        prefsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Preferences", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        prefsPanel.setBackground(UIManager.getColor("Panel.background"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JCheckBox rememberProjCheck = new JCheckBox("Remember last project selection", true);
        JCheckBox tooltipCheck = new JCheckBox("Show tooltips on hover", true);
        JCheckBox confirmDeleteCheck = new JCheckBox("Confirm before delete operations", true);
        JComboBox<String> defaultPageCombo = new JComboBox<>(new String[]{"Dashboard", "My Projects", "Materials"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; prefsPanel.add(rememberProjCheck, gbc);
        gbc.gridy = 1; prefsPanel.add(tooltipCheck, gbc);
        gbc.gridy = 2; prefsPanel.add(confirmDeleteCheck, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 3; prefsPanel.add(new JLabel("Default page on login:"), gbc);
        gbc.gridx = 1; prefsPanel.add(defaultPageCombo, gbc);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setOpaque(false);
        JButton updateBtn = new JButton("Update Profile");
        updateBtn.setBackground(Color.decode("#3498db"));
        updateBtn.setForeground(Color.WHITE);
        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setBackground(Color.decode("#e67e22"));
        changePassBtn.setForeground(Color.WHITE);

        actionPanel.add(changePassBtn);
        actionPanel.add(updateBtn);

        panel.add(personalPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(prefsPanel);
        panel.add(Box.createVerticalGlue());
        panel.add(actionPanel);

        // Handlers
        updateBtn.addActionListener(e -> {
            currentUser.setFullName(nameField.getText());
            currentUser.setEmail(emailField.getText());
            currentUser.setPhone(phoneField.getText());
            userController.updateUser(currentUser);
        });

        changePassBtn.addActionListener(e -> {
            String curr = new String(currentPassField.getPassword());
            String newP = new String(newPassField.getPassword());
            String conf = new String(confirmPassField.getPassword());
            if (curr.isEmpty() || newP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both current and new passwords.");
                return;
            }
            if (!newP.equals(conf)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.");
                return;
            }
            userController.changePassword(currentUser.getId(), curr, newP);
            currentPassField.setText("");
            newPassField.setText("");
            confirmPassField.setText("");
        });

        return panel;
    }

    private JPanel createSystemInfoTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Application Version:"), gbc);
        gbc.gridx = 1; panel.add(new JLabel("1.0.0"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Server Version:"), gbc);
        gbc.gridx = 1; panel.add(new JLabel("1.0.0"), gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Database:"), gbc);
        gbc.gridx = 1; panel.add(new JLabel("PostgreSQL 14"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Java Version:"), gbc);
        gbc.gridx = 1; panel.add(new JLabel(System.getProperty("java.version")), gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Operating System:"), gbc);
        gbc.gridx = 1; panel.add(new JLabel(System.getProperty("os.name")), gbc);

        long[] mem = adminController.getMemoryUsage();
        JProgressBar memBar = new JProgressBar(0, (int)mem[1]);
        memBar.setValue((int)mem[0]);
        memBar.setStringPainted(true);
        memBar.setString(mem[0] + " MB / " + mem[1] + " MB");
        
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Client Memory:"), gbc);
        gbc.gridx = 1; panel.add(memBar, gbc);

        JButton refreshBtn = new JButton("Refresh System Info");
        refreshBtn.addActionListener(e -> {
            long[] m = adminController.getMemoryUsage();
            memBar.setMaximum((int)m[1]);
            memBar.setValue((int)m[0]);
            memBar.setString(m[0] + " MB / " + m[1] + " MB");
        });

        gbc.gridx = 1; gbc.gridy = 6; panel.add(refreshBtn, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
}
