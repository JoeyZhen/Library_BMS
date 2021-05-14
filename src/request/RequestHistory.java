package request;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores the history of requests.
 *
 * @author Zachary Cook
 */
public class RequestHistory implements Serializable {
    private ArrayList<Request> pastRequests;
    private ArrayList<Request> undoneRequests;

    /**
     * Creates the request history.
     */
    public RequestHistory() {
        this.pastRequests = new ArrayList<>();
        this.undoneRequests = new ArrayList<>();
    }

    /**
     * Returns the next request to undo.
     *
     * @return the next request to undo.
     */
    public Request getRequestToUndo() {
        // Return null if there are no requests.
        if (this.pastRequests.size() == 0) {
            return null;
        }

        // Return the request.
        return this.pastRequests.get(this.pastRequests.size() - 1);
    }

    /**
     * Returns the next request to redo.
     *
     * @return the next request to redo.
     */
    public Request getRequestToRedo() {
        // Return null if there are no requests.
        if (this.undoneRequests.size() == 0) {
            return null;
        }

        // Return the request.
        return this.undoneRequests.get(this.undoneRequests.size() - 1);
    }

    /**
     * Adds a request that was done.
     *
     * @param request the request that was done.
     */
    public void addCompletedRequest(Request request) {
        this.undoneRequests.clear();
        this.pastRequests.add(request);
    }

    /**
     * Moves the last undone requests to the requests to redo.
     */
    public void lastRequestUndone() {
        // Return if there is no command to undo.
        if (this.pastRequests.size() == 0) {
            return;
        }

        // Move the request.
        this.undoneRequests.add(this.pastRequests.remove(this.pastRequests.size() - 1));
    }

    /**
     * Moves the last redone requests to the requests to undo.
     */
    public void lastRequestRedone() {
        // Return if there is no command to undo.
        if (this.undoneRequests.size() == 0) {
            return;
        }

        // Move the request.
        this.pastRequests.add(this.undoneRequests.remove(this.undoneRequests.size() - 1));
    }
}
