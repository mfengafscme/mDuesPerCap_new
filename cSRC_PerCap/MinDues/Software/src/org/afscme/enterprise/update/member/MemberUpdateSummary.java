package org.afscme.enterprise.update.member;

import java.util.Map;
import java.util.HashMap;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.Codes;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.UpdateSummary;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.update.PersonReviewData;
/**
 * Represents the data for update summary.
 */
public class MemberUpdateSummary extends UpdateSummary {
    /**
     * Per affiliate statistics.
     * key -- affPk, value -- PersonReviewData
     */
    protected Map affUpdateSummary;
    
    protected PersonReviewData totalCounts;
    
    public MemberUpdateSummary() {
        super();
        affUpdateSummary = new HashMap();
        exceptions = new HashMap();        
    }
    
    public String toString() {
        return "MemberUpdateSummary[" + 
                    "affUpdateSummary=" + CollectionUtil.toString(affUpdateSummary) +
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
    /****************************************************************************************/
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
    /*******************************************************************************************/
}
