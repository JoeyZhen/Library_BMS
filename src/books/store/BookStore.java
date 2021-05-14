package books.store;

import books.Book;
import books.Books;

import java.io.Serializable;

/**
 * Class representing a "Book Store". This includes the books
 * that can be bought by the library.
 *
 * @author Zachary Cook
 */
public class BookStore implements Serializable {

    /**
     * Services that can be used for searching.
     */
    public enum SearchService {
        LOCAL,
        GOOGLE
    }

    private Books books;
    private SearchService currentService;
    private StoreSearchService localSearch;
    private StoreSearchService googleSearch;

    /**
     * Creates the book store.
     */
    public BookStore() {
        this.books = new Books();
        this.currentService = SearchService.LOCAL;

        // Create the searches.
        this.localSearch = LocalSearch.fromFile(books);
        this.googleSearch = new GoogleSearch(books);
    }

    /**
     * Sets the current service.
     *
     * @param service the new service.
     */
    public void setSearchService(SearchService service) {
        this.currentService = service;
    }

    /**
     * Returns the current book search service.
     *
     * @return the current book search service.
     */
    public StoreSearchService getSearchService() {
        // Return Google if it is set to Google.
        if (this.currentService == SearchService.GOOGLE) {
            return this.googleSearch;
        }

        // Return the local search.
        return this.localSearch;
    }

    /**
     * Returns the book for a given id.
     *
     * @param id the id to return.
     * @return the book with the id.
     */
    public Book getBook(int id) {
        return this.books.getBookById(id);
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
        return this.getSearchService().getBooks(title,authors,isbn,publisher);
    }
}
