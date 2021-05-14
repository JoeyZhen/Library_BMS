package user;

import time.Date;

import java.io.Serializable;

/**
 * Class representing a visitor.
 *
 * @author Joey Zhen
 */
public class Visitor implements Serializable {
    // The required length for user ids.
    public int USER_ID_LENGTH = 10;

    private String id;
    private Name name;
    private String address;
    private String phoneNumber;
    private Date registrationDate;

    /**
     * Creates a visitor.
     *
     * @param id the id of the visitor.
     * @param name the name of the visitor.
     * @param address the address of the visitor.
     * @param phoneNumber the phone number of the user.
     * @param registrationDate the regisration date of the user.
     */
    public Visitor(String id,Name name,String address,String phoneNumber,Date registrationDate) {
        // Throw an error if the id is the wrong length or contains non-numbers.
        if (!id.matches("[0-9]+")) {
            throw new IllegalArgumentException("Id contains non-number characters");
        }
        if (id.length() != USER_ID_LENGTH) {
            throw new IllegalArgumentException("Id is the incorrect length. (Contains " + id.length() + " numbers; must be " + USER_ID_LENGTH + " numbers)");
        }

        // Throw an error if the phone number contains non-numbers.
        if (!phoneNumber.matches("[0-9]+")) {
            throw new IllegalArgumentException("Phone number contains non-number characters");
        }

        // Set the values.
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    /**
     * Creates a visitor.
     *
     * @param id the id of the visitor.
     * @param name the name of the visitor.
     * @param address the address of the visitor.
     * @param phoneNumber the phone number of the user.
     */
    public Visitor(String id,Name name,String address,String phoneNumber) {
        this(id,name,address,phoneNumber,new Date(0,0,0,0,0,0));
    }

    /**
     * Creates a visitor.
     *
     * @param id the id of the visitor.
     * @param firstName the first name of the visitor.
     * @param lastName the last name of the visitor.
     * @param address the address of the visitor.
     * @param phoneNumber the phone number of the user.
     */
    public Visitor(String id,String firstName,String lastName,String address,String phoneNumber) {
        this(id,new Name(firstName,lastName),address,phoneNumber);
    }

    /**
     * Returns the id of the user.
     *
     * @return the id of the user.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public String getName() {
        return this.name.toString();
    }

    /**
     * Returns the address of the user.
     *
     * @return the address of the user.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return the phone number of the user.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Returns the registration date.
     *
     * @return the registration date.
     */
    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.getName() + " (" + this.getId() + ")";
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
        if (!(obj instanceof Visitor)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Visitor visitor = (Visitor) obj;
        return this.hashCode() == visitor.hashCode();
    }
}
