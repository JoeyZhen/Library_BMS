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

import java.util.ArrayList;

/**
 * Request for a visitor beginning a visit.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class BeginVisit extends AccountRequest implements Waypoint {
    private Date currentDate;
    private boolean currentlyClosed;
    private boolean wasCompleted;
    private Visitor visitorPerformedOn;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public BeginVisit(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.VISITOR);
        this.wasCompleted = false;

        // Get the current date info.
        Clock clock = services.getClock();
        this.currentDate = clock.getDate();
        this.currentlyClosed = (clock.getTimeState() == Clock.TimeState.CLOSED);
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "arrive";
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
        String formattedDate = this.currentDate.formatDate();
        String formattedTime = this.currentDate.formatTime();

        // Return an error if the library is closed.
        if (this.currentlyClosed) {
            return this.sendResponse("closed");
        }

        // Return an error if there is an open visit.
        if (services.getVisitHistory().hasOpenVisit(visitor)) {
            return this.sendResponse("duplicate");
        }

        // Create the visit.
        services.getVisitHistory().addVisit(visitor,this.currentDate);
        this.wasCompleted = true;
        this.visitorPerformedOn = visitor;
        return this.sendResponse(visitor.getId() + "," + formattedDate + "," + formattedTime);
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
        services.getVisitHistory().undoAddVisit(this.visitorPerformedOn);
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