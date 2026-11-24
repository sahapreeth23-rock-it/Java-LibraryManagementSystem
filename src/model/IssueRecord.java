
package model;

import java.time.LocalDate;

public class IssueRecord {

    private String issueId;
    private String bookId;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    // Constructor
    public IssueRecord(String issueId, String bookId, String memberId,
                       LocalDate issueDate, LocalDate dueDate) {

        this.issueId = issueId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }
    // Getters
    public String getIssueId() {
        return issueId;
    }
    public String getBookId() {
        return bookId;
    }
    public String getMemberId() {
        return memberId;
    }
    public LocalDate getIssueDate() {
        return issueDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    // Setter for return date
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    // Check whether the book has been returned
    public boolean isReturned() {
        return returnDate != null;
    }

    // Convert record into a string for file storage
    public String toFileString() {
        return issueId + "," + bookId + "," + memberId + ","
                + issueDate + "," + dueDate + ","
                + (returnDate == null ? "" : returnDate);
    }

    @Override
    public String toString() {
        return "Issue ID: " + issueId
                + ", Book ID: " + bookId
                + ", Member ID: " + memberId
                + ", Issue Date: " + issueDate
                + ", Due Date: " + dueDate
                + ", Return Date: "
                + (returnDate == null ? "Not Returned" : returnDate);
    }
}
