package request.connected.revertable;

import books.Book;
import books.Books;
import books.transactions.Transaction;
import request.Arguments;
import request.Parameter;
import request.Waypoint;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import time.Date;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Request for starting a loan of a book to a visitor.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class BorrowBook extends AccountRequest implements Waypoint {
    private boolean wasCompleted;
    private Date currentDate;
    private Date dueDate;
    private Visitor visitorAppliedTo;
    private ArrayList<Book> booksBorrowed;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public BorrowBook(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.VISITOR);
        this.wasCompleted = false;
        this.booksBorrowed = new ArrayList<>();

        // Get the current date and due date.
        this.currentDate = services.getClock().getDate();
        this.dueDate = new Date(currentDate.getMonth(),currentDate.getDay()+ 7,currentDate.getYear(),0,0,0);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "borrow";
    }

    /**
     * Returns a list of the required parameters.
     *
     * @return a list of the required parameters.
     */
    @Override
    public ArrayList<Parameter> getRequiredParameters() {
        // Create the required parameters.
        ArrayList<Parameter> requiredParameters = new ArrayList<>();
        requiredParameters.add(new Parameter("{id}",Parameter.ParameterType.LIST_OF_INTEGERS));

        // Return the required parameters.
        return requiredParameters;
    }


    /**
     * Returns a response for the request.
     *
     * @return the response of the request.
     */
    @Override
    public Response handleRequest() {
        this.wasCompleted = true;
        Arguments arguments = this.getArguments();
        Services services = this.getServices();
        Connection connection = this.getConnection();

        // Get the ids.
        HashMap<Integer,Integer> amountToBorrow = new HashMap<>();

        for (Integer id : arguments.getNextListAsIntegers()) {
            // Get the id.
            if (id == null) {
                return this.sendResponse("id-not-a-number");
            }

            // Add the id.
            if (amountToBorrow.containsKey(id)) {
                amountToBorrow.put(id,amountToBorrow.get(id) + 1);
            } else {
                amountToBorrow.put(id,1);
            }
        }

        // Get the visitor.
        Visitor visitor = connection.getUser().getVisitor();
        if (arguments.hasNext()) {
            String visitorId = arguments.getNextString();
            visitor = services.getVisitorsRegistry().getVisitor(visitorId);

            if (visitor == null) {
                return this.sendResponse("invalid-visitor-id");
            }
        }

        // Return if the user isn't authorized for the visitor.
        if (!connection.canManipulateVisitor(visitor)) {
            return this.sendResponse("not-authorized");
        }

        // Get the books to check out.
        Books books = new Books();
        for (int id : amountToBorrow.keySet()) {
            Book book = services.getBookInventory().getBook(services.getBookStore().getBook(id).getISBN());

            // Return an error if the book is invalid.
            if (book == null) {
                return this.sendResponse("invalid-book-id");
            }

            // Determine if there is enough copies and throw an error if there isn't.
            if ((book.getNumCopies() - book.getNumCopiesCheckedOut()) < amountToBorrow.get(id)) {
                return this.sendResponse("book-limit-exceeded");
            }

            // Add the book.
            for (int i = 0; i < amountToBorrow.get(id); i++) {
                books.add(book);
            }
        }

        // Determine the amount of unreturned books and unpaid fees.
        int unretunedBooks = 0;
        int unpaidBalance = 0;
        for (Transaction transaction : services.getTransactionHistory().getTransactionsByVisitor(visitor)) {
            if (!transaction.getReturned()) {
                unretunedBooks += 1;
            }
            if (!transaction.getLateFeedPaid()) {
                unpaidBalance += transaction.calculateFee(this.currentDate);
            }
        }

        // Return an error if there is an unpaid balance.
        if (unpaidBalance > 0) {
            return this.sendResponse("outstanding-fine," + unpaidBalance);
        }

        // Return an error if there is 5 or more books borrowed.
        if (unretunedBooks >= 5) {
            return this.sendResponse("book-limit-exceeded");
        }

        // Borrow the books.
        for (Book book : books) {
            book.borrowCopy();
            services.getTransactionHistory().registerTransaction(book,visitor,this.currentDate,this.dueDate);
            this.booksBorrowed.add(book);
        }

        // Return the request.response.
        this.visitorAppliedTo = visitor;
        this.wasCompleted = true;
        return this.sendResponse(this.dueDate.formatDate());
    }

    /**
     * Undos the waypoint.
     *
     * @return if it was successful.
     */
    @Override
    public boolean undo() {
        Services services = this.getServices();

        // Return if the request failed.
        if (!this.wasCompleted) {
            return false;
        }

        // Remove the books and purchase history.
        for (Book book : this.booksBorrowed) {
            book.returnCopy();
            services.getTransactionHistory().unregisterTransaction(book,this.visitorAppliedTo,this.currentDate,this.dueDate);
        }

        // Return true (success).
        this.wasCompleted = false;
        this.booksBorrowed.clear();
        return true;
    }

    /**
     * Redos the waypoint. It should not be called without
     * performing the original request.
     *
     * @return if it was successful.
     */
    @Override
    public boolean redo() {
        this.handleRequest();
        return true;
    }
}