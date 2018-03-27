package org.afscme.enterprise.update.officer;

import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.officer.OfficerChanges;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents the data of an individual the officer pre update summary.
 */
public class OfficerPreUpdateSummary extends PreUpdateSummary {
    
    protected OfficerChanges officerChanges;
    
    /*******************************************************************************************/
    /**
     * Per affiliate statistics.
     * key -- AffiliateIdentifier, value -- OfficerChanges
     */
    // Note we did not use affPK as the key since some inFile affiliate may not have affPk (error).
    protected   Map             officerCounts;
     /**
     * the total statistics.
     * Note, this is not calculated and stored into the database when the backgroud process
     * is run. It is only used (calculated and displayed) for display purpose when the
     * user is reviewing the pre-process result.
     */
    protected   OfficerChanges  totalCounts;
    
    /**
     * A map to keep track of aggregated field value changes for all matched members of all affiliates.
     *
     * key -- field name(Common Code -- Integer), value -- FieldChange
     */
    protected   Map             fieldChanges;
    
    /**
     * A map to keep track of aggregated position value changes for all  of all affiliates.
     *
     * key -- affPK(Primarykey -- Integer), value -- PositionChanges
     */
    protected   Map             positionChanges;
    /*********************************************************************************************/
    
    
    
    /** Getter for property officerChanges.
     * @return Value of property officerChanges.
     *
     */
    public OfficerChanges getOfficerChanges() {
        return officerChanges;
    }
    
    /** Setter for property officerChanges.
     * @param officerChanges New value of property officerChanges.
     *
     */
    public void setOfficerChanges(OfficerChanges officerChanges) {
        this.officerChanges = officerChanges;
    }
    
    /********************************************************************************************/
    /** Getter for property officerCounts.
     * @return Value of property officerCounts.
     *
     */
    public Map getOfficerCounts() {
        return officerCounts;
    }
    
    /** Setter for property officerCounts.
     * @param officerCounts New value of property officerCounts.
     *
     */
    public void setOfficerCounts(Map officerCounts) {
        this.officerCounts = officerCounts;
    }
    
    /** Getter for property totalCounts.
     * @return Value of property totalCounts.
     *
     */
    public OfficerChanges getTotalCounts() {
        return totalCounts;
    }
    
    /** Setter for property totalCounts.
     * @param totalCounts New value of property totalCounts.
     *
     */
    public void setTotalCounts(OfficerChanges totalCounts) {
        this.totalCounts = totalCounts;
    }
    
    /** Getter for property fieldChanges.
     * @return Value of property fieldChanges.
     *
     */
    public Map getFieldChanges() {
        return fieldChanges;
    }
    
    /** Setter for property fieldChanges.
     * @param fieldChanges New value of property fieldChanges.
     *
     */
    public void setFieldChanges(Map fieldChanges) {
        this.fieldChanges = fieldChanges;
    }
    
    /** Getter for property PositionChanges.
     * @return Value positionChanges of property PositionChanges.
     *
     */
    public Map getPositionChanges() {
        return positionChanges;
    }
    
    /** Setter for property PositionChanges.
     * @param positionChanges New value of property PositionChanges.
     *
     */
    public void setPositionChanges(Map positionChanges) {
        this.positionChanges = positionChanges;
    }
    /**************************************************************************************************/
}
