package request;

/**
 * Interface for an request that can be undone.
 *
 * @author Zachary Cook
 */
public interface Waypoint {
    /**
     * Undos the waypoint.
     *
     * @return if it was successful.
     */
    boolean undo();

    /**
     * Redos the waypoint. It should not be called without
     * performing the original request.
     *
     * @return if it was successful.
     */
    boolean redo();
}