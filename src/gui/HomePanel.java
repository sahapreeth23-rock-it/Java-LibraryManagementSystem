package gui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel(
            "WELCOME TO OUR LIBRARY", JLabel.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel message = new JLabel(
            "Manage books, members and library activities easily.",
            JLabel.CENTER
        );
        message.setFont(new Font("Arial", Font.PLAIN, 17));

        JButton dashboardButton = new JButton("Go to Dashboard");

        JPanel center = new JPanel(new GridLayout(3, 1, 10, 10));
        center.setBackground(Color.WHITE);

        center.add(title);
        center.add(message);
        center.add(dashboardButton);

        add(center, BorderLayout.CENTER);

        dashboardButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ((MainFrame) frame).showDashboard();
        });
    }
}