package time;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Class representing a date.
 *
 * @author Joey Zhen
 * @author Michael Dolan
 */
public class Date extends Time implements Serializable {
    private java.util.Date date;
    private int year;
    private int month;
    private int day;

    /**
     * Creates a data object.
     *
     * @param month the month of the date.
     * @param day the day of the date.
     * @param year the year of the date.
     * @param hours the hours of the date.
     * @param minutes the minutes of the date.
     * @param seconds the seconds of the date.
     */
    public Date(int month,int day,int year,int hours,int minutes,int seconds) {
        super(hours,minutes,seconds);

        // Create the date.
        this.date = new java.util.Date(year - 1900,month - 1,day,hours,minutes,seconds);

        // Store the date information.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the year of the date.
     *
     * @return the year of the date.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Returns the month of the date.
     *
     * @return the month of the date.
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Returns the day of the date.
     *
     * @return the day of the date.
     */
    public int getDay() {
        return this.day;
    }

   /**
     * Formats the date as YYYY/MM/DD.
     *
     * @return the formatted date.
     */
    public String formatDate() {
        // Determine the partial strings.
        String month = String.valueOf(this.month);
        if (month.length() == 1) {
            month = "0" + month;
        }

        String day = String.valueOf(this.day);
        if (day.length() == 1) {
            day = "0" + day;
        }

        // Create the new string.
        return this.year + "/" + month + "/" + day;
    }

    /**
     * Returns the timestamp in seconds.
     *
     * @return the timestamp in seconds.
     */
    public int getTimestamp() {
        // Create the calender object.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);

        // Return the total time in seconds.
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    /**
     * Creates a new time that has the time advanced.
     *
     * @param years the years to advance.
     * @param months the months to advance.
     * @param days the days to advance.
     * @param hours the hours to advance.
     * @param minutes the minutes to advance.
     * @param seconds the seconds to advance.
     */
    public Date advance(int years,int months,int days,int hours,int minutes,int seconds) {
        // Get the new hours, minutes, and seconds.
        int newHours = this.hours + hours;
        int newMinutes = this.minutes + minutes;
        int newSeconds = this.seconds + seconds;

        // Handle seconds overflow.
        newMinutes += newSeconds / 60;
        newSeconds = newSeconds % 60;

        // Handle minutes overflow.
        newHours += newMinutes / 60;
        newMinutes = newMinutes % 60;

        // Handle hours overflow.
        days += newHours / 24;
        newHours = newHours % 24;

        // Return the new time.
        return new Date(this.month + months,this.day + days,this.year + years,newHours,newMinutes,newSeconds);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.month + "/" + this.day + "/" + this.year + " " + super.toString();
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
        if (!(obj instanceof Date)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Date date = (Date) obj;
        return this.hashCode() == date.hashCode();
    }

    /**
     * Determines if the date is after the given date.
     *
     * @param otherDate the date to check.
     * @return if the date is given after the given date.
     */
    public boolean after(Date otherDate) {
        return otherDate.getTimestamp() < this.getTimestamp();
    }

    /**
     * Gets the difference in the number of days between two dates. The date given
     * as the parameter should be greater than for the difference to be positive.
     *
     * @param otherDate the date before this one to get he number of days between.
     * @return how many days are between two dates.
     */
    public int differenceInDays(Date otherDate) {
        // Determine the difference in seconds.
        int secondsBetween = otherDate.getTimestamp() - this.getTimestamp();

        // Convert and return the seconds to days.
        return secondsBetween / (60 * 60 * 24);
    }
}
