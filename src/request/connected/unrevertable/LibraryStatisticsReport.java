package request.connected.unrevertable;

import books.Book;
import books.purchases.PurchaseLog;
import books.transactions.Transaction;
import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import time.Date;
import time.Time;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;
import user.visit.Visit;

import java.util.ArrayList;

/**
 * Request for getting a statistics report of the library.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class LibraryStatisticsReport extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public LibraryStatisticsReport(Services services, Connection connection, Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "report";
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

        // Get the day scope, if any.
        Integer dayLimit = Integer.MAX_VALUE;
        if (arguments.hasNext()) {
            dayLimit = arguments.getNextInteger();
            if (dayLimit == null) {
                return this.sendResponse("day-not-a-number");
            }
        }

        // Get the current date.
        Date currentDate = services.getClock().getDate();

        // Get the average visit length.
        int averageVisitLength = 0;
        int visits = 0;
        for (Visit visit : services.getVisitHistory().getFinishedVisits()) {
            if (visit.getDate().differenceInDays(currentDate) < dayLimit) {
                averageVisitLength = ((averageVisitLength * visits) + (visit.getTimeOfDeparture().getSeconds() - visit.getDate().getSeconds())) / (visits + 1);
                visits += 1;
            }
        }

        // Get the purchased books.
        int bookCount = 0;
        int purchasedBooks = 0;
        for (Book book : services.getBookInventory().getBooks()) {
            bookCount += book.getNumCopies();
        }

        for (PurchaseLog log : services.getPurchaseHistory().getPurchaseLogs()) {
            if (log.getPurchaseDate().differenceInDays(currentDate) < dayLimit) {
                purchasedBooks += 1;
            }
        }

        // Get the fines.
        int finesCollected = 0;
        int finesOutstanding = 0;

        for (Visitor visitor : services.getVisitorsRegistry().getVisitors()) {
            for (Transaction transaction : services.getTransactionHistory().getTransactionsByVisitor(visitor)) {
                if (transaction.getCheckedOut().differenceInDays(currentDate) < dayLimit || !transaction.getReturned() || transaction.getReturnedDate().differenceInDays(currentDate) < dayLimit) {
                    // Add the fines.
                    int finesPaid = transaction.getPartialLateFeePaid();
                    int finesOwed = (int) transaction.calculateFee(currentDate) - finesPaid;

                    finesCollected += finesPaid;
                    finesOutstanding += finesOwed;
                }
            }
        }

        // Format the average visit time.
        Time averageVisitTime  = new Time(0,0,0);
        averageVisitTime = averageVisitTime.advance(0,0,averageVisitLength);

        // Create the result.
        String result = currentDate.formatDate() + ","
            + "\n Number of Books: " + bookCount
            + "\n Number of Visitors: " + services.getVisitorsRegistry().getVisitors().size()
            + "\n Average Length of Visit: " + averageVisitTime.formatTime()
            + "\n Number of Books Purchased: " + purchasedBooks
            + "\n Fines Collected: " + finesCollected
            + "\n Fines Outstanding: " + finesOutstanding + "\n";

        // Return the result.
        return this.sendResponse(result);
    }
}