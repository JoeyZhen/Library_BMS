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
 * Request that undoes the last request.
 *
 * @author Zachary Cook
 */
public class Undo extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public Undo(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.VISITOR);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "undo";
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

        // Get the request to undo and return an error if there is nothing to undo.
        Request lastRequest = connection.getRequestToUndo();
        if (lastRequest == null) {
            return this.sendResponse("cannot-undo");
        }

        // Cast the requests and return an error if it isn't a waypoint request.
        Waypoint requestToUndo = null;
        if (lastRequest instanceof Waypoint) {
            requestToUndo = (Waypoint) lastRequest;
        } else {
            return this.sendResponse("cannot-undo");
        }

        // Undo the request and return an error if it failed.
        lastRequest.getArguments().resetPointer();
        boolean requestUndone = requestToUndo.undo();
        lastRequest.getArguments().resetPointer();
        if (!requestUndone) {
            return this.sendResponse("cannot-undo");
        }

        // Register the request as completed.
        connection.lastRequestUndone();

        // Return success.
        return this.sendResponse("success");
    }
}
