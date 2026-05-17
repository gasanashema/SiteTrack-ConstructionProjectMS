package view.admin;

import controller.SystemAdminController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupPanel extends JPanel {
    private SystemAdminController adminController;

    private JLabel lastBackupLabel;
    private JLabel lastArchivedLabel;

    public BackupPanel() {
        this.adminController = new SystemAdminController();
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("Backup & Data Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2c3e50"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIManager.getColor("Panel.background"));

        // --- Section 1: Database Backup ---
        JPanel backupPanel = new JPanel(new GridBagLayout());
        backupPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Database Backup", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        backupPanel.setBackground(UIManager.getColor("Panel.background"));
        backupPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        lastBackupLabel = new JLabel("Never");
        JButton createBackupBtn = new JButton("Create Backup Now");
        createBackupBtn.setBackground(Color.decode("#3498db"));
        createBackupBtn.setForeground(Color.WHITE);
        
        JButton openFolderBtn = new JButton("Open Backup Folder");
        JCheckBox autoBackupCheck = new JCheckBox("Enable Automatic Backups", true);
        JComboBox<String> freqCombo = new JComboBox<>(new String[]{"Daily", "Weekly", "Monthly"});

        gbc.gridx = 0; gbc.gridy = 0; backupPanel.add(new JLabel("Last Backup:"), gbc);
        gbc.gridx = 1; backupPanel.add(lastBackupLabel, gbc);
        gbc.gridx = 2; backupPanel.add(createBackupBtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; backupPanel.add(new JLabel("Backup Location:"), gbc);
        gbc.gridx = 1; backupPanel.add(new JLabel(System.getProperty("user.home") + File.separator + "SiteTrackBackups"), gbc);
        gbc.gridx = 2; backupPanel.add(openFolderBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; backupPanel.add(autoBackupCheck, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; backupPanel.add(freqCombo, gbc);

        // --- Section 2: Data Export ---
        JPanel exportPanel = new JPanel(new GridBagLayout());
        exportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Export Business Data", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        exportPanel.setBackground(UIManager.getColor("Panel.background"));
        exportPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<String> formatCombo = new JComboBox<>(new String[]{"CSV", "Excel", "JSON"});
        JButton expProjBtn = new JButton("Export All Projects");
        JButton expMatBtn = new JButton("Export Materials & Stock");
        JButton expLaborBtn = new JButton("Export Labor Records");

        gbc.gridx = 0; gbc.gridy = 0; exportPanel.add(new JLabel("Format:"), gbc);
        gbc.gridx = 1; exportPanel.add(formatCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; exportPanel.add(expProjBtn, gbc);
        gbc.gridx = 1; exportPanel.add(expMatBtn, gbc);
        gbc.gridx = 2; exportPanel.add(expLaborBtn, gbc);

        // --- Section 3: Data Import ---
        JPanel importPanel = new JPanel(new GridBagLayout());
        importPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Import Data", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        importPanel.setBackground(UIManager.getColor("Panel.background"));
        importPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JButton selectFileBtn = new JButton("Select File");
        JLabel fileLabel = new JLabel("No file selected");
        JButton importBtn = new JButton("Import");
        importBtn.setEnabled(false);

        selectFileBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                fileLabel.setText(chooser.getSelectedFile().getName());
                importBtn.setEnabled(true);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; importPanel.add(selectFileBtn, gbc);
        gbc.gridx = 1; importPanel.add(fileLabel, gbc);
        gbc.gridx = 2; importPanel.add(importBtn, gbc);

        // --- Section 4: Archive ---
        JPanel archivePanel = new JPanel(new GridBagLayout());
        archivePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Archive Old Records", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        archivePanel.setBackground(UIManager.getColor("Panel.background"));
        archivePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(12, 1, 60, 1));
        JButton archiveBtn = new JButton("Archive Now");
        archiveBtn.setBackground(Color.decode("#e67e22"));
        archiveBtn.setForeground(Color.WHITE);
        lastArchivedLabel = new JLabel("Last Archived: Never");

        gbc.gridx = 0; gbc.gridy = 0; archivePanel.add(new JLabel("Archive records older than (months):"), gbc);
        gbc.gridx = 1; archivePanel.add(monthSpinner, gbc);
        gbc.gridx = 2; archivePanel.add(archiveBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; archivePanel.add(lastArchivedLabel, gbc);

        // --- Actions ---
        createBackupBtn.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this, "Create a backup of the entire database? This may take a few minutes.", "Confirm", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                JDialog progress = new JDialog(Window.getWindows()[0], "Backing up", Dialog.ModalityType.APPLICATION_MODAL);
                progress.setSize(300, 100);
                progress.setLocationRelativeTo(this);
                JProgressBar bar = new JProgressBar();
                bar.setIndeterminate(true);
                progress.add(new JLabel("Creating backup, please wait...", SwingConstants.CENTER), BorderLayout.NORTH);
                progress.add(bar, BorderLayout.CENTER);
                
                adminController.simulateBackup(() -> {
                    progress.dispose();
                    lastBackupLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss")));
                    JOptionPane.showMessageDialog(this, "Backup completed successfully!");
                }, () -> {
                    progress.dispose();
                    JOptionPane.showMessageDialog(this, "Backup failed.", "Error", JOptionPane.ERROR_MESSAGE);
                });
                progress.setVisible(true);
            }
        });

        expProjBtn.addActionListener(e -> adminController.exportDataPlaceholder("Projects", (String)formatCombo.getSelectedItem()));
        expMatBtn.addActionListener(e -> adminController.exportDataPlaceholder("Materials", (String)formatCombo.getSelectedItem()));
        expLaborBtn.addActionListener(e -> adminController.exportDataPlaceholder("Labor", (String)formatCombo.getSelectedItem()));

        archiveBtn.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this, "Archive old records? This will move them out of active queries.", "Confirm Archive", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                lastArchivedLabel.setText("Last Archived: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                JOptionPane.showMessageDialog(this, "Archive process completed.");
            }
        });

        contentPanel.add(backupPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(exportPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(importPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(archivePanel);
        contentPanel.add(Box.createVerticalGlue());

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }
}
