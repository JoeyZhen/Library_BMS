package request;

import request.response.Response;
import system.Services;
import user.connection.Connection;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract class representing a request.
 *
 * @author Zachary Cook
 */
public abstract class Request implements Serializable {
    private Services services;
    private Connection connection;
    private Arguments arguments;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public Request(Services services,Connection connection,Arguments arguments) {
        this.services = services;
        this.connection = connection;
        this.arguments = arguments;
    }

    /**
     * Returns the services used by the request.
     *
     * @return the services used by the request.
     */
    public Services getServices() {
        return this.services;
    }

    /**
     * Returns the connection used by the request.
     *
     * @return the connection used by the request.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Returns the arguments used by the request.
     *
     * @return the arguments used by the request.
     */
    public Arguments getArguments() {
        return this.arguments;
    }

    /**
     * Sends a blank response.
     *
     * @return a blank response.
     */
    public Response sendResponse() {
        return new Response(this.getName() + ";");
    }

    /**
     * Sends a general response.
     *
     * @param response the response message, ignoring the final semicolon.
     */
    public Response sendResponse(String response) {
        // Create the response string.
        String responseString = this.getName() + "," + response + ";";

        // Return the response.
        return new Response(responseString);
    }

    /**
     * Sends a missing parameters response.
     *
     * @param missingParameters the missing parameters as "param-1,param-2,param-3"
     */
    public Response sendMissingParametersResponse(String missingParameters) {
        return this.sendResponse("missing-parameters,{" + missingParameters + "}");
    }

    /**
     * Creates a response for missing parameters.
     *
     * @param parameters the parameters to compile.
     * @param startIndex the start index of the missing parameters.
     */
    public Response sendMissingParametersResponse(ArrayList<Parameter> parameters,int startIndex) {
        // Assemble the string.
        String arguments = "";
        for (int i = startIndex; i < parameters.size(); i++) {
            // Add the comma.
            if (!arguments.equals("")) {
                arguments += ",";
            }

            // Add the parameter.
            arguments += parameters.get(i).getName();
        }

        // Create and return the response.
        return this.sendMissingParametersResponse(arguments);
    }

    /**
     * Gets the response for the request.
     */
    public Response getResponse() {
        // Determine if any parameters are missing.
        ArrayList<Parameter> requiredParameters = this.getRequiredParameters();
        for (int i = 0; i < requiredParameters.size(); i++) {
            // Return if the parameter is missing.
            if (!arguments.hasNext()) {
                return this.sendMissingParametersResponse(this.getRequiredParameters(),i);
            }

            // Increment the pointer.
            arguments.offsetPointer(1);
        }
        arguments.resetPointer();

        // Return the response.
        return this.handleRequest();
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    public abstract String getName();

    /**
     * Returns a list of the required parameters.
     *
     * @return a list of the required parameters.
     */
    public abstract ArrayList<Parameter> getRequiredParameters();

    /**
     * Returns a response for the request.
     *
     * @return the response of the request.
     */
    public abstract Response handleRequest();
}
