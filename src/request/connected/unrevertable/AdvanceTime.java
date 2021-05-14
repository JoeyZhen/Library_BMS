package request.connected.unrevertable;

import request.Arguments;
import request.Parameter;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import time.Date;
import time.Time;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;

/**
 * Request for advancing the current time.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class AdvanceTime extends AccountRequest {
    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public AdvanceTime(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "advance";
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
        requiredParameters.add(new Parameter("number-of-days",Parameter.ParameterType.INTEGER));

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

        // Get the number of days to advance.
        Integer daysToAdvance = arguments.getNextInteger();
        if (daysToAdvance == null) {
            return this.sendResponse("days-not-a-number");
        }

        // Get the number of hours to advance.
        Integer hoursToAdvance = 0;
        if (arguments.hasNext()) {
            hoursToAdvance = arguments.getNextInteger();
            if (hoursToAdvance == null) {
                return this.sendResponse("hours-not-a-number");
            }
        }

        // Return an error if the days or hours are invalid.
        if (daysToAdvance < 0 || daysToAdvance > 7) {
            return this.sendResponse("invalid-number-of-days," + daysToAdvance);
        }
        if (hoursToAdvance < 0 || hoursToAdvance > 23) {
            return this.sendResponse("invalid-number-of-hours," + hoursToAdvance);
        }

        // Advance the time.
        Date beginningTime = services.getClock().getDate();
        services.getClock().advanceTime(daysToAdvance,hoursToAdvance);

        // End visits if it is past closing or the day has changed.
        Date endingTime = services.getClock().getDate();
        if (beginningTime.getDay() != endingTime.getDay() || endingTime.getHours() >= 19) {
            services.getVisitHistory().finishAllVisits(new Time(19,0,0));
        }

        // Return success.
        return this.sendResponse("success");
    }
}