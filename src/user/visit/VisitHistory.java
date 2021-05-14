package user.visit;

import time.Date;
import time.Time;
import user.Visitor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores all visit history, and allows history for each user and each day to be fetched.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */

public class VisitHistory implements Serializable {
    private ArrayList<Visit> finishedVisits;
    private ArrayList<Visit> unfinishedVisits;
    private int visitId;

    /**
     * Creates the visit history.
     */
    public VisitHistory(){
        this.finishedVisits = new ArrayList<>();
        this.unfinishedVisits = new ArrayList<>();
        this.visitId = 0;
    }

    /**
     * Returns if the visitor has an open visit.
     *
     * @param visitor the visitor to check.
     */
    public boolean hasOpenVisit(Visitor visitor) {
        // Return true if an unfinished visit is found.
        for (Visit visit : this.unfinishedVisits) {
            if (visit.getVisitor().equals(visitor)) {
                return true;
            }
        }

        // Return false (visit not found).
        return false;
    }

    /**
     * Returns the finished visits.
     *
     * @return the finished visits.
     */
    public ArrayList<Visit> getFinishedVisits() {
        return new ArrayList<>(this.finishedVisits);
    }

    /**
     * Returns the unfinished visits.
     *
     * @return the unfinished visits.
     */
    public ArrayList<Visit> getUnfinishedVisits() {
        return new ArrayList<>(this.unfinishedVisits);
    }

    /**
     * This method adds a new incomplete visit to the unfinished visits list
     *
     * @param visitor the visitor who is visiting.
     * @param visitDate the date of the visit, includes start time.
     */
    public Visit addVisit(Visitor visitor,Date visitDate){
        // Create the visit.
        this.visitId = this.finishedVisits.size() + this.unfinishedVisits.size();
        Visit visit = new Visit(visitId, visitor, visitDate);

        // Register the visit as unfinished and return the visit.
        this.unfinishedVisits.add(visit);
        return visit;
    }

    /**
     * Undoes adding a visitor.
     *
     * @param visitor the visitor to remove the visit for.
     */
    public void undoAddVisit(Visitor visitor) {
        // Get the index to remove.
        int indexToRemove = -1;
        for (int i = 0; i < this.unfinishedVisits.size(); i++) {
            Visit visit = this.unfinishedVisits.get(i);
            if (visit.getVisitor() == visitor) {
                indexToRemove = i;
                break;
            }
        }

        // Remove the last index.
        if (indexToRemove != -1) {
            this.unfinishedVisits.remove(indexToRemove);
        }
    }

    /**
     * This method ends a certain visitors visit, and moves that visit from the
     * unfinishedVisits list to the finishedVisits list.
     *
     * @param visitor the visitor of the ended visit
     * @param visitEndTime the time of the ended visit
     * @return the visit, if any.
     */
    public Visit finishVisit(Visitor visitor, Time visitEndTime){
        // Get the unfinished visit.
        Visit visitToRemove = null;
        for (Visit visit : this.unfinishedVisits) {
            if (visit.getVisitor().equals(visitor)) {
                visit.endVisit(visitEndTime);
                visitToRemove = visit;
                break;
            }
        }

        // Remove the visit, if any.
        if (visitToRemove != null) {
            this.unfinishedVisits.remove(visitToRemove);
            this.finishedVisits.add(visitToRemove);
        }

        // Return null (no visit).
        return visitToRemove;
    }

    /**
     * Undoes finishing a visitor.
     *
     * @param visitor the visitor to remove the visit for.
     */
    public void undoFinishVisit(Visitor visitor) {
        // Get the index to remove.
        int indexToRemove = -1;
        for (int i = 0; i < this.finishedVisits.size(); i++) {
            Visit visit = this.finishedVisits.get(i);
            if (visit.getVisitor() == visitor) {
                indexToRemove = i;
            }
        }

        // Remove the last index.
        if (indexToRemove != -1) {
            Visit visit = this.finishedVisits.remove(indexToRemove);
            visit.undoEndVisit();
            this.unfinishedVisits.add(visit);
        }
    }

    /**
     * Finishes all of the unfinished visits.
     *
     * @param visitEndTime the time of the ended visit
     */
    public void finishAllVisits(Time visitEndTime) {
        // End the visits.
        for (Visit visit : this.unfinishedVisits) {
            visit.endVisit(visitEndTime);
        }

        // Move all the visits.
        this.finishedVisits.addAll(this.unfinishedVisits);
        this.unfinishedVisits.clear();
    }

    /**
     * This method calculates the average time for every completed visit to the library.
     *
     * @return returns the average time as a new time object
     */
    public Time averageVisitTime(){
        // Return 0 if there are no finished visits.
        if (this.finishedVisits.size() == 0) {
            return new Time(0);
        }

        // Calculate the total seconds of the visits.
        int totalSeconds = 0;
        for (Visit visit : this.finishedVisits) {
            totalSeconds += visit.getTimeOfDeparture().getSeconds() - visit.getDate().getSeconds();
        }

        // Determine and return the average.
        return new Time(totalSeconds/this.finishedVisits.size());
    }
}
