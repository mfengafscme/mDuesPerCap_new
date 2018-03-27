package org.afscme.enterprise.update.rebate;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.update.PersonReviewData;
import org.afscme.enterprise.update.ExceptionData;
/**
 * Holds the results of an apply update action for rebate files
 */
public class RebateReviewSummary {
    
    protected AffiliateIdentifier affiliateIdentifier;
    protected RebateChanges[] rebateChanges;
    protected PersonReviewData totals;
    /*********************************************************************************************/
    //array of exception datastructure to hold the exceptions
    protected ExceptionData[]       exceptionResult;
    //array of PersonReviewData to hold the Rebate summary
    protected PersonReviewData[]    rebateReviewData;
    /***********************************************************************************************/
    
    /** Getter for property affiliateIdentifier.
     * @return Value of property affiliateIdentifier.
     *
     */
    public AffiliateIdentifier getAffiliateIdentifier() {
        return affiliateIdentifier;
    }
    
    /** Setter for property affiliateIdentifier.
     * @param affiliateIdentifier New value of property affiliateIdentifier.
     *
     */
    public void setAffiliateIdentifier(AffiliateIdentifier affiliateIdentifier) {
        this.affiliateIdentifier = affiliateIdentifier;
    }
    
    /** Getter for property rebateChanges.
     * @return Value of property rebateChanges.
     *
     */
    public RebateChanges[] getRebateChanges() {
        return this.rebateChanges;
    }
    
    /** Setter for property rebateChanges.
     * @param rebateChanges New value of property rebateChanges.
     *
     */
    public void setRebateChanges(RebateChanges[] rebateChanges) {
        this.rebateChanges = rebateChanges;
    }
    
    /** Getter for property totals.
     * @return Value of property totals.
     *
     */
    public PersonReviewData getTotals() {
        return totals;
    }
    
    /** Setter for property totals.
     * @param totals New value of property totals.
     *
     */
    public void setTotals(PersonReviewData totals) {
        this.totals = totals;
    }
    /*************************************************************************************************/
    /** Getter for property exceptionResult.
     * @return Value of property exceptionResult.
     *
     */
    public ExceptionData[] getExceptionResult() {
        return this.exceptionResult;
    }
    
    /** Setter for property exceptionResult.
     * @param exceptionResult New value of property exceptionResult.
     *
     */
    public void setExceptionResult(ExceptionData[] exceptionResult) {
        this.exceptionResult = exceptionResult;
    }
    
    /** Getter for property officerReviewData.
     * @return rebateReviewData new Value of property rebateReviewData.
     *
     */
    public PersonReviewData[] getRebateReviewData() {
        return this.rebateReviewData;
    }
    
    /** Setter for property officerReviewData.
     * @param rebateReviewData New value of property rebateReviewData.
     *
     */
    public void setRebateReviewData(PersonReviewData[] rebateReviewData) {
        this.rebateReviewData = rebateReviewData;
    }
    /*******************************************************************************************************/
    
}
