package books;

import user.Name;

import java.io.Serializable;

/**
 * Class representing the author of a book.
 *
 * @author Zachary Cook
 */
public class Author implements Serializable {
    private Name name;

    /**
     * Creates an author.
     *
     * @param name the name of the author.
     */
    public Author(Name name) {
        this.name = name;
    }

    /**
     * Creates an author.
     *
     * @param firstName the first name of the author.
     * @param lastName the last name of the author.
     */
    public Author(String firstName,String lastName) {
        this(new Name(firstName,lastName));
    }

    /**
     * Returns the name of the author as a string.
     *
     * @return the name of the author as a string.
     */
    public String getName() {
        return this.name.toString();
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
        if (!(obj instanceof Author)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Author author = (Author) obj;
        return this.hashCode() == author.hashCode();
    }
}
