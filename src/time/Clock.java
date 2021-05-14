package time;

import java.io.Serializable;

/**
 * Class representing the "clock" of the system.
 *
 * @author Joey Zhen
 */
public class Clock implements Serializable {
    private Date currentDate;
    private TimeState currentState;

    /**
     * Time states for the library.
     */
    public enum TimeState {
        OPEN,
        CLOSED
    }

    /**
     * Creates the clock.
     */
    public Clock(Date date) {
        this.currentDate = date;
        this.updateTimeState();
    }

    /**
     * Creates a clock.
     */
    public Clock() {
        this(new Date(1,1,2019,8,0,0));
    }

    /**
     * Updates the time state of the clock.
     */
    private void updateTimeState() {
        if (this.currentDate.getHours() < 8 || this.currentDate.getHours() >= 19) {
            this.currentState = TimeState.CLOSED;
        } else {
            this.currentState = TimeState.OPEN;
        }
    }

    /**
     * Advances the system time.
     *
     * @param days the amount of days to advance.
     * @param hours the amount of hours to advance.
     */
    public void advanceTime(int days,int hours) {
        this.currentDate = this.currentDate.advance(0,0,days,hours,0,0);
        this.updateTimeState();
    }

    /**
     * Returns the current date.
     *
     * @return the current date.
     */
    public Date getDate() {
        return this.currentDate;
    }

    /**
     * Returns the current time state.
     *
     * @return the current time state.
     */
    public TimeState getTimeState() {
        return this.currentState;
    }
}
