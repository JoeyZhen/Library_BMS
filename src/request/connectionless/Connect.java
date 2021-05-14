package request.connectionless;

import request.Arguments;
import request.Parameter;
import request.Request;
import request.response.Response;
import system.Services;
import user.connection.Connection;

import java.util.ArrayList;

/**
 * Request for creating a connection.
 *
 * @author Zachary Cook
 */
public class Connect extends Request {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public Connect(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "connect";
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
        Services services = this.getServices();

        // Create the connection.
        Connection connection = services.getClientConnections().createConnection();

        // Return the id.
        return this.sendResponse(Integer.toString(connection.getId()));
    }
}
