import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean available;
    private Date dueDate;
    private float penaltyPerDay;

    public Book(int bookId, String title, String author, float penaltyPerDay) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = true;
        this.dueDate = null;
        this.penaltyPerDay = penaltyPerDay;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void checkOut(Date dueDate) {
        available = false;
        this.dueDate = dueDate;
    }

    public float returnBook(Date returnDate) {
        if (dueDate == null) {
            return 0;
        }

        long daysLate = daysBetweenDates(dueDate, returnDate);
        if (daysLate <= 0) {
            available = true;
            dueDate = null;
            return 0;
        } else {
            available = true;
            dueDate = null;
            return daysLate * penaltyPerDay;
        }
    }

    private static long daysBetweenDates(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        return diffTime / (24 * 60 * 60 * 1000); // Milliseconds in a day
    }
}

class Library {
    private ArrayList<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public boolean isBookIdAvailable(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return false; // Book ID is not available
            }
        }
        return true; // Book ID is available
    }

    public void displayAvailableBooks() {
        System.out.println("Available Books:");
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println("Book ID: " + book.getBookId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println();
            }
        }
    }

    public boolean checkoutBookByName(String bookName, Date dueDate) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookName) && book.isAvailable()) {
                book.checkOut(dueDate);
                return true;
            }
        }
        return false;
    }

    public float returnBook(int bookId, Date returnDate) {
        for (Book book : books) {
            if (book.getBookId() == bookId && !book.isAvailable()) {
                return book.returnBook(returnDate);
            }
        }
        return -1; // Book not found or already returned
    }

    public boolean isBookAvailableById(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId && book.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public boolean removeBookById(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                books.remove(book);
                return true;
            }
        }
        return false;
    }

    public boolean isBookAvailableByName(String bookName) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookName) && book.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}

class LibraryManagementSystem {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Add some sample books to the library with a penalty of 2 rupees per day
        library.addBook(new Book(1, "OCA Java SE 8", "Kathy Sierra, Bert Bates, Elisabeth Robson", 2.0f));
        library.addBook(new Book(2, "Java The Complete Reference", "Herbert Schildt", 2.0f));
        library.addBook(new Book(3, "Head First Java", "Kathy Sierra, Bert Bates", 2.0f));

        int choice;
        do {
            System.out.println("Library Management System");
            System.out.println("1. View Available Books");
            System.out.println("2. Checkout a Book by Name");
            System.out.println("3. Return a Book");
            System.out.println("4. Remove a Book by ID");
            System.out.println("5. Add a Book");
            System.out.println("6. Check Book Availability by Book Name");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    library.displayAvailableBooks();
                    break;
                case 2:
                    System.out.print("Enter Book Name to checkout: ");
                    scanner.nextLine(); // Consume the newline character
                    String checkoutName = scanner.nextLine();
                    System.out.print("Enter due date (yyyy-MM-dd): ");
                    String dueDateStr = scanner.nextLine();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dueDate = dateFormat.parse(dueDateStr);
                    if (library.checkoutBookByName(checkoutName, dueDate)) {
                        System.out.println("Book checked out successfully.");
                    } else {
                        System.out.println("Book not found or already checked out.");
                    }
                    break;
                case 3:
                    System.out.print("Enter Book ID to return: ");
                    int returnId = scanner.nextInt();
                    System.out.print("Enter return date (yyyy-MM-dd): ");
                    scanner.nextLine(); // Consume the newline character
                    String returnDateStr = scanner.nextLine();
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date returnDate = dateFormat.parse(returnDateStr);
                    float penaltyAmount = library.returnBook(returnId, returnDate);
                    if (penaltyAmount >= 0) {
                        if (penaltyAmount == 0) {
                            System.out.println("Book returned successfully.");
                        } else {
                            System.out.println("Book returned with a penalty of " + penaltyAmount + " rupees.");
                        }
                    } else {
                        System.out.println("Book not found or already returned.");
                    }
                    break;
                case 4:
                    System.out.print("Enter Book ID to remove: ");
                    int removeId = scanner.nextInt();
                    if (library.removeBookById(removeId)) {
                        System.out.println("Book removed successfully.");
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 5:
                    System.out.print("Enter Book ID: ");
                    int bookId = scanner.nextInt();
                    if (!library.isBookIdAvailable(bookId)) {
                        System.out.println("Book ID is already taken.");
                        break;
                    }
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Penalty Per Day: ");
                    float penaltyPerDay = scanner.nextFloat();
                    library.addBook(new Book(bookId, title, author, penaltyPerDay));
                    System.out.println("Book added successfully.");
                    break;
                case 6:
                    System.out.print("Enter Book Name to check availability: ");
                    scanner.nextLine(); // Consume the newline character
                    String bookName = scanner.nextLine();
                    if (library.isBookAvailableByName(bookName)) {
                        System.out.println("Book is available.");
                    } else {
                        System.out.println("Book is not available.");
                    }
                    break;
                case 7:
                    System.out.println("Exiting the Library Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);

        scanner.close();
    }
}