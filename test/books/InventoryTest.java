package books;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Inventory} class.
 *
 * @author Zachary Cook
 */
public class InventoryTest {
    private Book book1;
    private Book book2;
    private Book book3;
    private Inventory CuT;

    /**
     * Sets up the unit tests.
     */
    @BeforeEach
    public void setup() {
        // Create the authors and publishers.
        Author author1 = new Author("John","Doe");
        Author author2 = new Author("Jane","Doe");
        Publisher publisher1 = new Publisher("Publisher 1");
        Publisher publisher2 = new Publisher("Publisher 2");

        // Create a blank date.
        Date blankDate = new Date(0,0,0,0,0,0);

        // Create the books.
        this.book1 = new Book(author1,publisher1,1,blankDate,425,3,1,"Test Book");
        this.book2 = new Book(author1,publisher2,2,blankDate,271,7,7,"Test Book: The Sequel");
        this.book3 = new Book(author2,publisher2,3,blankDate,515,2,0,"Other Book");

        // Create the component under testing and register the books.
        this.CuT = new Inventory();
        this.CuT.registerBook(this.book1);
        this.CuT.registerBook(this.book2);
        this.CuT.registerBook(this.book3);
    }

    /**
     * Tests that the constructor works without failing.
     */
    @Test
    public void test_constructor() {
        new Inventory();
    }

    /**
     * Tests the {@link Inventory#getBook(long)} method.
     */
    @Test
    public void test_getBook() {
        assertEquals(CuT.getBook(1),this.book1,"Book is incorrect.");
        assertEquals(CuT.getBook(2),this.book2,"Book is incorrect.");
        assertEquals(CuT.getBook(3),this.book3,"Book is incorrect.");
        assertNull(CuT.getBook(4),"Book was returned.");
    }

    /**
     * Tests the {@link Inventory#getBooks(String, String, String, String)} method by name.
     */
    @Test
    public void test_getBooksName() {
        assertTrue(CuT.getBooks("Test Book","*","*","*").contains(this.book1),"Book 1 not returned.");
        assertTrue(CuT.getBooks("Test Book","*","*","*").contains(this.book2),"Book 2 not returned.");
        assertFalse(CuT.getBooks("Test Book","*","*","*").contains(this.book3),"Book 3 returned.");

        assertFalse(CuT.getBooks("Test Book: The Sequel","*","*","*").contains(this.book1),"Book 1 returned.");
        assertTrue(CuT.getBooks("Test Book: The Sequel","*","*","*").contains(this.book2),"Book 2 not returned.");
        assertFalse(CuT.getBooks("Test Book: The Sequel","*","*","*").contains(this.book3),"Book 3 returned.");
    }

    /**
     * Tests the {@link Inventory#getBooks(String, String, String, String)} method by author.
     */
    @Test
    public void test_getBooksAuthor() {
        assertTrue(CuT.getBooks("*","John Doe","*","*").contains(this.book1),"Book 1 not returned.");
        assertTrue(CuT.getBooks("*","John Doe","*","*").contains(this.book2),"Book 2 not returned.");
        assertFalse(CuT.getBooks("*","John Doe","*","*").contains(this.book3),"Book 3 returned.");

        assertFalse(CuT.getBooks("*","Jane Doe","*","*").contains(this.book1),"Book 1 returned.");
        assertFalse(CuT.getBooks("*","Jane Doe","*","*").contains(this.book2),"Book 2 returned.");
        assertTrue(CuT.getBooks("*","Jane Doe","*","*").contains(this.book3),"Book 3 not returned.");
    }

    /**
     * Tests the {@link Inventory#getBooks(String, String, String, String)} method by publisher.
     */
    @Test
    public void test_getBooksPublisher() {
        assertTrue(CuT.getBooks("*","*","*","Publisher 1").contains(this.book1),"Book 1 not returned.");
        assertFalse(CuT.getBooks("*","*","*","Publisher 1").contains(this.book2),"Book 2 returned.");
        assertFalse(CuT.getBooks("*","*","*","Publisher 1").contains(this.book3),"Book 3 returned.");

        assertFalse(CuT.getBooks("*","*","*","Publisher 2").contains(this.book1),"Book 1 returned.");
        assertTrue(CuT.getBooks("*","*","*","Publisher 2").contains(this.book2),"Book 2 not returned.");
        assertTrue(CuT.getBooks("*","*","*","Publisher 2").contains(this.book3),"Book 3 not returned.");
    }
}
