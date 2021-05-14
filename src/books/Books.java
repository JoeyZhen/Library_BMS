package books;

import books.search.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains a list of books. This is used for sorting.
 *
 * @author Zachary Cook
 */
public class Books extends ArrayList<Book> implements Serializable {
    /**
     * The sorting methods.
     */
    public enum SortingMethod {
        TITLE,
        PUBLISH_DATE,
        BOOK_STATUS
    }

    /**
     * Creates a list of books.
     */
    public Books() {
        super();
    }

    /**
     * Creates a list of books.
     *
     * @param list the list to copy.
     */
    public Books(List<Book> list) {
        super(list);
    }

    /**
     * Returns a book that matches the id.
     *
     * @param id the id to match.
     * @return the book for the id.
     */
    public Book getBookById(int id) {
        // Iterate through the books.
        for (Book book : this) {
            if (book.getId() == id) {
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
    public Books filterBooks(String title,String authors,String isbn,String publisher) {
        // Create the filters.
        RootContainer rootContainer = new RootContainer(this);
        TitleFilter titleFilter = new TitleFilter(rootContainer,title);
        AuthorFilter authorFilter = new AuthorFilter(titleFilter,authors);
        ISBNFilter isbnFilter = new ISBNFilter(authorFilter,isbn);
        PublisherFilter publisherFilter = new PublisherFilter(isbnFilter,publisher);

        // Return the books.
        return publisherFilter.filterBooks();
    }

    /**
     * Sorts the books by a certain method.
     *
     * @param method the method to sort.
     */
    public void sort(SortingMethod method) {
        // Sort by title.
        if (method == SortingMethod.TITLE) {
            this.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                }
            });
        }

        // Sort by published date.
        if (method == SortingMethod.PUBLISH_DATE) {
            this.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o2.getPublishedDate().getTimestamp() - o1.getPublishedDate().getTimestamp();
                }
            });
        }

        // Sort by book status.
        if (method == SortingMethod.BOOK_STATUS) {
            this.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return (o2.getNumCopies() - o2.getNumCopiesCheckedOut()) - (o1.getNumCopies() - o1.getNumCopiesCheckedOut());
                }
            });
        }
    }
}
