package books.search;

import books.Author;
import books.Book;
import books.Books;

import java.util.ArrayList;

/**
 * Filter for books based on the authors.
 *
 * @author Zachary Cook
 */
public class AuthorFilter implements BookFilter {
    private BookFilter bookFilter;
    private String filterString;

    /**
     * Creates the filter.
     *
     * @param subFilter the filter to apply onto.
     * @param filterString the string to use for filtering.
     */
    public AuthorFilter(BookFilter subFilter,String filterString) {
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

        // Split the authors.
        ArrayList<String> authors = new ArrayList<>();
        for (String authorName : this.filterString.split(",")) {
            authors.add(authorName.trim());
        }

        // Iterate through the books.
        for (Book book : this.bookFilter.filterBooks()) {
            // Determine if all authors are present.
            boolean authorsPresent = true;
            for (String author : authors) {
                // Determine if the author was found.
                boolean specificAuthorPresent = false;
                for (Author bookAuthor : book.getAuthors()) {
                    if (bookAuthor.getName().toLowerCase().contains(author.toLowerCase())) {
                        specificAuthorPresent = true;
                        break;
                    }
                }

                // Set the search as failed if the author wasn't found.
                if (!specificAuthorPresent) {
                    authorsPresent = false;
                    break;
                }
            }

            // Add the book if the authors exist.
            if (authorsPresent) {
                filteredBooks.add(book);
            }
        }

        // Return the books.
        return filteredBooks;
    }
}