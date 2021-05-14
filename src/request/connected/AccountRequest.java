package request.connected;

import request.Arguments;
import request.response.Response;
import system.Services;
import user.connection.Connection;
import user.connection.User;

/**
 * Abstract class representing a request that requires an account.
 *
 * @author Zachary Cook
 */
public abstract class AccountRequest extends ConnectedRequest {
    private User.PermissionLevel permissionLevel;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     * @param permissionLevel the permission level of the request.
     */
    public AccountRequest(Services services,Connection connection,Arguments arguments,User.PermissionLevel permissionLevel) {
        super(services,connection,arguments);
        this.permissionLevel = permissionLevel;
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

        // Return an error if the connection has no account or if the permission level is incorrect.
        User user = this.getConnection().getUser();
        if (user == null || !user.getPermissionLevel().meetsPermissionLevel(this.permissionLevel)) {
            return this.sendResponse("not-authorized");
        }

        // Return the request.response.
        return super.getResponse();
    }
}
