package request.connected.revertable;

import books.Author;
import books.Book;
import request.Arguments;
import request.Parameter;
import request.Waypoint;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import time.Date;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Request for purchasing new books.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class BookPurchase extends AccountRequest implements Waypoint {
    private boolean wasCompleted;
    private Date currentDate;
    private ArrayList<Book> purchasedBooks;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public BookPurchase(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
        this.wasCompleted = false;
        this.currentDate = services.getClock().getDate();
        this.purchasedBooks = new ArrayList<>();
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "buy";
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
        requiredParameters.add(new Parameter("quantity",Parameter.ParameterType.INTEGER));
        requiredParameters.add(new Parameter("id", Parameter.ParameterType.REMAINING_INTEGERS));

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
        this.wasCompleted = false;
        Arguments arguments = this.getArguments();
        Services services = this.getServices();

        // Get the quantity.
        int quantity = arguments.getNextInteger();

        // Get the ids.
        if (!arguments.hasNext()) {
            return this.sendMissingParametersResponse("id");
        }
        HashMap<Integer,Integer> amountToBuy = new HashMap<>();

        for (Integer id : arguments.getRemainingIntegers()) {
            // Add the id.
            if (amountToBuy.containsKey(id)) {
                amountToBuy.put(id,amountToBuy.get(id) + quantity);
            } else {
                amountToBuy.put(id,quantity);
            }
        }

        // Add the books.
        for (int id : amountToBuy.keySet()) {
            Book book = services.getBookStore().getBook(id);

            for (int i = 0; i < amountToBuy.get(id); i++) {
                services.getBookInventory().registerBook(book);
                services.getBookInventory().getBook(book.getISBN()).addCopy();
                services.getPurchaseHistory().registerPurchase(book,this.currentDate);
                this.purchasedBooks.add(book);
            }
        }

        // Build the return string.
        String results = Integer.toString(amountToBuy.keySet().size());
        for (int id : amountToBuy.keySet()) {
            Book book = services.getBookStore().getBook(id);

            // Create the list of authors.
            String authorsList = "";
            for (Author author : book.getAuthors()) {
                if (!authorsList.equals("")) {
                    authorsList += ",";
                }

                authorsList += author.getName();
            }

            // Add the result.
            results += "\n" + book.getISBN() + "," + book.getName() + ",{" + authorsList + "}," + book.getPublishedDate().formatDate() + "," + amountToBuy.get(id) + ",";
        }

        // Return the result.
        this.wasCompleted = true;
        return this.sendResponse(results);
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
        for (Book book : this.purchasedBooks) {
            services.getBookInventory().getBook(book.getISBN()).removeCopy();
            services.getPurchaseHistory().unregisterPurchase(book,this.currentDate);
        }

        // Return true (success).
        this.wasCompleted = false;
        this.purchasedBooks.clear();
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