package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.Request;
import request.Waypoint;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request for redoing requests.
 *
 * @author Zachary Cook
 */
public class Redo extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public Redo(Services services, Connection connection, Arguments arguments) {
        super(services,connection,arguments, User.PermissionLevel.VISITOR);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "redo";
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
        Connection connection = this.getConnection();

        // Get the request to redo and return an error if there is nothing to undo.
        Request lastUndoneRequest = connection.getRequestToRedo();
        if (lastUndoneRequest == null) {
            return this.sendResponse("cannot-redo");
        }

        // Cast the requests and return an error if it isn't a waypoint request.
        Waypoint requestToRedo = null;
        if (lastUndoneRequest instanceof Waypoint) {
            requestToRedo = (Waypoint) lastUndoneRequest;
        } else {
            return this.sendResponse("cannot-redo");
        }

        // Undo the request and return an error if it failed.
        lastUndoneRequest.getArguments().resetPointer();
        boolean requestUndone = requestToRedo.redo();
        lastUndoneRequest.getArguments().resetPointer();
        if (!requestUndone) {
            return this.sendResponse("cannot-undo");
        }

        // Register the request as completed.
        connection.lastRequestRedone();

        // Return success.
        return this.sendResponse("success");
    }
}
