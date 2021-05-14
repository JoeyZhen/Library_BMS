package user.connection;

import request.Request;
import request.RequestHistory;
import user.Visitor;

import java.io.Serializable;

/**
 * Class representing an active connection.
 *
 * @author Joey Zhen
 */
public class Connection implements Serializable {
    private int id;
    private User user;
    private RequestHistory requestHistory;

    /**
     * Creates a connection.
     *
     * @param id the id of the connection.
     */
    public Connection(int id) {
        this.id = id;
        this.requestHistory = new RequestHistory();
    }

    /**
     * Returns the id of the connection.
     *
     * @return the id of the connection.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the user of the connection. It
     * may be null.
     *
     * @return the user of the connection.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Associate the user with the connection.
     *
     * @param user the user to associate.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns if the connection can manipulate the data
     * for the given visitor.
     *
     * @param visitor the visitor to check.
     * @return if the connection can manipulate the data.
     */
    public boolean canManipulateVisitor(Visitor visitor) {
        // Return false if the connection doesn't have a user.
        if (this.getUser() == null) {
            return false;
        }

        // Return false if the users don't match and the user is a visitor.
        if (this.getUser().getVisitor() != visitor && this.getUser().getPermissionLevel() == User.PermissionLevel.VISITOR) {
            return false;
        }

        // Return true (success).
        return true;
    }

    /**
     * Returns the next request to undo.
     *
     * @return the next request to undo.
     */
    public Request getRequestToUndo() {
        return this.requestHistory.getRequestToUndo();
    }

    /**
     * Returns the next request to redo.
     *
     * @return the next request to redo.
     */
    public Request getRequestToRedo() {
        return this.requestHistory.getRequestToRedo();
    }

    /**
     * Adds a request that was done.
     *
     * @param request the request that was done.
     */
    public void addCompletedRequest(Request request) {
        this.requestHistory.addCompletedRequest(request);
    }

    /**
     * Moves the last undone requests to the requests to redo.
     */
    public void lastRequestUndone() {
        this.requestHistory.lastRequestUndone();
    }

    /**
     * Moves the last redone requests to the requests to undo.
     */
    public void lastRequestRedone() {
        this.requestHistory.lastRequestRedone();
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Connection " + this.getId();
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     *
     * @return true if this object is the same as the obj argument;
     * false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        // Return false if the class isn't the same.
        if (!(obj instanceof Connection)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Connection connection = (Connection) obj;
        return this.hashCode() == connection.hashCode();
    }
}
