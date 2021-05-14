package books.search;

import books.Books;

/**
 * Root container for filtering books. No filter is applied
 * since the purpose is to store the books used by filters.
 *
 * @author Zachary Cook
 */
public class RootContainer implements BookFilter {
    private Books books;

    /**
     * Creates the filter.
     *
     * @param books the books to store.
     */
    public RootContainer(Books books) {
        this.books = books;
    }

    /**
     * Filters the stored books.
     *
     * @return the filtered books.
     */
    @Override
    public Books filterBooks() {
        return this.books;
    }
}
