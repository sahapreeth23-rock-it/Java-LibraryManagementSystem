package gui;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    JLabel booksCount;
    JLabel membersCount;
    JLabel issuedCount;

    public DashboardPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Library Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 20, 30)
        );

        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 20));
        cards.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );
        cards.setBackground(Color.WHITE);

        booksCount = new JLabel("0", JLabel.CENTER);
        membersCount = new JLabel("0", JLabel.CENTER);
        issuedCount = new JLabel("0", JLabel.CENTER);

        cards.add(createCard("Total Books", booksCount));
        cards.add(createCard("Total Members", membersCount));
        cards.add(createCard("Books Issued", issuedCount));

        add(title, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);

        JPanel actions = new JPanel();

        JButton booksButton = new JButton("Manage Books");
        JButton membersButton = new JButton("Manage Members");
        JButton issueButton = new JButton("Issue / Return");

        actions.add(booksButton);
        actions.add(membersButton);
        actions.add(issueButton);

        add(actions, BorderLayout.SOUTH);

        booksButton.addActionListener(e ->
            ((MainFrame) SwingUtilities.getWindowAncestor(this))
                .showPage("BOOKS")
        );

        membersButton.addActionListener(e ->
            ((MainFrame) SwingUtilities.getWindowAncestor(this))
                .showPage("MEMBERS")
        );

        issueButton.addActionListener(e ->
            ((MainFrame) SwingUtilities.getWindowAncestor(this))
                .showPage("ISSUE")
        );
    }

    JPanel createCard(String heading, JLabel value) {

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(new Color(230, 240, 250));

        JLabel name = new JLabel(heading, JLabel.CENTER);

        value.setFont(new Font("Arial", Font.BOLD, 26));

        card.add(name);
        card.add(value);

        return card;
    }

    public void updateStatistics(int books, int members, int issued) {

        booksCount.setText(String.valueOf(books));
        membersCount.setText(String.valueOf(members));
        issuedCount.setText(String.valueOf(issued));
    }
}