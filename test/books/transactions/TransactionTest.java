package books.transactions;

import books.Author;
import books.Book;
import books.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.Date;
import user.Visitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Transaction} class.
 *
 * @author Zachary Cook
 */
public class TransactionTest {
    private Book book;
    private Visitor visitor;

    /**
     * Sets up the unit tests.
     */
    @BeforeEach
    public void setup() {
        // Create the authors and publishers.
        Author author = new Author("John","Doe");
        Publisher publisher = new Publisher("Publisher");

        // Create a blank date.
        Date blankDate = new Date(0,0,0,0,0,0);

        // Create the book.
        this.book = new Book(author,publisher,1,blankDate,425,3,1,"Test Book");

        // Create the visitor.
        this.visitor = new Visitor("0000000001","Jane","Doe","Address","1234567890");
    }

    /**
     * Test that the constructor works without failing.
     */
    @Test
    public void test_constructor() {
        new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));
    }

    /**
     * Test the {@link Transaction#getId()} method.
     */
    @Test
    public void test_getId() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Run the assertions.
        assertEquals(CuT.getId(),1,"Id is incorrect.");
    }

    /**
     * Test the {@link Transaction#getVisitor()} method.
     */
    @Test
    public void test_getVisitor() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Run the assertions.
        assertEquals(CuT.getVisitor(),this.visitor,"Visitor is incorrect.");
    }

    /**
     * Test the {@link Transaction#getBook()} method.
     */
    @Test
    public void test_getBook() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Run the assertions.
        assertEquals(CuT.getBook(),this.book,"Book is incorrect.");
    }

    /**
     * Test the {@link Transaction#getCheckedOut()} method.
     */
    @Test
    public void test_getCheckedOut() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Run the assertions.
        assertEquals(CuT.getCheckedOut(),new Date(1,2,2019,0,0,0),"Checked out date is incorrect.");
    }

    /**
     * Test the {@link Transaction#getCheckedOut()} method.
     */
    @Test
    public void test_getDueDate() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Run the assertions.
        assertEquals(CuT.getDueDate(),new Date(1,9,2019,0,0,0),"Due date is incorrect.");
    }

    /**
     * Test the {@link Transaction#calculateFee(Date)} method.
     */
    @Test
    public void test_calculateFee() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Run the assertions.
        assertEquals(CuT.calculateFee(new Date(1,6,2019,0,0,0)),0,"Fee is incorrect.");
        assertEquals(CuT.calculateFee(new Date(1,10,2019,0,0,0)),10,"Fee is incorrect.");
        assertEquals(CuT.calculateFee(new Date(1,17,2019,0,0,0)),12,"Fee is incorrect.");
        assertEquals(CuT.calculateFee(new Date(1,17,2020,0,0,0)),30,"Fee is incorrect.");
    }

    /**
     * Tests the late fee being paid.
     */
    @Test
    public void test_lateFeePaid() {
        // Create the component under testing.
        Transaction CuT = new Transaction(1,this.visitor,this.book,new Date(1,2,2019,0,0,0),new Date(1,9,2019,0,0,0));

        // Assert the late fee is not paid.
        assertFalse(CuT.getLateFeedPaid(),"Late fee was paid.");
        assertEquals(CuT.calculateFee(new Date(1,17,2019,0,0,0)),12,"Fee is incorrect.");

        // Pay the late fee and assert it was paid.
        CuT.setLateFeeAsPaid();
        assertTrue(CuT.getLateFeedPaid(),"Late fee wasn't paid.");
        assertEquals(CuT.calculateFee(new Date(1,17,2019,0,0,0)),0,"Fee is incorrect.");
    }
}
