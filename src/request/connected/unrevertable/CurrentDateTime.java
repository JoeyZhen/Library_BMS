package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import time.Clock;
import system.Services;
import time.Date;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request for getting the current date and time.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class CurrentDateTime extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public CurrentDateTime(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "datetime";
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

        // Get the clock information.
        Clock clock = services.getClock();
        Date date = clock.getDate();
        String formattedDate = date.formatDate();
        String formattedTime = date.formatTime();

        // Format the request.response.
        return this.sendResponse(formattedDate + "," + formattedTime);
    }
}