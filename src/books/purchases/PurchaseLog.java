package books.purchases;

import books.Book;
import time.Date;

import java.io.Serializable;

/**
 * Class representing a purchase log.
 *
 * @author Zachary Cook
 */
public class PurchaseLog implements Serializable {
    private Book book;
    private Date date;

    /**
     * Creates a purchase log class.
     *
     * @param book the book.
     * @param purchaseDate the purchase date.
     */
    public PurchaseLog(Book book,Date purchaseDate) {
        this.book = book;
        this.date = purchaseDate;
    }

    /**
     * Returns the book.
     *
     * @return the book.
     */
    public Book getBook() {
        return this.book;
    }

    /**
     * Returns the purchase date.
     *
     * @return the purchase date.
     */
    public Date getPurchaseDate() {
        return this.date;
    }
}
