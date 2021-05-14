package books.store;

import books.Author;
import books.Book;
import books.Books;
import books.Publisher;
import system.CSV;
import time.Date;
import user.Name;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Performs searching on the local book store.
 *
 * @author Zachary Cook
 */
public class LocalSearch implements StoreSearchService,Serializable {
    // The default file location of the book store file.
    public static String BOOK_STORE_FILE_LOCATION = "books.txt";

    private Books localStoreBooks;
    private Books sharedBooks;

    /**
     * Creates the book store.
     *
     * @param books the main list of books.
     */
    public LocalSearch(Books books) {
        this.localStoreBooks = new Books();
        this.sharedBooks = books;
    }

    /**
     * Adds a book to the book store.
     *
     * @param isbn the id of the book.
     * @param name the name of the book.
     * @param authors the authors of the book.
     * @param publisher the publisher of the book.
     * @param date the date the book was published.
     * @param pageCount the page count of the book.
     */
    public void addBook(long isbn,String name,ArrayList<Author> authors,Publisher publisher,Date date,int pageCount) {
        Book book = new Book(authors,publisher,isbn,date,pageCount,0,0,name,sharedBooks.size() + 1);
        this.localStoreBooks.add(book);
        this.sharedBooks.add(book);
    }

    /**
     * Adds a book from a CSV line.
     *
     * @param csvLine the line to parse.
     */
    public void addBookFromCSV(String csvLine) {
        // Split the string.
        ArrayList<String> entries = CSV.parseCSV(csvLine);

        // Parse the ISBN.
        long isbn;
        try {
            isbn = Long.parseLong(entries.get(0));
        } catch (NumberFormatException e) {
            System.out.println("ISBN number invalid (" + entries.get(0) + "); ignoring: " + csvLine);
            return;
        }

        // Parse the book name.
        String bookName = entries.get(1);

        // Parse the authors.
        String authorString = entries.get(2);
        ArrayList<Author> authors = new ArrayList<>();
        for (String authorName : Arrays.asList(authorString.split(","))) {
            authors.add(new Author(new Name(authorName)));
        }

        // Parse the publisher.
        String publisher = entries.get(3);

        // Parse the published date.
        String dateString = entries.get(4);
        int year = 0;
        int month = 1;
        int day = 1;
        try {
            if (dateString.length() > 0) {
                year = Integer.parseInt(dateString.substring(0,4));
            }
            if (dateString.length() > 4) {
                month = Integer.parseInt(dateString.substring(5, 7));
            }
            if (dateString.length() > 7) {
                day = Integer.parseInt(dateString.substring(8, 10));
            }
        } catch (NumberFormatException e) {
            System.out.println("Page count invalid (" + entries.get(4) + ") ; ignoring: " + csvLine);
            return;
        }

        // Parse the page count.
        int pageCount;
        try {
            pageCount = Integer.parseInt(entries.get(5));
        } catch (NumberFormatException e) {
            System.out.println("Page count invalid (" + entries.get(5) + "); ignoring: " + csvLine);
            return;
        }

        // Add the book.
        this.addBook(isbn,bookName,authors,new Publisher(publisher),new Date(month,day,year,0,0,0),pageCount);
    }

    /**
     * Returns the books for the given search.
     *
     * @param title the title of the book. To ignore this, leave it empty or use "*".
     * @param authors the authors of the book. To ignore this, leave it empty or use "*".
     * @param isbn the authors of the book. To ignore this, leave it empty or use "*".
     * @param publisher the publisher of the book. To ignore this, leave it empty or use "*".
     * @return the filtered books.
     */
    public Books getBooks(String title,String authors,String isbn,String publisher) {
        return this.localStoreBooks.filterBooks(title,authors,isbn,publisher);
    }

    /**
     * Creates a bookstore from the CSV file of books.
     *
     * @param books the main list of books.
     */
    public static LocalSearch fromFile(Books books) {
        // Create the bookstore.
        LocalSearch bookSearch = new LocalSearch(books);

        // Read the files by each line.
        BufferedReader reader;
        try {
            // Create the reader.
            reader = new BufferedReader(new FileReader(BOOK_STORE_FILE_LOCATION));

            // Keep reading lines until the lines have ended.
            String line = reader.readLine();
            while (line != null) {
                // Parse the book and add read the next line.
                bookSearch.addBookFromCSV(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Warning: The books in the book store (" +  BOOK_STORE_FILE_LOCATION + ") is unreadable.");
            // e.printStackTrace();
        }

        // Return the bookstore.
        return bookSearch;
    }
}
