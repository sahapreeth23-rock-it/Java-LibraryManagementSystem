
package gui;

import model.IssueRecord;
import service.IssueService;
import service.LibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class IssuePanel extends JPanel {

    private final LibraryService libraryService;
    private final IssueService issueService;

    private JTextField txtBookId;
    private JTextField txtMemberId;
    private JTextField txtIssueId;

    private JTable issueTable;
    private DefaultTableModel tableModel;

    public IssuePanel(LibraryService libraryService,
                      IssueService issueService) {

        this.libraryService = libraryService;
        this.issueService = issueService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
        refreshIssueTable();
    }

    private void initComponents() {

        JLabel heading = new JLabel("Issue / Return Management");
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(heading, BorderLayout.NORTH);

        // Issue Book Form
        JPanel issuePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        issuePanel.setBorder(
            BorderFactory.createTitledBorder("Issue a Book")
        );

        txtBookId = new JTextField(15);
        txtMemberId = new JTextField(15);

        JButton btnIssue = new JButton("Issue Book");

        issuePanel.add(new JLabel("Book ID:"));
        issuePanel.add(txtBookId);

        issuePanel.add(new JLabel("Member ID:"));
        issuePanel.add(txtMemberId);

        issuePanel.add(new JLabel(""));
        issuePanel.add(btnIssue);

        // Return Book Form
        JPanel returnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        returnPanel.setBorder(
            BorderFactory.createTitledBorder("Return a Book")
        );

        txtIssueId = new JTextField(15);
        JButton btnReturn = new JButton("Return Book");

        returnPanel.add(new JLabel("Issue ID:"));
        returnPanel.add(txtIssueId);

        returnPanel.add(new JLabel(""));
        returnPanel.add(btnReturn);

        // Form Container
        JPanel formContainer = new JPanel(new GridLayout(1, 2, 15, 15));

        formContainer.add(issuePanel);
        formContainer.add(returnPanel);

        topPanel.add(formContainer, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Transaction Table
        String[] columns = {
            "Issue ID",
            "Book ID",
            "Member ID",
            "Issue Date",
            "Due Date",
            "Return Date",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        issueTable = new JTable(tableModel);
        issueTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(issueTable);

        JPanel tablePanel = new JPanel(new BorderLayout());

        tablePanel.setBorder(
            BorderFactory.createTitledBorder("Transaction Records")
        );

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Button Actions
        btnIssue.addActionListener(e -> issueBook());
        btnReturn.addActionListener(e -> returnBook());
    }

    // Issue a book
    private void issueBook() {

        try {

            String bookId = txtBookId.getText().trim();
            String memberId = txtMemberId.getText().trim();

            IssueRecord record =
                issueService.issueBook(bookId, memberId);

            refreshIssueTable();
            updateDashboard();

            txtBookId.setText("");
            txtMemberId.setText("");

            JOptionPane.showMessageDialog(
                this,
                "Book issued successfully!\nIssue ID: "
                    + record.getIssueId()
                    + "\nDue Date: " + record.getDueDate(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Issue Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Return a book
    private void returnBook() {

        try {

            String issueId = txtIssueId.getText().trim();

            IssueRecord record =
                issueService.returnBook(issueId);

            refreshIssueTable();
            updateDashboard();

            txtIssueId.setText("");

            JOptionPane.showMessageDialog(
                this,
                "Book returned successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Return Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Refresh transaction table
    public void refreshIssueTable() {

        List<IssueRecord> records =
            issueService.getAllIssueRecords();

        tableModel.setRowCount(0);

        for (IssueRecord record : records) {

            tableModel.addRow(new Object[]{

                record.getIssueId(),
                record.getBookId(),
                record.getMemberId(),
                record.getIssueDate(),
                record.getDueDate(),

                record.getReturnDate() == null
                    ? "Not Returned"
                    : record.getReturnDate(),

                record.isReturned()
                    ? "Returned"
                    : "Issued"
            });
        }
    }

    // Update dashboard statistics
    private void updateDashboard() {

        MainFrame frame =
            (MainFrame) SwingUtilities.getWindowAncestor(this);

        if (frame != null) {
            frame.showDashboardStatistics();
        }
    }
}
