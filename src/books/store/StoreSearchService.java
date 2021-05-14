package books.store;

import books.Books;

/**
 * Interface for a book store searching service.
 *
 * @author Zachary Cook
 */
public interface StoreSearchService {
    /**
     * Returns the books for the given search.
     *
     * @param title the title of the book. To ignore this, leave it empty or use "*".
     * @param authors the authors of the book. To ignore this, leave it empty or use "*".
     * @param isbn the authors of the book. To ignore this, leave it empty or use "*".
     * @param publisher the publisher of the book. To ignore this, leave it empty or use "*".
     * @return the filtered books.
     */
    public Books getBooks(String title,String authors,String isbn,String publisher);
}
