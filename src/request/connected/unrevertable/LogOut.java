package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request that logs out a user.
 *
 * @author Zachary Cook
 */
public class LogOut extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public LogOut(Services services, Connection connection, Arguments arguments) {
        super(services,connection,arguments, User.PermissionLevel.VISITOR);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "logout";
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

        // Set the connection's user to null.
        connection.setUser(null);

        // Format the request.response.
        return this.sendResponse("success");
    }
}
