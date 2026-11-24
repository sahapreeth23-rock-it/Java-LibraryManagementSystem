package service;

import model.Book;
import model.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryService {

    private final Map<String, Book> bookMap;
    private final Map<String, Member> memberMap;

    public LibraryService() {
        this.bookMap = new HashMap<>();
        this.memberMap = new HashMap<>();
    }


    public void addBook(Book book) throws IllegalArgumentException {
        if (book == null || book.getBookId() == null || book.getBookId().trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty.");
        }
        if (bookMap.containsKey(book.getBookId())) {
            throw new IllegalArgumentException("Book with ID '" + book.getBookId() + "' already exists.");
        }
        bookMap.put(book.getBookId(), book);
    }

    public void updateBook(Book updatedBook) throws IllegalArgumentException {
        if (updatedBook == null || !bookMap.containsKey(updatedBook.getBookId())) {
            String id = (updatedBook == null) ? "null" : updatedBook.getBookId();
            throw new IllegalArgumentException("Cannot update. Book ID '" + id + "' not found.");
        }
        bookMap.put(updatedBook.getBookId(), updatedBook);
    }

    public void deleteBook(String bookId) throws IllegalArgumentException {
        if (!bookMap.containsKey(bookId)) {
            throw new IllegalArgumentException("Cannot delete. Book ID '" + bookId + "' does not exist.");
        }
        bookMap.remove(bookId);
    }

    public Book getBook(String bookId) {
        return bookMap.get(bookId);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookMap.values());
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        String lowerQuery = query.toLowerCase().trim();
        return bookMap.values().stream()
                .filter(b -> containsIgnoreCase(b.getBookId(), lowerQuery)
                        || containsIgnoreCase(b.getTitle(), lowerQuery)
                        || containsIgnoreCase(b.getAuthor(), lowerQuery)
                        || containsIgnoreCase(b.getCategory(), lowerQuery))
                .collect(Collectors.toList());
    }


    public void addMember(Member member) throws IllegalArgumentException {
        if (member == null || member.getMemberId() == null || member.getMemberId().trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (memberMap.containsKey(member.getMemberId())) {
            throw new IllegalArgumentException("Member with ID '" + member.getMemberId() + "' already exists.");
        }
        memberMap.put(member.getMemberId(), member);
    }

    public void updateMember(Member updatedMember) throws IllegalArgumentException {
        if (updatedMember == null || !memberMap.containsKey(updatedMember.getMemberId())) {
            String id = (updatedMember == null) ? "null" : updatedMember.getMemberId();
            throw new IllegalArgumentException("Cannot update. Member ID '" + id + "' not found.");
        }
        memberMap.put(updatedMember.getMemberId(), updatedMember);
    }

    public void deleteMember(String memberId) throws IllegalArgumentException {
        if (!memberMap.containsKey(memberId)) {
            throw new IllegalArgumentException("Cannot delete. Member ID '" + memberId + "' does not exist.");
        }
        memberMap.remove(memberId);
    }

    public Member getMember(String memberId) {
        return memberMap.get(memberId);
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(memberMap.values());
    }

    public List<Member> searchMembers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMembers();
        }
        String lowerQuery = query.toLowerCase().trim();
        return memberMap.values().stream()
                .filter(m -> containsIgnoreCase(m.getMemberId(), lowerQuery)
                        || containsIgnoreCase(m.getName(), lowerQuery)
                        || containsIgnoreCase(m.getEmail(), lowerQuery)
                        || containsIgnoreCase(m.getPhone(), lowerQuery))
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String field, String lowerQuery) {
        return field != null && field.toLowerCase().contains(lowerQuery);
    }

    public Map<String, Book> getBookMap() {
        return bookMap;
    }

    public Map<String, Member> getMemberMap() {
        return memberMap;
    }
}
