package books.transactions;

import books.Author;
import books.Book;
import books.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.Date;
import user.Name;
import user.Visitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class for storing transactions.
 *
 * @author Michael Dolan
 * @author Zachary Cook
 */
class TransactionHistoryTest {

    private TransactionHistory CuT;
    private Book book1;
    private Book book2;
    private Book book3;
    private Visitor visitor1;
    private Visitor visitor2;
    private Visitor visitor3;

    /**
     * Sets up the unit tests.
     */
    @BeforeEach
    public void setup() {
        // Create the component under testing.
        CuT = new TransactionHistory();

        // Create the first book.
        Publisher publisher1 = new Publisher("publisher1");
        Date publishDate1 = new Date(0, 0, 0, 0, 0, 0);
        String book1Name = "book 1";
        Name author1Name = new Name("author", "one");
        Author author1 = new Author(author1Name);
        int ISBN1 = 10;
        int pageCount1 = 100;
        int numCopies1 = 11;
        int numCopiesCheckedOut1 = 11;
        book1 = new Book(author1, publisher1, ISBN1, publishDate1, pageCount1, numCopies1, numCopiesCheckedOut1, book1Name);

        // Create the the second book.
        Publisher publisher2 = new Publisher("publisher1");
        Date publishDate2 = new Date(0, 0, 0, 0, 0, 0);
        String book2Name = "book 1";
        Name author2Name = new Name("author", "one");
        Author author2 = new Author(author2Name);
        int ISBN2 = 10;
        int pageCount2 = 100;
        int numCopies2 = 11;
        int numCopiesCheckedOut2 = 11;
        book2 = new Book(author2, publisher2, ISBN2, publishDate2, pageCount2, numCopies2, numCopiesCheckedOut2, book2Name);

        // Create the third book.
        Publisher publisher3 = new Publisher("publisher1");
        Date publishDate3 = new Date(0, 0, 0, 0, 0, 0);
        String book3Name = "book 1";
        Name author3Name = new Name("author", "one");
        Author author3 = new Author(author3Name);
        int ISBN3 = 10;
        int pageCount3 = 100;
        int numCopies3 = 11;
        int numCopiesCheckedOut3 = 11;
        book2 = new Book(author3, publisher3, ISBN3, publishDate3, pageCount3, numCopies3, numCopiesCheckedOut3, book3Name);

        // Create the visitors.
        Name visitor1Name = new Name("visitor", "one");
        visitor1 = new Visitor("0000000001",visitor1Name,"V1Address", "1111111111");
        Name visitor2Name = new Name("visitor", "two");
        visitor2 = new Visitor("0000000002",visitor2Name,"V2Address", "1111111111");
        Name visitor3Name = new Name("visitor", "three");
        visitor3 = new Visitor("0000000003",visitor3Name,"V3Address", "1111111111");
    }

    /**
     * Tests the {@link TransactionHistory#registerTransaction(Transaction)} method and other getters.
     */
    @Test
    public void test_registerTransaction() {
        // Create the dates.
        Date out1 = new Date(1,1,1,1,1,1);
        Date in1 = new Date(2,2,2,2,2,2);

        // Create the transaction.
        Transaction transaction1 = new Transaction(1, visitor1, book1, out1, in1);
        CuT.registerTransaction(transaction1);

        // Run the assertions.
        assertEquals(CuT.getTransaction(1), transaction1,"Transaction is incorrect.");
        assertEquals(CuT.getTransactionsByVisitor(visitor1).get(0), transaction1,"Transaction is incorrect.");
        assertEquals(CuT.getTransactionsCheckedOutOn(out1).get(0), transaction1,"Transaction is incorrect.");
        assertEquals(CuT.getTransactionsDueOn(in1).get(0), transaction1,"Transaction is incorrect.");
    }
}