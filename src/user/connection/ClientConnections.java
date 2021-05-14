package user.connection;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Stores the current connections.
 *
 * @author Joey Zhen
 */
public class ClientConnections implements Serializable {
    private int nextMaxId;
    private HashMap<Integer,Connection> connections;

    /**
     * Creates the connections.
     */
    public ClientConnections() {
        this.nextMaxId = 1;
        this.connections = new HashMap<>();
    }

    /**
     * Returns the next possible id for the connections.
     *
     * @return the next id.
     */
    private int getNextId() {
        // Return if an id below the max id is open.
        for (int i = 1; i <= nextMaxId; i++) {
            if (this.connections.get(i) == null) {
                return i;
            }
        }

        // Return the max id.
        return this.nextMaxId;
    }

    /**
     * Returns the connection for the id.
     *
     * @param id the connection of the id.
     * @return the connection for the id.
     */
    public Connection getConnection(int id) {
        return this.connections.get(id);
    }

    /**
     * Creates a new connection.
     *
     * @return the new connection.
     */
    public Connection createConnection() {
        // Get the next id and set the max id if needed.
        int nextId = this.getNextId();
        if (nextId >= this.nextMaxId) {
            this.nextMaxId = nextMaxId + 1;
        }

        // Create the connection.
        Connection newConnection = new Connection(nextId);
        this.connections.put(nextId,newConnection);

        // Return the connection.
        return newConnection;
    }

    /**
     * Closes a connection.
     *
     * @param id the id of the connection to close.
     */
    public void closeConnection(int id) {
        this.connections.remove(id);
    }
}