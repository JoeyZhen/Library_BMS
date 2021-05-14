package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.ConnectedRequest;
import request.response.Response;
import system.Services;
import user.connection.Connection;

import java.util.ArrayList;

/**
 * Request that disconnects a client.
 *
 * @author Zachary Cook
 */
public class Disconnect extends ConnectedRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public Disconnect(Services services, Connection connection, Arguments arguments) {
        super(services,connection,arguments);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "disconnect";
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

        // Remove the connection.
        services.getClientConnections().closeConnection(this.getConnection().getId());

        // Return the id.
        return this.sendResponse();
    }
}
