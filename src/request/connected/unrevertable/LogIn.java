package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.ConnectedRequest;
import request.response.Response;
import system.Services;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request that logs in a user.
 *
 * @author Zachary Cook
 */
public class LogIn extends ConnectedRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public LogIn(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "login";
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
        requiredParameters.add(new Parameter("username",Parameter.ParameterType.STRING));
        requiredParameters.add(new Parameter("password",Parameter.ParameterType.STRING));

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
        Connection connection = this.getConnection();

        // Return an error if the connection already has a connection.
        if (connection.getUser() != null) {
            return this.sendResponse("already-logged-in");
        }

        // Get the username and password.
        String username = arguments.getNextString();
        String password = arguments.getNextString();

        // If the login is invalid, return an error.
        if (!services.getUserRegistry().isLoginValid(username,password)) {
            return this.sendResponse("bad-username-or-password");
        }

        // Set the connection's user.
        User user = services.getUserRegistry().getUser(username);
        connection.setUser(user);

        // Format the request.response.
        return this.sendResponse("success");
    }
}
