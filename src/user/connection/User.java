package user.connection;

import user.Visitor;

import java.io.Serializable;

/**
 * Class representing a user of the system.
 *
 * @author Joey Zhen
 */
public class User implements Serializable {
    private String username;
    private String password;
    private PermissionLevel permissionLevel;
    private Visitor visitor;

    /**
     * Permission levels a user can have.
     */
    public enum PermissionLevel {
        VISITOR(1),
        EMPLOYEE(2);

        protected int adminLevelId;

        /**
         * Creates an enum item.
         *
         * @param adminLevelId the admin level id.
         */
        PermissionLevel(int adminLevelId) {
            this.adminLevelId = adminLevelId;
        }

        /**
         * Returns if the permission level is above
         * or equals the given permission level.
         *
         * @param otherLevel the admin level to compare.
         */
        public boolean meetsPermissionLevel(PermissionLevel otherLevel) {
            return this.adminLevelId >= otherLevel.adminLevelId;
        }
    }

    /**
     * Creates a user.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param permissionLevel the permission level of the user.
     * @param visitor the associated visitor.
     */
    public User(String username,String password,PermissionLevel permissionLevel,Visitor visitor) {
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
        this.visitor = visitor;
    }

    /**
     * Returns the permission level of the user.
     *
     * @return the permission level of the user.
     */
    public PermissionLevel getPermissionLevel() {
        return this.permissionLevel;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the associated visitor.
     *
     * @return the associated visitor.
     */
    public Visitor getVisitor() {
        return this.visitor;
    }

    /**
     * Returns if the given password matches.
     *
     * @param password the password to compare.
     * @return if the password matches.
     */
    public boolean passwordMatches(String password) {
        return this.password.equals(password);
    }

    /**
     * Returns if the user has access to the permission level.
     *
     * @param permissionLevel the permission level to check.
     * @return if the user has access to the permission level.
     */
    public boolean hasAccess(PermissionLevel permissionLevel) {
        return this.permissionLevel.meetsPermissionLevel(permissionLevel);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.getUsername() + " (" + this.getPermissionLevel() + ")";
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
        if (!(obj instanceof User)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        User user = (User) obj;
        return this.hashCode() == user.hashCode();
    }
}
