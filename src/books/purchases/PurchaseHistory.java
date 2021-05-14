package books.purchases;

import books.Book;
import time.Date;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing the purchase history of books.
 *
 * @author Zachary Cook
 */
public class PurchaseHistory implements Serializable {
    private ArrayList<PurchaseLog> purchaseLogs;

    /**
     * Creates a purchase history.
     */
    public PurchaseHistory() {
        this.purchaseLogs = new ArrayList<>();
    }

    /**
     * Adds a purchase log.
     *
     * @param book the book.
     * @param purchaseDate the purchase date.
     */
    public void registerPurchase(Book book, Date purchaseDate) {
        this.purchaseLogs.add(new PurchaseLog(book,purchaseDate));
    }

    /**
     * Removes a purchase log.
     *
     * @param book the book.
     * @param purchaseDate the purchase date.
     */
    public void unregisterPurchase(Book book,Date purchaseDate) {
        // Get the index to remove.
        int indexToRemove = -1;
        for (int i = 0; i <= this.purchaseLogs.size(); i++) {
            PurchaseLog log = this.purchaseLogs.get(i);
            if (log.getBook().getISBN() == book.getISBN() && log.getPurchaseDate().equals(purchaseDate)) {
                indexToRemove = i;
                break;
            }
        }

        // Remove the purchase.
        if (indexToRemove != -1) {
            this.purchaseLogs.remove(indexToRemove);
        }
    }

    /**
     * Returns the purchase logs.
     *
     * @return the purchase logs.
     */
    public ArrayList<PurchaseLog> getPurchaseLogs() {
        return new ArrayList<>(this.purchaseLogs);
    }

}
