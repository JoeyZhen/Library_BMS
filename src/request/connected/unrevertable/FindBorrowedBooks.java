package request.connected.unrevertable;

import books.Book;
import books.transactions.Transaction;
import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request for getting books borrowed by a visitor.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class FindBorrowedBooks extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public FindBorrowedBooks(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "borrowed";
    }

    /**
     * Returns a list of the required parameters.
     *
     * @return a list of the required parameters.
     */
    @Override
    public ArrayList<Parameter> getRequiredParameters() {
        return new ArrayList<>();
    }

    /**
     * Returns a response for the request.
     *
     * @return the response of the request.
     */
    @Override
    public Response handleRequest() {
        Arguments arguments = this.getArguments();
        Services services = this.getServices();
        Connection connection = this.getConnection();

        // Get the visitor.
        Visitor visitor = connection.getUser().getVisitor();
        if (arguments.hasNext()) {
            String visitorId = arguments.getNextString();
            visitor = services.getVisitorsRegistry().getVisitor(visitorId);

            if (visitor == null) {
                return this.sendResponse("invalid-visitor-id");
            }
        }

        // Determine the amount of unreturned books.
        ArrayList<Transaction> unreturnedBooks = new ArrayList<>();
        for (Transaction transaction : services.getTransactionHistory().getTransactionsByVisitor(visitor)) {
            if (!transaction.getReturned()) {
                unreturnedBooks.add(transaction);
            }
        }

        // Format the result.
        String result = Integer.toString(unreturnedBooks.size());
        for (Transaction transaction : unreturnedBooks) {
            Book book = transaction.getBook();
            result += "\n" + book.getId() + "," + book.getISBN() + "," + book.getName() + "," + transaction.getCheckedOut().formatDate();
        }

        // Return the request.response.
        return this.sendResponse(result);
    }
}