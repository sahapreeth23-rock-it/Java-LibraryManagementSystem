
package service;

import model.Book;
import model.Member;
import model.IssueRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueService {

    private final LibraryService libraryService;
    private final List<IssueRecord> issueRecords;

    private int nextIssueNumber = 1;

    private static final int LOAN_PERIOD = 14;

    // Constructor
    public IssueService(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.issueRecords = new ArrayList<>();
    }

    // Issue a book
    public IssueRecord issueBook(String bookId, String memberId) {

        Book book = libraryService.getBook(bookId);
        Member member = libraryService.getMember(memberId);

        // Check whether book exists
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        // Check whether member exists
        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }

        // Check book availability
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("Book is not available.");
        }

        // Generate issue ID
        String issueId = String.format("I%03d", nextIssueNumber++);

        // Set issue and due dates
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_PERIOD);

        // Create issue record
        IssueRecord record = new IssueRecord(
                issueId, bookId, memberId, issueDate, dueDate
        );

        // Update available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        // Store transaction
        issueRecords.add(record);

        return record;
    }

    // Return a book
    public IssueRecord returnBook(String issueId) {

        IssueRecord record = null;

        // Find issue record
        for (IssueRecord r : issueRecords) {
            if (r.getIssueId().equals(issueId)) {
                record = r;
                break;
            }
        }

        if (record == null) {
            throw new IllegalArgumentException("Issue record not found.");
        }

        // Check whether already returned
        if (record.isReturned()) {
            throw new IllegalArgumentException("Book has already been returned.");
        }

        // Find the book
        Book book = libraryService.getBook(record.getBookId());

        if (book == null) {
            throw new IllegalArgumentException("Book record not found.");
        }

        // Update return date
        record.setReturnDate(LocalDate.now());

        // Increase available copies
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }

        return record;
    }

    // Get all issue records
    public List<IssueRecord> getAllIssueRecords() {
        return new ArrayList<>(issueRecords);
    }

    // Get active (not returned) issues
    public List<IssueRecord> getActiveIssueRecords() {

        List<IssueRecord> activeRecords = new ArrayList<>();

        for (IssueRecord record : issueRecords) {
            if (!record.isReturned()) {
                activeRecords.add(record);
            }
        }

        return activeRecords;
    }

    // Get total number of currently issued books
    public int getIssuedCount() {
        return getActiveIssueRecords().size();
    }

    // Load previously saved issue records
    public void loadIssueRecords(List<IssueRecord> records) {

        issueRecords.clear();
        nextIssueNumber = 1;

        for (IssueRecord record : records) {
            issueRecords.add(record);

            try {
                int number = Integer.parseInt(
                        record.getIssueId().substring(1)
                );

                nextIssueNumber = Math.max(nextIssueNumber, number + 1);

            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                // Ignore IDs that do not follow the expected format
            }
        }
    }
}
