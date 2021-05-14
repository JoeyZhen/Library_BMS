package books.search;

import books.Book;
import books.Books;

/**
 * Filter for books based on the ISBN number.
 *
 * @author Zachary Cook
 */
public class ISBNFilter implements BookFilter {
    private BookFilter bookFilter;
    private String filterString;

    /**
     * Creates the filter.
     *
     * @param subFilter the filter to apply onto.
     * @param filterString the string to use for filtering.
     */
    public ISBNFilter(BookFilter subFilter,String filterString) {
        this.bookFilter = subFilter;
        this.filterString = filterString;
    }

    /**
     * Filters the stored books.
     *
     * @return the filtered books.
     */
    @Override
    public Books filterBooks() {
        // Return a copy of the books if the filter is "*".
        if (this.filterString.equals("*")) {
            return new Books(this.bookFilter.filterBooks());
        }

        // Create the base books.
        Books filteredBooks = new Books();

        // Iterate through the books.
        for (Book book : this.bookFilter.filterBooks()) {
            if (Long.toString(book.getISBN()).contains(this.filterString)) {
                filteredBooks.add(book);
            }
        }

        // Return the books.
        return filteredBooks;
    }
}