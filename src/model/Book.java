package model;
public class Book { 
    private String bookId; 
    private String title; 
    private String author; 
    private String category; 
    private int totalCopies; 
    private int availableCopies; 
    Constructor public Book() { 
        this.bookId = ""; 
        this.title = ""; 
        this.author = ""; 
        this.category = ""; 
        this.totalCopies = 0; 
        this.availableCopies = 0; 
        } 
    public Book(String bookId, String title, String author, String category, int totalCopies) { 
        this.bookId = bookId; 
        this.title = title; 
        this.author = author; 
        this.category = category; 
        this.totalCopies = totalCopies; 
        this.availableCopies = totalCopies; 
        }
    public Book(String bookId, String title, String author, String category, int totalCopies, int availableCopies) { 
        this.bookId = bookId; 
        this.title = title; 
        this.author = author; 
        this.category = category; 
        this.totalCopies = totalCopies; 
        this.availableCopies = availableCopies; 
        }  
    public String getBookId() { return bookId; } 
    public void setBookId(String bookId) { this.bookId = bookId; } 
    public String getTitle() { return title; } 
    public void setTitle(String title) { this.title = title; } 
    public String getAuthor() { return author; } 
    public void setAuthor(String author) { this.author = author; } 
    public String getCategory() { return category; } 
    public void setCategory(String category) { this.category = category; } 
    public int getTotalCopies() { return totalCopies; } 
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; } 
    public int getAvailableCopies() { return availableCopies; } 
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; } 
    public String toFileString() { 
        return bookId + "," + title + "," + author + "," + category + "," + totalCopies + "," + availableCopies; 
        } 
        @Override 
        public String toString() {
             return "[" + bookId + "] " + title + " by " + author + " (" + category + ") - Available: " + availableCopies + "/" + totalCopies; 
        } 
    }