package org.afscme.enterprise.update.officer;

import org.afscme.enterprise.update.ExceptionData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * Holds the results of an apply update action for officer files
 */
public class OfficerReviewSummary {
    
    protected ExceptionData[]       exceptionResult;
    protected OfficerReviewData[]   officerReviewData;
    protected OfficerReviewData     totals;
    protected AffiliateIdentifier[] affiliateIdentifier;
    
    /** Getter for property affiliateIdentifier.
     * @return Value of property affiliateIdentifier.
     *
     */
    public AffiliateIdentifier[] getAffiliateIdentifier() {
        return affiliateIdentifier;
    }
    
    /** Setter for property affiliateIdentifier.
     * @param affiliateIdentifier New value of property affiliateIdentifier.
     *
     */
    public void setAffiliateIdentifier(AffiliateIdentifier[] affiliateIdentifier) {
        this.affiliateIdentifier = affiliateIdentifier;
    }
    
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
     * @return officerReviewData new Value of property officerReviewData.
     *
     */
    public OfficerReviewData[] getOfficerReviewData() {
        return this.officerReviewData;
    }
    
    /** Setter for property officerReviewData.
     * @param officerReviewData New value of property officerReviewData.
     *
     */
    public void setOfficerReviewData(OfficerReviewData[] officerReviewData) {
        this.officerReviewData = officerReviewData;
    }
    
    /** Getter for property totals.
     * @return OfficerReviewData Value of property totals.
     *
     */
    public OfficerReviewData getTotals() {
        return totals;
    }
    
    /** Setter for property totals.
     * @param totals New value of property totals.
     *
     */
    public void setTotals(OfficerReviewData totals) {
        this.totals = totals;
    }
    
}
