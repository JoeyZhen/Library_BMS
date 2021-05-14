package request.connected.revertable;

import request.Arguments;
import request.Parameter;
import request.Waypoint;
import request.connected.AccountRequest;
import request.response.Response;
import time.Clock;
import system.Services;
import time.Date;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;
import user.visit.Visit;

import java.util.ArrayList;

/**
 * Request for getting the current date and time.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class EndVisit extends AccountRequest implements Waypoint {
    private boolean wasCompleted;
    private Visitor visitorPerformedOn;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public EndVisit(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.VISITOR);
        this.wasCompleted = false;
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "depart";
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
        requiredParameters.add(new Parameter("visitor-id",Parameter.ParameterType.STRING));

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
        this.wasCompleted = false;
        Arguments arguments = this.getArguments();
        Services services = this.getServices();
        Connection connection = this.getConnection();

        // Get the visitor.
        Visitor visitor = connection.getUser().getVisitor();
        if (arguments.hasNext()) {
            String visitorId = arguments.getNextString();
            visitor = services.getVisitorsRegistry().getVisitor(visitorId);

            if (visitor == null) {
                return this.sendResponse("invalid-id");
            }
        }

        // Return if the user isn't authorized for the visitor.
        if (!connection.canManipulateVisitor(visitor)) {
            return this.sendResponse("not-authorized");
        }

        // Get the current time.
        Clock clock = services.getClock();
        Date date = clock.getDate();
        String formattedTime = date.formatTime();

        // End the current visit and return an error if there is no visit..
        Visit visit = services.getVisitHistory().finishVisit(visitor,date);
        if (visit == null) {
            return this.sendResponse("invalid-id");
        }

        // Format the time difference.
        int visitDuration = visit.getTimeOfDeparture().getSeconds() - visit.getDate().getSeconds();
        String formattedHours = Integer.toString(visitDuration/3600);
        String formattedMinutes = Integer.toString((visitDuration/60) % 60);
        String formattedSeconds = Integer.toString(visitDuration % 60);
        if (formattedHours.length() == 1) {
            formattedHours = "0" + formattedHours;
        }
        if (formattedMinutes.length() == 1) {
            formattedMinutes = "0" + formattedMinutes;
        }
        if (formattedSeconds.length() == 1) {
            formattedSeconds = "0" + formattedSeconds;
        }

        // Return the request.response.
        this.wasCompleted = true;
        this.visitorPerformedOn = visitor;
        return this.sendResponse(visitor.getId() + "," + formattedTime + "," + formattedHours + ":" + formattedMinutes + ":" + formattedSeconds);
    }

    /**
     * Undos the waypoint.
     *
     * @return if it was successful.
     */
    @Override
    public boolean undo() {
        Services services = this.getServices();

        // Return if the request failed.
        if (!this.wasCompleted) {
            return false;
        }

        // Undo the visit.
        services.getVisitHistory().undoFinishVisit(this.visitorPerformedOn);
        this.wasCompleted = false;

        // Return true (success).
        return true;
    }

    /**
     * Redos the waypoint. It should not be called without
     * performing the original request.
     *
     * @return if it was successful.
     */
    @Override
    public boolean redo() {
        this.handleRequest();
        return true;
    }
}