package org.afscme.enterprise.update.officer;

import org.afscme.enterprise.update.PersonReviewData;

/**
 * Encapsulates the results of applying an officer update.
 */
public class OfficerReviewData extends PersonReviewData {
    
    /**
     * Number of vacancy-creations attempted
     */
//    protected int vacantAttempted;
    
    /**
     * Number of vacancies created
     */
//    protected int vacantCompleted;
    
    /** Getter for property vacantAttempted.
     * @return Value of property vacantAttempted.
     *
     */
    public int getVacantAttempted() {
        return vacantAttempted;
    }
    
    /** Setter for property vacantAttempted.
     * @param vacantAttempted New value of property vacantAttempted.
     *
     */
    public void setVacantAttempted(int vA) {
        vacantAttempted = vA;
    }
    
    public void incrementVacantAttempted() {
        vacantAttempted += 1;
    }
    
    public void decrementVacantAttempted() {
        vacantAttempted -= 1;
    }
    
    /** Getter for property vacantCompleted.
     * @return Value of property vacantCompleted.
     *
     */
    public int getVacantCompleted() {
        return vacantCompleted;
    }
    
    /** Setter for property vacantCompleted.
     * @param vacantCompleted New value of property vacantCompleted.
     *
     */
    public void setVacantCompleted(int vC) {
        vacantCompleted = vC;
    }
    
    public void incrementVacantCompleted() {
        vacantCompleted += 1;
    }
    
    public void decrementVacantCompleted() {
        vacantCompleted -= 1;
    }
    
}
