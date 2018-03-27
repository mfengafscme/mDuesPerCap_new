package org.afscme.enterprise.update.rebate;

import java.util.Map;
import java.util.HashMap;
import org.afscme.enterprise.update.PersonReviewData;
import org.afscme.enterprise.update.UpdateSummary;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * Represents the data for update summary.
 */
public class RebateUpdateSummary extends UpdateSummary {

    /**
     * Per affiliate statistics.
     * key -- affPk, value -- PersonReviewData
     */
    protected Map affUpdateSummary;

    protected PersonReviewData totalCounts;

    /**
     * Per affiliate file date
     * key -- affPk, value -- Timestamp
     */
    protected Map affFileGenerated;
    
    /**
     * Map of RebateUpdateElement objects to be updated, by personPk.
     * key = personPk; value = RebateUpdateElement
     */
    protected Map updates; 
    

    public RebateUpdateSummary() {
        super();
        affUpdateSummary = new HashMap();
        affFileGenerated = new HashMap();
        updates = new HashMap();
        exceptions = new HashMap();
    }

    public String toString() {
        return "RebateUpdateSummary[" +
                "affUpdateSummary=" + CollectionUtil.toString(affUpdateSummary) +
                ", affFileGenerated=" + CollectionUtil.toString(affFileGenerated) +                
                ", updates=" + CollectionUtil.toString(updates) +
                ", exceptions=" + CollectionUtil.toString(exceptions) +
                "]"
        ;
    }

    /** Getter for property affUpdateSummary.
     * @return Value of property affUpdateSummary.
     *
     */
    public Map getAffUpdateSummary() {
        return affUpdateSummary;
    }

    /** Setter for property affUpdateSummary.
     * @param affUpdateSummary New value of property affUpdateSummary.
     *
     */
    public void setAffUpdateSummary(Map affUpdateSummary) {
        this.affUpdateSummary = affUpdateSummary;
    }

    /** Getter for property affFileGenerated.
     * @return Value of property affFileGenerated.
     *
     */
    public Map getAffFileGenerated() {
        return affFileGenerated;
    }

    /** Setter for property affFileGenerated.
     * @param affFileGenerated New value of property affFileGenerated.
     *
     */
    public void setAffFileGenerated(Map affFileGenerated) {
        this.affFileGenerated = affFileGenerated;
    }

    /** Getter for property totalCounts.
     * @return Value of property totalCounts.
     *
     */
    public PersonReviewData getTotalCounts() {
        return totalCounts;
    }

    /** Setter for property totalCounts.
     * @param totalCounts New value of property totalCounts.
     *
     */
    public void setTotalCounts(PersonReviewData totalCounts) {
        this.totalCounts = totalCounts;
    }
    /** Getter for property updates.
     * @return Value of property updates.
     *
     */
    public Map getUpdates() {
        return updates;
    }
    
    /** Setter for property updates.
     * @param updates New value of property updates.
     *
     */
    public void setUpdates(Map updates) {
        this.updates = updates;
    }
    
}
