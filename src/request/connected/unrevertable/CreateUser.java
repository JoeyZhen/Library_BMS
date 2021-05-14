package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request that creates a new user.
 *
 * @author Zachary Cook
 */
public class CreateUser extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public CreateUser(Services services, Connection connection, Arguments arguments) {
        super(services,connection,arguments, User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "create";
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
        requiredParameters.add(new Parameter("role",Parameter.ParameterType.STRING));
        requiredParameters.add(new Parameter("visitor ID",Parameter.ParameterType.STRING));

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

        // Get the username, password, role, and visitor id.
        String username = arguments.getNextString();
        String password = arguments.getNextString();
        String role = arguments.getNextString().toLowerCase();
        String visitorId = arguments.getNextString();

        // Return an error if the username is a duplicate.
        if (services.getUserRegistry().getUser(username) != null) {
            return this.sendResponse("duplicate-username");
        }

        // Get the visitor and return an error if doesn't exist or has a user.
        Visitor visitor = services.getVisitorsRegistry().getVisitor(visitorId);
        if (visitor == null) {
            return this.sendResponse("invalid-visitor");
        }
        if (services.getUserRegistry().getUser(visitor) != null) {
            return this.sendResponse("duplicate-visitor");
        }

        // Determine the permission level.
        User.PermissionLevel permissionLevel = null;
        if (role.equals("visitor")) {
            permissionLevel = User.PermissionLevel.VISITOR;
        } else if (role.equals("employee")) {
            permissionLevel = User.PermissionLevel.EMPLOYEE;
        } else {
            return this.sendResponse("invalid-role");
        }

        // Create the user.
        services.getUserRegistry().registerUser(username,password,permissionLevel,visitor);

        // Format the request.response.
        return this.sendResponse("success");
    }
}
