package books;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Book} class.
 *
 * @author Michael Dolan
 * @author Zachary Cook
 */
public class BookTest {
    private Book CuT;

    /**
     * Sets up the unit test.
     */
    @BeforeEach
    public void setUp() {
        // Create the author and publisher.
        Author author = new Author("John","Doe");
        Publisher publisher = new Publisher("Test Publisher");

        // Create the book information.
        int ISBN = 1;
        Date publishedDate = new Date(0,0,0,0,0,0);
        int pageCount = 1;
        int numCopies = 2;
        int numCopiesCheckedOut = 1;
        String name = "Test Book";

        // Create the CuT.
        this.CuT = new Book(author, publisher, ISBN, publishedDate, pageCount, numCopies, numCopiesCheckedOut, name);
    }

    /**
     * Tests the {@link Book#getISBN()} method.
     */
    @Test
    public void test_getISBN() {
        assertEquals(CuT.getISBN(), 1, "ISBN number is incorrect.");
    }

    /**
     * Tests the {@link Book#getPublishedDate()} method.
     */
    @Test
    public void test_getPublishedDate() {
        assertEquals(CuT.getPublishedDate(), new Date(0,0,0,0,0,0), "Publisher date is incorrect.");
    }

    /**
     * Tests the {@link Book#getPageCount()} method.
     */
    @Test
    public void test_getPageCount() {
        assertEquals(CuT.getPageCount(), 1, "Page count is incorrect.");
    }

    /**
     * Tests the {@link Book#getNumCopies()} method.
     */
    @Test
    public void test_getNumCopies() {
        assertEquals(CuT.getNumCopies(), 2, "Number of copies is incorrect.");
    }

    /**
     * Tests the {@link Book#getPublishedDate()} method.
     */
    @Test
    public void test_getPurchasedDate() {
        assertEquals(CuT.getPublishedDate(), new Date(0,0,0,0,0,0), "Purchase date is incorrect.");
    }

    /**
     * Tests the {@link Book#getNumCopiesCheckedOut()} method.
     */
    @Test
    public void test_getNumCopiesCheckedOut() {
        assertEquals(CuT.getNumCopiesCheckedOut(), 1, "Number of copies checked out is incorrect.");
    }

    /**
     * Tests the {@link Book#getAuthors()} method.
     */
    @Test
    public void test_getAuthor() {
        assertEquals(CuT.getAuthors().get(0), new Author("John","Doe"), "Author isn't correct.");
    }

    /**
     * Tests the {@link Book#getPublisher()} method.
     */
    @Test
    public void test_getPublisher() {
        assertEquals(CuT.getPublisher(), new Publisher("Test Publisher"), "Publisher is incorrect.");
    }

    /**
     * Tests the {@link Book#getName()} method.
     */
    @Test
    public void test_getName() {
        assertEquals(CuT.getName(), "Test Book","Name is incorrect.");
    }

    /**
     * Tests the {@link Book#equals(Object)} method.
     */
    @Test
    public void test_equals() {
        assertTrue(CuT.equals(CuT),"Books aren't equal.");
    }
}