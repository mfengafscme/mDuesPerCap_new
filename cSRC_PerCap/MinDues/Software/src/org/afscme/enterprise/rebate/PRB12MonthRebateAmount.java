package org.afscme.enterprise.rebate;


/** Holds the data for the 12 Month Rebate Amount
 */
public class PRB12MonthRebateAmount 
{
    
    /**
     * Contains the Rebate Year
     */
    public Integer prbYear;
    
    /**
     * Contains the Rebate Percentage.  This field only retains the percentage and is 
     * not used in any calculations.
     */
    public Double prbPercentage;
    
    /**
     * This is the annual amount to be paid to a member with a Full Time dues status.
     */
    public Double prbFullTime;
    
    /**
     * This is the annual amount to be paid to a member with a PartTime dues status.
     */
    public Double prbPartTime;
    
    /**
     * This is the annual amount to be paid to a member with a Lower PartTime dues 
     * status.
     */
    public Double prbLowerPartTime;
    
    /**
     * This is the annual amount to be paid to a member with a Retiree dues status.
     */
    public Double prbRetiree;
    
    /** Getter for property prbFullTime.
     * @return Value of property prbFullTime.
     *
     */
    public java.lang.Double getPrbFullTime() {
        return prbFullTime;
    }
    
    /** Setter for property prbFullTime.
     * @param prbFullTime New value of property prbFullTime.
     *
     */
    public void setPrbFullTime(java.lang.Double prbFullTime) {
        this.prbFullTime = prbFullTime;
    }
    
    /** Getter for property prbLowerPartTime.
     * @return Value of property prbLowerPartTime.
     *
     */
    public java.lang.Double getPrbLowerPartTime() {
        return prbLowerPartTime;
    }
    
    /** Setter for property prbLowerPartTime.
     * @param prbLowerPartTime New value of property prbLowerPartTime.
     *
     */
    public void setPrbLowerPartTime(java.lang.Double prbLowerPartTime) {
        this.prbLowerPartTime = prbLowerPartTime;
    }
    
    /** Getter for property prbPartTime.
     * @return Value of property prbPartTime.
     *
     */
    public java.lang.Double getPrbPartTime() {
        return prbPartTime;
    }
    
    /** Setter for property prbPartTime.
     * @param prbPartTime New value of property prbPartTime.
     *
     */
    public void setPrbPartTime(java.lang.Double prbPartTime) {
        this.prbPartTime = prbPartTime;
    }
    
    /** Getter for property prbPercentage.
     * @return Value of property prbPercentage.
     *
     */
    public java.lang.Double getPrbPercentage() {
        return prbPercentage;
    }
    
    /** Setter for property prbPercentage.
     * @param prbPercentage New value of property prbPercentage.
     *
     */
    public void setPrbPercentage(java.lang.Double prbPercentage) {
        this.prbPercentage = prbPercentage;
    }
    
    /** Getter for property prbRetiree.
     * @return Value of property prbRetiree.
     *
     */
    public java.lang.Double getPrbRetiree() {
        return prbRetiree;
    }
    
    /** Setter for property prbRetiree.
     * @param prbRetiree New value of property prbRetiree.
     *
     */
    public void setPrbRetiree(java.lang.Double prbRetiree) {
        this.prbRetiree = prbRetiree;
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
