package view.workers;

import controller.WorkerController;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class WorkersPanel extends JPanel {
    private MainFrame mainFrame;
    private WorkerController workerController;
    private JTabbedPane tabbedPane;

    public WorkersPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.workerController = new WorkerController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(UIManager.getColor("Panel.background"));
        
        JLabel titleLabel = new JLabel("Workers Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topHeaderPanel.add(titleLabel, BorderLayout.NORTH);
        add(topHeaderPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));

        // Worker List Tab (Accessible to Admin and Site Manager)
        tabbedPane.addTab("Site Workers", new WorkerListPanel(mainFrame, workerController));

        // Worker Types Tab (Admin only)
        if (SessionManager.getInstance().isAdmin()) {
            tabbedPane.addTab("Worker Types", new WorkerTypePanel(mainFrame, workerController));
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
