
package gui;

import model.Book;
import service.LibraryService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {

    private final LibraryService libraryService;

    private JTextField txtBookId;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtCategory;
    private JTextField txtTotalCopies;
    private JTextField txtSearch;

    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
        refreshBookTable(libraryService.getAllBooks());
    }

    private void initComponents() {

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JLabel lblHeader = new JLabel("Book Management", JLabel.LEFT);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 20));

        topPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        searchPanel.add(new JLabel("Search Book:"));

        txtSearch = new JTextField(20);

        JButton btnSearch = new JButton("Search");
        JButton btnResetSearch = new JButton("Reset");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnResetSearch);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {
            "Book ID", "Title", "Author",
            "Category", "Total Copies", "Available"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(24);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Book ID:"), gbc);

        gbc.gridx = 1;
        txtBookId = new JTextField(15);
        formPanel.add(txtBookId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        txtTitle = new JTextField(15);
        formPanel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Author:"), gbc);

        gbc.gridx = 1;
        txtAuthor = new JTextField(15);
        formPanel.add(txtAuthor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        txtCategory = new JTextField(15);
        formPanel.add(txtCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Total Copies:"), gbc);

        gbc.gridx = 1;
        txtTotalCopies = new JTextField(15);
        formPanel.add(txtTotalCopies, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearFields());

        btnSearch.addActionListener(e -> performSearch());

        btnResetSearch.addActionListener(e -> {
            txtSearch.setText("");
            refreshBookTable(libraryService.getAllBooks());
        });

        bookTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {

                int row = bookTable.getSelectedRow();

                txtBookId.setText(tableModel.getValueAt(row, 0).toString());
                txtTitle.setText(tableModel.getValueAt(row, 1).toString());
                txtAuthor.setText(tableModel.getValueAt(row, 2).toString());
                txtCategory.setText(tableModel.getValueAt(row, 3).toString());
                txtTotalCopies.setText(tableModel.getValueAt(row, 4).toString());

                txtBookId.setEditable(false);
            }
        });
    }

    private void addBook() {

        try {

            String id = txtBookId.getText().trim();
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            String category = txtCategory.getText().trim();

            int copies = Integer.parseInt(txtTotalCopies.getText().trim());

            Book book = new Book(id, title, author, category, copies);

            libraryService.addBook(book);

            refreshBookTable(libraryService.getAllBooks());
            updateDashboard();
            clearFields();

            JOptionPane.showMessageDialog(
                this,
                "Book added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Total Copies must be a valid integer.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateBook() {

        try {

            String id = txtBookId.getText().trim();
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            String category = txtCategory.getText().trim();

            int copies = Integer.parseInt(txtTotalCopies.getText().trim());

            Book existing = libraryService.getBook(id);

            if (existing == null) {
                throw new IllegalArgumentException(
                    "Select a book from the table to update."
                );
            }

            int diff = copies - existing.getTotalCopies();

            int newAvailable = Math.max(
                0,
                existing.getAvailableCopies() + diff
            );

            Book updated = new Book(
                id, title, author, category, copies, newAvailable
            );

            libraryService.updateBook(updated);

            refreshBookTable(libraryService.getAllBooks());
            updateDashboard();
            clearFields();

            JOptionPane.showMessageDialog(
                this,
                "Book updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Total Copies must be a valid integer.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteBook() {

        try {

            String id = txtBookId.getText().trim();

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete book ID '" + id + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                libraryService.deleteBook(id);

                refreshBookTable(libraryService.getAllBooks());
                updateDashboard();
                clearFields();

                JOptionPane.showMessageDialog(
                    this,
                    "Book deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void performSearch() {

        String query = txtSearch.getText().trim();

        List<Book> results = libraryService.searchBooks(query);

        refreshBookTable(results);
    }

    private void refreshBookTable(List<Book> books) {

        tableModel.setRowCount(0);

        for (Book b : books) {

            tableModel.addRow(new Object[]{
                b.getBookId(),
                b.getTitle(),
                b.getAuthor(),
                b.getCategory(),
                b.getTotalCopies(),
                b.getAvailableCopies()
            });
        }
    }

    private void clearFields() {

        txtBookId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
        txtTotalCopies.setText("");

        txtBookId.setEditable(true);

        bookTable.clearSelection();
    }

    private void updateDashboard() {

        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(this);

        if (frame != null) {
            frame.showDashboardStatistics();
        }
    }
}
