package books.search;

import books.Books;

/**
 * Interface for filtering books.
 *
 * @author Zachary Cook
 */
public interface BookFilter {
    /**
     * Filters the stored books.
     *
     * @return the filtered books.
     */
    Books filterBooks();
}
