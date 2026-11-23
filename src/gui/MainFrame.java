
package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
public void showDashboard() {
    cardLayout.show(mainPanel, "DASHBOARD");
}
public void showPage(String page) {
    cardLayout.show(mainPanel, page);
}
    CardLayout cardLayout;
    JPanel mainPanel;

    public MainFrame() {

        setTitle("Library Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new HomePanel(), "HOME");
        mainPanel.add(new DashboardPanel(), "DASHBOARD");
        mainPanel.add(createPage("Book Management"), "BOOKS");
        mainPanel.add(createPage("Member Management"), "MEMBERS");
        mainPanel.add(createPage("Issue / Return"), "ISSUE");

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

        button.addActionListener(e ->
            cardLayout.show(mainPanel, page)
        );

        sidebar.add(button);
    }
}
