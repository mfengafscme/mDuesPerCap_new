package org.afscme.enterprise.update.officer;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * Holds all the information about updates to a single affiliate in a review 
 * summary
 */
public class OfficerUpdateResult {
    protected OfficerChanges[] officerChanges;
    protected AffiliateIdentifier affiliateIdentifier;
    
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
    
    /** Getter for property officerChanges.
     * @return Value of property officerChanges.
     *
     */
    public OfficerChanges[] getOfficerChanges() {
        return this.officerChanges;
    }
    
    /** Setter for property officerChanges.
     * @param officerChanges New value of property officerChanges.
     *
     */
    public void setOfficerChanges(OfficerChanges[] officerChanges) {
        this.officerChanges = officerChanges;
    }
    
}
