package request;

import request.connected.revertable.*;
import request.connected.unrevertable.*;
import request.connectionless.Connect;
import system.Services;
import user.connection.Connection;

/**
 * "Factory" for creating requests.
 *
 * @author Zachary Cook
 */
public class RequestCreator {
    private Services services;

    /**
     * Creates a request creator.
     *
     * @param services the services for the commands to use.
     */
    public RequestCreator(Services services) {
        this.services = services;
    }

    /**
     * Creates a request.
     *
     * @param name the name of the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public Request createRequest(String name,Connection connection,Arguments arguments) {
        name = name.toLowerCase();

        // Return a new request if one exists.
        switch (name) {
            case "connect":
                return new Connect(this.services,connection,arguments);
            case "disconnect":
                return new Disconnect(this.services,connection,arguments);
            case "create":
                return new CreateUser(this.services,connection,arguments);
            case "login":
                return new LogIn(this.services,connection,arguments);
            case "logout":
                return new LogOut(this.services,connection,arguments);
            case "undo":
                return new Undo(this.services,connection,arguments);
            case "redo":
                return new Redo(this.services,connection,arguments);
            case "service":
                return new BookStoreService(this.services,connection,arguments);
            case "register":
                return new RegisterVisitor(this.services,connection,arguments);
            case "arrive":
                return new BeginVisit(this.services,connection,arguments);
            case "depart":
                return new EndVisit(this.services,connection,arguments);
            case "info":
                return new LibraryBookSearch(this.services,connection,arguments);
            case "borrow":
                return new BorrowBook(this.services,connection,arguments);
            case "borrowed":
                return new FindBorrowedBooks(this.services,connection,arguments);
            case "return":
                return new ReturnBook(this.services,connection,arguments);
            case "pay":
                return new PayFine(this.services,connection,arguments);
            case "search":
                return new BookStoreSearch(this.services,connection,arguments);
            case "buy":
                return new BookPurchase(this.services,connection,arguments);
            case "advance":
                return new AdvanceTime(this.services,connection,arguments);
            case "datetime":
                return new CurrentDateTime(this.services,connection,arguments);
            case "report":
                return new LibraryStatisticsReport(this.services,connection,arguments);
        }

        // Return null (no match).
        return null;
    }
}
