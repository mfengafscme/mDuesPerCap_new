package org.afscme.enterprise.rebate;


/** Holds the data for the Political Rebate Summary By Year
 */
public class PRBSummaryByYear
{
    
    /**
     * Contains the Rebate Year
     */
    public Integer prbYear;
    
    /**
     * Contains the Rebate Status
     */
    public String prbStatus;
    
    /** Getter for property prbStatus.
     * @return Value of property prbStatus.
     *
     */
    public java.lang.String getPrbStatus() {
        return prbStatus;
    }    
    
    /** Setter for property prbStatus.
     * @param prbStatus New value of property prbStatus.
     *
     */
    public void setPrbStatus(java.lang.String prbStatus) {
        this.prbStatus = prbStatus;
    }
    
    /** Getter for property prbYear.
     * @return Value of property prbYear.
     *
     */
    public java.lang.Integer getPrbYear() {
        return prbYear;
    }
    
    /** Setter for property prbYear.
     * @param prbYear New value of property prbYear.
     *
     */
    public void setPrbYear(java.lang.Integer prbYear) {
        this.prbYear = prbYear;
    }
    
}
