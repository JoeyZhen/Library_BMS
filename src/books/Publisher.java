package books;

import java.io.Serializable;

/**
 * Class representing the publisher of a book.
 *
 * @author Zachary Cook
 */
public class Publisher implements Serializable {
    private String name;

    /**
     * Creates a publisher.
     *
     * @param name the name of the publisher.
     */
    public Publisher(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the publisher as a string.
     *
     * @return the name of the publisher as a string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.getName();
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
        if (!(obj instanceof Publisher)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Publisher publisher = (Publisher) obj;
        return this.hashCode() == publisher.hashCode();
    }
}
