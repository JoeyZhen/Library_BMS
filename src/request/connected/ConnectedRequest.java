package request.connected;

import request.Arguments;
import request.Request;
import request.response.Response;
import system.Services;
import user.connection.Connection;

/**
 * Abstract class representing a request that requires a connection.
 *
 * @author Zachary Cook
 */
public abstract class ConnectedRequest extends Request {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public ConnectedRequest(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments);
    }

    /**
     * Sends a blank request.response.
     */
    @Override
    public Response sendResponse() {
        // Create the request.response string.
        String connectionId = Integer.toString(this.getConnection().getId());
        String responseString = connectionId + "," + this.getName() + ";";

        // Return the request.response.
        return new Response(responseString);
    }

    /**
     * Sends a general response.
     *
     * @param response the response message, ignoring the final semicolon.
     */
    @Override
    public Response sendResponse(String response) {
        // Create the request.response string.
        String connectionId = Integer.toString(this.getConnection().getId());
        String responseString = connectionId + "," + this.getName() + "," + response + ";";;

        // Return the request.response.
        return new Response(responseString);
    }

    /**
     * Gets the response for the request.
     */
    @Override
    public Response getResponse() {
        // Return an error if the connection doesn't exist.
        if (this.getConnection() == null) {
            return new Response("invalid-client-id;");
        }

        // Return the request.response.
        return super.getResponse();
    }
}
