package user.connection;

import user.Visitor;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class that stores the users in the system.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class UserRegistry implements Serializable {
    private HashMap<String,User> usersByName;
    private HashMap<Visitor,User> usersByVisitor;

    /**
     * Creates the user registry.
     */
    public UserRegistry() {
        this.usersByName = new HashMap<>();
        this.usersByVisitor = new HashMap<>();
    }

    /**
     * Adds a visitor to the registry.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param permissionLevel the permission level of the user.
     * @param visitor the visitor associated with the user.
     */
    public void registerUser(String username,String password,User.PermissionLevel permissionLevel,Visitor visitor) {
        // Throw an error if the user exists.
        if (this.getUser(username) != null) {
            throw new IllegalArgumentException("User already exists.");
        }

        // Add the user.
        User user = new User(username,password,permissionLevel,visitor);
        this.usersByName.put(username,user);
        this.usersByVisitor.put(visitor,user);
    }

    /**
     * Returns the user for the given username.
     *
     * @param username the username to check for.
     * @return the user with the username.
     */
    public User getUser(String username) {
        return this.usersByName.get(username);
    }

    /**
     * Returns the user for the given visitor.
     *
     * @param visitor the visitor to check for.
     * @return the user with the visitor.
     */
    public User getUser(Visitor visitor) {
        return this.usersByVisitor.get(visitor);
    }

    /**
     * Returns if the login is valid.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @return if the login is valid.
     */
    public boolean isLoginValid(String username,String password) {
        // Return false if the user doesn't exist.
        User user = this.getUser(username);
        if (user == null) {
            return false;
        }

        // Return based on the password.
        return user.passwordMatches(password);
    }
}
