package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import time.Clock;
import system.Services;
import time.Date;
import user.Name;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request for registering a visitor.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class RegisterVisitor extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public RegisterVisitor(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "register";
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
        requiredParameters.add(new Parameter("first-name",Parameter.ParameterType.STRING));
        requiredParameters.add(new Parameter("last-name", Parameter.ParameterType.STRING));
        requiredParameters.add(new Parameter("address", Parameter.ParameterType.STRING));
        requiredParameters.add(new Parameter("phone-number", Parameter.ParameterType.STRING));

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

        // Get the parameters.
        String firstName = arguments.getNextString();
        String lastName = arguments.getNextString();
        String address = arguments.getNextString();
        String phoneNumber = arguments.getNextString();

        // Check for an existing visitor and return an error if the visitor exists.
        Name visitorName = new Name(firstName,lastName);
        Visitor existingVisitor = services.getVisitorsRegistry().getVisitor(visitorName);
        if (existingVisitor != null && existingVisitor.getAddress().equals(address) && existingVisitor.getPhoneNumber().equals(phoneNumber)) {
            return this.sendResponse("duplicate");
        }

        // Get the current time.
        Clock clock = services.getClock();
        Date date = clock.getDate();
        String formattedDate = date.formatDate();
        String formattedTime = date.formatTime();

        // Register the visitor.
        Visitor visitor;
        try {
            visitor = services.getVisitorsRegistry().registerVisitor(visitorName,address,phoneNumber,date);
        } catch (IllegalArgumentException e) {
            return this.sendResponse("error," + e.getMessage());
        }

        // Return the request.response.
        return this.sendResponse(visitor.getId() + "," + formattedDate + " " + formattedTime);
    }
}