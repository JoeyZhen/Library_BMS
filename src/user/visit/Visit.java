package user.visit;

import time.Date;
import time.Time;
import user.Visitor;

import java.io.Serializable;

/**
 * Class representing a visitor.
 *
 * @author Joey Zhen
 */
public class Visit implements Serializable {
    private int id;
    private Visitor visitor;
    private Date date;
    private Time timeOfDeparture;

    /**
     * Creates a visit that has ended.
     *
     * @param id the id of the visit.
     * @param visitor the visitor involved in the visit.
     * @param date the date and start time of the visit.
     * @param timeOfDeparture the time of departure.
     */
    public Visit(int id,Visitor visitor,Date date,Time timeOfDeparture) {
        this.id = id;
        this.visitor = visitor;
        this.date = date;
        this.timeOfDeparture = timeOfDeparture;
    }

    /**
     * Creates a visit that hasn't ended.
     *
     * @param id the id of the visit.
     * @param visitor the visitor involved in the visit.
     * @param date the date and start time of the visit.
     */
    public Visit(int id,Visitor visitor,Date date) {
        this.id = id;
        this.visitor = visitor;
        this.date = date;
    }

    /**
     * Returns the id of the visit.
     *
     * @return the id of the visit.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the visitor involved in the visit.
     *
     * @return the visitor involved in the visit.
     */
    public Visitor getVisitor() {
        return this.visitor;
    }

    /**
     * Returns the date and time of the visit.
     *
     * @return the date and time of the visit.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Returns the time of departure. This will be null
     * if the visit has not ended.
     *
     * @return the time of departure.
     */
    public Time getTimeOfDeparture() {
        return this.timeOfDeparture;
    }

    /**
     * Returns if the visit has ended.
     *
     * @return if the visit has ended.
     */
    public boolean hasEnded() {
        return this.getTimeOfDeparture() != null;
    }

    /**
     * Ends the visit.
     *
     * @param time the time the visit has ended at.
     */
    public void endVisit(Time time) {
        this.timeOfDeparture = time;
    }

    /**
     * Undoes ending the visit.
     */
    public void undoEndVisit() {
        this.timeOfDeparture = null;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        // Determine the base string to return.
        String visitString = this.visitor.toString() + " @ " + this.getDate().toString();

        // Add the end time if it exists.
        if (this.getTimeOfDeparture() != null) {
            visitString += " - " + this.getTimeOfDeparture().toString();
        }

        // Return the visit as a string.
        return visitString;
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
        if (!(obj instanceof Visit)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Visit visit = (Visit) obj;
        return this.hashCode() == visit.hashCode();
    }
}
