package books;


import java.io.Serializable;

/**
 * Class representing the inventory of books.
 *
 * @author Michael Dolan
 * @author Zachary Cook
 */
public class Inventory implements Serializable {
    private Books books;

    /**
     * Creates the inventory of book.
     */
    public Inventory() {
        this.books = new Books();
    }

    /**
     * Registers a book in the inventory.
     *
     * @param book the book to register.
     */
    public void registerBook(Book book) {
        // If the book doesn't exist, add the book.
        if (this.getBook(book.getISBN()) == null) {
            this.books.add(book);
        }
    }

    /**
     * Returns the book for the ISBN number.
     *
     * @param isbn the ISBN number of the book.
     *
     * @return the book with the corresponding ISBN number.
     */
    public Book getBook(long isbn) {
        // Find and return the book.
        for (Book book : this.books) {
            if (book.getISBN() == isbn) {
                return book;
            }
        }

        // Return null (not found).
        return null;
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
        return this.books.filterBooks(title,authors,isbn,publisher);
    }

    /**
     * Returns all of the books in the inventory.
     *
     * @return all of the books.
     */
    public Books getBooks() {
        return new Books(this.books);
    }


}
