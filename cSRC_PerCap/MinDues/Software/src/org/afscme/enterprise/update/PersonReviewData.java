package org.afscme.enterprise.update;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;



/**
 * Represents an the data of an update summary.
 */
public class PersonReviewData {
    
    /** Number of transactions attempted */
    protected int transAttempted = 0;
    
    /** Number of transactions completed */
    protected int transCompleted = 0;
    
    /** Number of transactions in error */
    protected int transError = 0;
    
    /** Number of adds attempted */
    protected int addsAttempted = 0;
    
    /** Number of adds completed */
    protected int addsCompleted = 0;
    
    /** Number of inactivations attempted */
    protected int inacAttempted = 0;
    
    /** Number of inactivations completed */
    protected int inacCompleted = 0;
    
    /** Number of changes attempted */
    protected int changesAttempted = 0;
    
    /** Number of changes completed */
    protected int changesCompleted = 0;
    
    /** Number of T record count */
    protected int inacTCount = 0;
    
    /** Number of vacant offices attempted */
    protected int vacantAttempted = 0;
    
    /** Number of vacant offices completed */
    protected int vacantCompleted = 0;
    
    protected AffiliateIdentifier affiliateId;
    
    protected Integer affPk = null;
    
    public PersonReviewData() {
    }
    
    public String toString() {
        return "PersonReviewData[" + 
                    "addsAttempted=" + addsAttempted + 
                    ", addsCompleted=" + addsCompleted + 
                    ", changesAttempted=" + changesAttempted + 
                    ", changesCompleted=" + changesCompleted + 
                    ", inacAttempted=" + inacAttempted + 
                    ", inacCompleted=" + inacCompleted +
                    ", inacTCount=" + inacTCount +
                    ", transAttempted=" + transAttempted +
                    ", transCompleted=" + transCompleted +
                    ", transError=" + transError +
                    ", vacantAttempted=" + vacantAttempted +
                    ", vacantCompleted=" + vacantCompleted +
                "]"
        ;
    }
    
    /** Getter for property addsAttempted.
     * @return Value of property addsAttempted.
     *
     */
    public int getAddsAttempted() {
        return addsAttempted;
    }
    
    /** Setter for property addsAttempted.
     * @param addsAttempted New value of property addsAttempted.
     *
     */
    public void setAddsAttempted(int addsAttempted) {
        this.addsAttempted = addsAttempted;
    }
    
    public void incrementAddsAttempted() {
        this.addsAttempted += 1;
    }
    
    public void decrementAddsAttempted() {
        this.addsAttempted -= 1;
    }
    
    /** Getter for property addsCompleted.
     * @return Value of property addsCompleted.
     *
     */
    public int getAddsCompleted() {
        return addsCompleted;
    }
    
    /** Setter for property addsCompleted.
     * @param addsCompleted New value of property addsCompleted.
     *
     */
    public void setAddsCompleted(int addsCompleted) {
        this.addsCompleted = addsCompleted;
    }
    
    public void incrementAddsCompleted() {
        this.addsCompleted += 1;
    }
    
    public void decrementAddsCompleted() {
        this.addsCompleted -= 1;
    }
    
    /** Getter for property changesAttempted.
     * @return Value of property changesAttempted.
     *
     */
    public int getChangesAttempted() {
        return changesAttempted;
    }
    
    /** Setter for property changesAttempted.
     * @param changesAttempted New value of property changesAttempted.
     *
     */
    public void setChangesAttempted(int changesAttempted) {
        this.changesAttempted = changesAttempted;
    }
    
    public void incrementChangesAttempted() {
        this.changesAttempted += 1;
    }
    
    public void decrementChangesAttempted() {
        this.changesAttempted -= 1;
    }
    
    /** Getter for property changesCompleted.
     * @return Value of property changesCompleted.
     *
     */
    public int getChangesCompleted() {
        return changesCompleted;
    }
    
    /** Setter for property changesCompleted.
     * @param changesCompleted New value of property changesCompleted.
     *
     */
    public void setChangesCompleted(int changesCompleted) {
        this.changesCompleted = changesCompleted;
    }
    
    public void incrementChangesCompleted() {
        this.changesCompleted += 1;
    }
    
    public void decrementChangesCompleted() {
        this.changesCompleted -= 1;
    }
    
    /** Getter for property inacAttempted.
     * @return Value of property inacAttempted.
     *
     */
    public int getInacAttempted() {
        return inacAttempted;
    }
    
    /** Setter for property inacAttempted.
     * @param inacAttempted New value of property inacAttempted.
     *
     */
    public void setInacAttempted(int inacAttempted) {
        this.inacAttempted = inacAttempted;
    }
    
    public void incrementInacAttempted() {
        this.inacAttempted += 1;
    }
    
    public void decrementInacAttempted() {
        this.inacAttempted -= 1;
    }
    
    /** Getter for property inacCompleted.
     * @return Value of property inacCompleted.
     *
     */
    public int getInacCompleted() {
        return inacCompleted;
    }
    
    /** Setter for property inacCompleted.
     * @param inacCompleted New value of property inacCompleted.
     *
     */
    public void setInacCompleted(int inacCompleted) {
        this.inacCompleted = inacCompleted;
    }
    
    public void incrementInacCompleted() {
        this.inacCompleted += 1;
    }
    
    public void decrementInacCompleted() {
        this.inacCompleted -= 1;
    }
    
    /** Getter for property inacTCount.
     * @return Value of property inacTCount.
     *
     */
    public int getInacTCount() {
        return inacTCount;
    }
    
    /** Setter for property inacTCount.
     * @param inacTCount New value of property inacTCount.
     *
     */
    public void setInacTCount(int inacTCount) {
        this.inacTCount = inacTCount;
    }
    
    public void incrementInacTCount() {
        this.inacTCount += 1;
    }
    
    public void decrementInacTCount() {
        this.inacTCount -= 1;
    }
    
    /** Getter for property transAttempted.
     * @return Value of property transAttempted.
     *
     */
    public int getTransAttempted() {
        return transAttempted;
    }
    
    /** Setter for property transAttempted.
     * @param transAttempted New value of property transAttempted.
     *
     */
    public void setTransAttempted(int transAttempted) {
        this.transAttempted = transAttempted;
    }
    
    public void incrementTransAttempted() {
        this.transAttempted += 1;
    }
    
    public void decrementTransAttempted() {
        this.transAttempted -= 1;
    }
    
    /** Getter for property transCompleted.
     * @return Value of property transCompleted.
     *
     */
    public int getTransCompleted() {
        return transCompleted;
    }
    
    /** Setter for property transCompleted.
     * @param transCompleted New value of property transCompleted.
     *
     */
    public void setTransCompleted(int transCompleted) {
        this.transCompleted = transCompleted;
    }
    
    public void incrementTransCompleted() {
        this.transCompleted += 1;
    }
    
    public void decrementTransCompleted() {
        this.transCompleted -= 1;
    }
    
    /** Getter for property transError.
     * @return Value of property transError.
     *
     */
    public int getTransError() {
        return transError;
    }
    
    /** Setter for property transError.
     * @param transError New value of property transError.
     *
     */
    public void setTransError(int transError) {
        this.transError = transError;
    }
    
    public void incrementTransError() {
        this.transError += 1;
    }
    
    public void decrementTransError() {
        this.transError -= 1;
    }
    
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
    public void setVacantAttempted(int vacantAttempted) {
        this.vacantAttempted = vacantAttempted;
    }
    
    public void incrementVacantAttempted() {
        this.vacantAttempted += 1;
    }
    
    public void decrementVacantAttempted() {
        this.vacantAttempted -= 1;
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
    public void setVacantCompleted(int vacantCompleted) {
        this.vacantCompleted = vacantCompleted;
    }
    
    public void incrementVacantCompleted() {
        this.vacantCompleted += 1;
    }
    
    public void decrementVacantCompleted() {
        this.vacantCompleted -= 1;
    }
    
    /********************************************************************************************/
    /** Getter for property AffId.
     * @return Value of property AffId.
     *
     */
    public AffiliateIdentifier getAffId() {
        return affiliateId;
    }
    
    /** Setter for property AffId.
     * @param affId New value of property AffId.
     *
     */
    public void setAffId(AffiliateIdentifier affId) {
            this.affiliateId        = affId;
    }
    
    public void addToValues(PersonReviewData updates) {
        if (updates != null) {
            this.transAttempted     += updates.transAttempted;
            this.transCompleted     += updates.transCompleted;
            this.transError         += updates.transError;
            this.addsAttempted      += updates.addsAttempted;
            this.addsCompleted      += updates.addsCompleted;
            this.inacAttempted      += updates.inacAttempted;
            this.inacCompleted      += updates.inacCompleted;
            this.changesAttempted   += updates.changesAttempted;
            this.changesCompleted   += updates.changesCompleted;
            this.vacantAttempted    += updates.vacantAttempted;
            this.vacantCompleted    += updates.vacantCompleted;
           
        }
    }
    /********************************************************************************************/
    /** Getter for property AffPk.
     * @return Value AffPk of  property AffPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property AffId.
     * @param affId New value of property AffId.
     *
     */
    public void setAffPk(Integer affPk) {
            this.affPk        = affPk;
    }
    /************************************************************************************************/
}
