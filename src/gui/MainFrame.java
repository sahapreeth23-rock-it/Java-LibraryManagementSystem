
package gui;

import javax.swing.*;
import java.awt.*;
import service.LibraryService;
import service.IssueService;
import utility.FileHandler;
import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    private LibraryService libraryService;
    private IssueService issueService;
    private DashboardPanel dashboardPanel;

    public void showDashboard() {

        dashboardPanel.updateStatistics(
                libraryService.getAllBooks().size(),
                libraryService.getAllMembers().size(),
                issueService.getIssuedCount()
        );

        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void showPage(String page) {
        cardLayout.show(mainPanel, page);
    }

    public MainFrame() {

        setTitle("Library Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        libraryService = new LibraryService();
        issueService = new IssueService(libraryService);
        try {
            FileHandler.loadAll(libraryService, issueService);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading saved data: " + e.getMessage()
            );
        }

        mainPanel.add(new HomePanel(), "HOME");
        dashboardPanel = new DashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");
        // Actual management panels
        mainPanel.add(new BookPanel(libraryService), "BOOKS");
        mainPanel.add(new MemberPanel(libraryService), "MEMBERS");

        mainPanel.add(
                new IssuePanel(libraryService, issueService),
                "ISSUE"
        );
        JPanel sidebar = new JPanel(new GridLayout(7, 1, 5, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(25, 45, 75));

        JLabel heading = new JLabel("  LIBRARY SYSTEM");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 17));

        sidebar.add(heading);

        addButton(sidebar, "Home", "HOME");
        addButton(sidebar, "Dashboard", "DASHBOARD");
        addButton(sidebar, "Books", "BOOKS");
        addButton(sidebar, "Members", "MEMBERS");
        addButton(sidebar, "Issue / Return", "ISSUE");

        sidebar.add(new JLabel(""));

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Save all data when the application closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    FileHandler.saveAll(libraryService, issueService);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Error saving data: " + ex.getMessage()
                    );
                }
            }
        });

        setVisible(true);
    }

    JPanel createPage(String title) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 20, 30)
        );

        JLabel message = new JLabel(
            "This section will be available soon.",
            JLabel.CENTER
        );

        message.setFont(new Font("Arial", Font.PLAIN, 18));

        panel.add(heading, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);

        return panel;
    }

    void addButton(JPanel sidebar, String name, String page) {

        JButton button = new JButton(name);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 75, 115));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            if (page.equals("DASHBOARD")) {
                showDashboard();
            } else {
                cardLayout.show(mainPanel, page);
            }
        });

        sidebar.add(button);
    }
}
