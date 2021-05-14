package request.connected.unrevertable;

import books.store.BookStore;
import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request that sets the book store service.
 *
 * @author Zacharry Cook
 */
public class BookStoreService extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public BookStoreService(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments, User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "service";
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
        requiredParameters.add(new Parameter("info-service",Parameter.ParameterType.STRING));

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

        // Get the service name.
        String service = arguments.getNextString().toLowerCase();

        // Get the service.
        BookStore.SearchService searchService = null;
        if (service.equals("local")) {
            searchService = BookStore.SearchService.LOCAL;
        } else if (service.equals("google")) {
            searchService = BookStore.SearchService.GOOGLE;
        } else {
            return this.sendResponse("invalid-service");
        }

        // Set the service.
        this.getServices().getBookStore().setSearchService(searchService);

        // Return success.
        return this.sendResponse("success");
    }
}
