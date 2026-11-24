package gui;

import model.Member;
import service.LibraryService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MemberPanel extends JPanel {

    private final LibraryService libraryService;

    private JTextField txtMemberId;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtSearch;

    private JTable memberTable;
    private DefaultTableModel tableModel;

    public MemberPanel(LibraryService libraryService) {
        this.libraryService = libraryService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
        refreshMemberTable(libraryService.getAllMembers());
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel lblHeader = new JLabel("Member Management", JLabel.LEFT);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.add(new JLabel("Search Member:"));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnResetSearch = new JButton("Reset");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnResetSearch);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Member ID", "Name", "Email", "Phone", "Membership Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(24);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(memberTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        txtMemberId = new JTextField(15);
        formPanel.add(txtMemberId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(15);
        formPanel.add(txtPhone, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAdd = new JButton("Register");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        btnAdd.addActionListener(e -> addMember());

        btnUpdate.addActionListener(e -> updateMember());

        btnDelete.addActionListener(e -> deleteMember());

        btnClear.addActionListener(e -> clearFields());

        btnSearch.addActionListener(e -> performSearch());
        btnResetSearch.addActionListener(e -> {
            txtSearch.setText("");
            refreshMemberTable(libraryService.getAllMembers());
        });

        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && memberTable.getSelectedRow() != -1) {
                int row = memberTable.getSelectedRow();
                txtMemberId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                txtPhone.setText(tableModel.getValueAt(row, 3).toString());
                txtMemberId.setEditable(false); 
            }
        });
    }

    private void addMember() {
        try {
            String id = txtMemberId.getText().trim();
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                throw new IllegalArgumentException("All fields are required.");
            }

            Member member = new Member(id, name, email, phone);
            libraryService.addMember(member);
            refreshMemberTable(libraryService.getAllMembers());
            clearFields();
            JOptionPane.showMessageDialog(this, "Member registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMember() {
        try {
            String id = txtMemberId.getText().trim();
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();

            Member existing = libraryService.getMember(id);
            if (existing == null) {
                throw new IllegalArgumentException("Select a member from the table to update.");
            }

            Member updated = new Member(id, name, email, phone, existing.getMembershipDate());
            libraryService.updateMember(updated);
            refreshMemberTable(libraryService.getAllMembers());
            clearFields();
            JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMember() {
        try {
            String id = txtMemberId.getText().trim();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete member ID '" + id + "'?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                libraryService.deleteMember(id);
                refreshMemberTable(libraryService.getAllMembers());
                clearFields();
                JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String query = txtSearch.getText().trim();
        List<Member> results = libraryService.searchMembers(query);
        refreshMemberTable(results);
    }

    private void refreshMemberTable(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member m : members) {
            tableModel.addRow(new Object[]{
                    m.getMemberId(),
                    m.getName(),
                    m.getEmail(),
                    m.getPhone(),
                    m.getMembershipDate()
            });
        }
    }

    private void clearFields() {
        txtMemberId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtMemberId.setEditable(true);
        memberTable.clearSelection();
    }
}
