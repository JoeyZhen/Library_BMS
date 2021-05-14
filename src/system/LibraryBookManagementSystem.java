package system;

import request.*;
import request.response.Response;
import user.connection.Connection;

/**
 * Class representing the library book management system. This acts
 * as the "facade" of the system for interacting with the system.
 *
 * @author Zachary Cook
 */
public class LibraryBookManagementSystem {
    // The default file location for saving the services.
    public static String SERVICES_SAVE_LOCATION = "services_save";

    protected Services services;
    private RequestCreator requestCreator;

    /**
     * Creates the library book management system.
     *
     * @param services the services to use.
     */
    public LibraryBookManagementSystem(Services services) {
        // Store the services.
        this.services = services;

        // Create the requests creator.
        this.requestCreator = new RequestCreator(this.services);
    }

    /**
     * Creates the library book management system.
     */
    public LibraryBookManagementSystem() {
        this(new Services());
    }

    /**
     * Returns the services used. This is intended to use
     * with behavior testing for requests.
     *
     * @return the services used.
     */
    protected Services getServices() {
        return this.services;
    }

    /**
     * Performs a request and returns a request.response as
     * a string.
     *
     * @param request the request to make.
     * @return the request.response.
     */
    public String performRequest(String request) {
        // Create the argument parser.
        Arguments argumentParser;
        try {
            argumentParser = new Arguments(request);
        } catch (IllegalArgumentException exception) {
            // Handle incomplete arguments (no ending semicolon).
            return "partial-request;";
        }

        // Determine the connection.
        Connection connection = null;
        if (argumentParser.hasNext()) {
            Integer clientId = argumentParser.getNextInteger();
            if (clientId != null) {
                connection = this.services.getClientConnections().getConnection(clientId);
            } else {
                argumentParser.resetPointer();
            }
        }

        // Create the request.
        if (!argumentParser.hasNext()) {
            return "partial-request;";
        }
        Request requestObject = this.requestCreator.createRequest(argumentParser.getNextString(),connection,argumentParser.cloneFromCurrentPointer());
        if (requestObject == null) {
            return "invalid-request;";
        }

        // Store the request if a connection exists.
        if (connection != null && !requestObject.getName().equals("undo") && !requestObject.getName().equals("redo")) {
            connection.addCompletedRequest(requestObject);
        }

        // Run the request and get a request.response.
        Response response = requestObject.getResponse();
        String responseString = response.getResponse();

        // Return the request.response.
        return responseString;
    }
}
