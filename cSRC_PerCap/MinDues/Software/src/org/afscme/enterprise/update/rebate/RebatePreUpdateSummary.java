package org.afscme.enterprise.update.rebate;

import java.util.Map;
import java.util.HashMap;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * Represents the data of an individual rebate import pre update summary.
 */
public class RebatePreUpdateSummary extends PreUpdateSummary {
    
    protected RebateChanges rebateChanges;  //is still needed??
    
    /*******************************************************************************************/
    /**
     * Per affiliate statistics.
     * key -- AffiliateIdentifier, value -- RebateChanges
     */
    // Note we did not use affPK as the key since some inFile affiliate may not have affPk (error).
    protected Map rebateCounts;
    
     /**
     * the total statistics.
     * Note, this is not calculated and stored into the database when the backgroud process
     * is run. It is only used (calculated and displayed) for display purpose when the
     * user is reviewing the pre-process result.
     */
    protected RebateChanges  totalCounts;

   
    /************************************************************************************************/

    public RebatePreUpdateSummary() {
        super();
        rebateCounts = new HashMap();
        totalCounts = new RebateChanges();
        exceptions = new HashMap();
    }
    
    public String toString() {
        return "RebatePreUpdateSummary [" +
            "\n\t rebateCounts=" + CollectionUtil.toString(rebateCounts) +
            "\n\t exceptions=" + CollectionUtil.toString(exceptions) +
            "\n\t totalCounts=" + totalCounts +
            "\n]"
        ;
    }

   
    /** Getter for property rebateChanges.
     * @return Value of property rebateChanges.
     *
     */
    public RebateChanges getRebateChanges() {
        return rebateChanges;
    }
    
    /** Setter for property rebateChanges.
     * @param rebateChanges New value of property rebateChanges.
     *
     */
    public void setRebateChanges(RebateChanges rebateChanges) {
        this.rebateChanges = rebateChanges;
    }
    
    /********************************************************************************************/
    /** Getter for property rebateCounts.
     * @return Value of property rebateCounts.
     *
     */
    public Map getRebateCounts() {
        return rebateCounts;
    }
    
    /** Setter for property rebateCounts.
     * @param officerCounts New value of property rebateCounts.
     *
     */
    public void setRebateCounts(Map rebateCounts) {
        this.rebateCounts = rebateCounts;
    }
    
    /** Getter for property totalCounts.
     * @return Value of property totalCounts.
     *
     */
    public RebateChanges getTotalCounts() {
        return totalCounts;
    }
    
    /** Setter for property totalCounts.
     * @param totalCounts New value of property totalCounts.
     *
     */
    public void setTotalCounts(RebateChanges totalCounts) {
        this.totalCounts = totalCounts;
    }
}
