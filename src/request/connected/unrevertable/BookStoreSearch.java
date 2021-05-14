package request.connected.unrevertable;

import books.Author;
import books.Book;
import books.Books;
import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request for searching the book store.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class BookStoreSearch extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public BookStoreSearch(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "search";
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
        requiredParameters.add(new Parameter("title",Parameter.ParameterType.STRING));

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
        Arguments arguments = this.getArguments();
        Services services = this.getServices();

        // Get the title.
        String title = arguments.getNextString();

        // Get the authors, if any.
        String authorsString = "*";
        if (arguments.hasNext()) {
            authorsString = arguments.getNextString();
        }

        // Get the ISBN, if any.
        String isbn = "*";
        if (arguments.hasNext()) {
            isbn = arguments.getNextString();
        }

        // Get the publisher, if any.
        String publisher = "*";
        if (arguments.hasNext()) {
            publisher = arguments.getNextString();
        }

        // Get the sort order, if any.
        String sortOrder = "title";
        if (arguments.hasNext()) {
            sortOrder = arguments.getNextString();
        }

        // Validate the sort order.
        Books.SortingMethod sortingMethod;
        if (sortOrder.toLowerCase().equals("title")) {
            sortingMethod = Books.SortingMethod.TITLE;
        } else if (sortOrder.toLowerCase().equals("publish-date")) {
            sortingMethod = Books.SortingMethod.PUBLISH_DATE;
        } else {
            return this.sendResponse("invalid-sort-order");
        }

        // Search and sort the books.
        Books foundBooks = services.getBookStore().getBooks(title,authorsString,isbn,publisher);
        foundBooks.sort(sortingMethod);

        // Build the return string.
        String results = Integer.toString(foundBooks.size());
        for (Book book : foundBooks) {
            // Create the list of authors.
            String authorsList = "";
            for (Author author : book.getAuthors()) {
                if (!authorsList.equals("")) {
                    authorsList += ",";
                }

                authorsList += author.getName();
            }

            // Add the result.
            results += "\n" + book.getId() + "," + book.getISBN() + "," + book.getName() + ",{" + authorsList + "}," + book.getPublishedDate().formatDate() + ",";
        }

        // Return the result.
        return this.sendResponse(results);
    }
}