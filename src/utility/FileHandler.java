
package utility;

import model.Book;
import model.Member;
import model.IssueRecord;
import service.LibraryService;
import service.IssueService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final Path DATA_FOLDER = Path.of("data");

    private static final Path BOOKS_FILE =
            DATA_FOLDER.resolve("books.txt");

    private static final Path MEMBERS_FILE =
            DATA_FOLDER.resolve("members.txt");

    private static final Path ISSUES_FILE =
            DATA_FOLDER.resolve("issues.txt");


    // Save all book records
    public static void saveBooks(LibraryService libraryService)
            throws IOException {

        List<String> lines = new ArrayList<>();

        for (Book book : libraryService.getAllBooks()) {
            lines.add(book.toFileString());
        }

        writeFile(BOOKS_FILE, lines);
    }


    // Save all member records
    public static void saveMembers(LibraryService libraryService)
            throws IOException {

        List<String> lines = new ArrayList<>();

        for (Member member : libraryService.getAllMembers()) {
            lines.add(member.toFileString());
        }

        writeFile(MEMBERS_FILE, lines);
    }


    // Save all issue and return records
    public static void saveIssues(IssueService issueService)
            throws IOException {

        List<String> lines = new ArrayList<>();

        for (IssueRecord record : issueService.getAllIssueRecords()) {
            lines.add(record.toFileString());
        }

        writeFile(ISSUES_FILE, lines);
    }


    // Save all library data
    public static void saveAll(LibraryService libraryService,
                               IssueService issueService)
            throws IOException {

        saveBooks(libraryService);
        saveMembers(libraryService);
        saveIssues(issueService);
    }


    // Load book records
    public static void loadBooks(LibraryService libraryService)
            throws IOException {

        if (!Files.exists(BOOKS_FILE)) {
            return;
        }

        libraryService.getBookMap().clear();

        List<String> lines = Files.readAllLines(
                BOOKS_FILE, StandardCharsets.UTF_8);

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] data = line.split(",", -1);

                if (data.length != 6) {
                    throw new IllegalArgumentException(
                            "Invalid book record");
                }

                Book book = new Book(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        Integer.parseInt(data[4]),
                        Integer.parseInt(data[5])
                );

                libraryService.addBook(book);

            } catch (RuntimeException ex) {
                throw new IOException(
                        "Error loading book record: " + line, ex);
            }
        }
    }


    // Load member records
    public static void loadMembers(LibraryService libraryService)
            throws IOException {

        if (!Files.exists(MEMBERS_FILE)) {
            return;
        }

        libraryService.getMemberMap().clear();

        List<String> lines = Files.readAllLines(
                MEMBERS_FILE, StandardCharsets.UTF_8);

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] data = line.split(",", -1);

                if (data.length != 5) {
                    throw new IllegalArgumentException(
                            "Invalid member record");
                }

                Member member = new Member(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4]
                );

                libraryService.addMember(member);

            } catch (RuntimeException ex) {
                throw new IOException(
                        "Error loading member record: " + line, ex);
            }
        }
    }


    // Load issue and return records
    public static void loadIssues(IssueService issueService)
            throws IOException {

        if (!Files.exists(ISSUES_FILE)) {
            return;
        }

        List<IssueRecord> records = new ArrayList<>();

        List<String> lines = Files.readAllLines(
                ISSUES_FILE, StandardCharsets.UTF_8);

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] data = line.split(",", -1);

                if (data.length != 6) {
                    throw new IllegalArgumentException(
                            "Invalid issue record");
                }

                IssueRecord record = new IssueRecord(
                        data[0],
                        data[1],
                        data[2],
                        LocalDate.parse(data[3]),
                        LocalDate.parse(data[4])
                );

                if (!data[5].isEmpty()) {
                    record.setReturnDate(
                            LocalDate.parse(data[5]));
                }

                records.add(record);

            } catch (RuntimeException ex) {
                throw new IOException(
                        "Error loading issue record: " + line, ex);
            }
        }

        issueService.loadIssueRecords(records);
    }


    // Load all library data
    public static void loadAll(LibraryService libraryService,
                               IssueService issueService)
            throws IOException {

        loadBooks(libraryService);
        loadMembers(libraryService);
        loadIssues(issueService);
    }


    // Common method to write data into a file
    private static void writeFile(Path file, List<String> lines)
            throws IOException {

        Files.createDirectories(DATA_FOLDER);

        Files.write(
                file,
                lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
