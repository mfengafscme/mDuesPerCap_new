package org.afscme.enterprise.update.officer;


/**
 * 'viewable' data about an officer position change.
 */
public class PostionChangesResult extends PositionChanges {
    
    /**
     * Constitutional title
     */
    protected String constitutionalTitle;
    
    /**
     * Title used by the affiliate
     */
    protected String affiliateTitle;
    
    /** Getter for property affiliateTitle.
     * @return Value of property affiliateTitle.
     *
     */
    public String getAffiliateTitle() {
        return affiliateTitle;
    }
    
    /** Setter for property affiliateTitle.
     * @param affiliateTitle New value of property affiliateTitle.
     *
     */
    public void setAffiliateTitle(String affiliateTitle) {
        this.affiliateTitle = affiliateTitle;
    }
    
    /** Getter for property constitutionalTitle.
     * @return Value of property constitutionalTitle.
     *
     */
    public String getConstitutionalTitle() {
        return constitutionalTitle;
    }
    
    /** Setter for property constitutionalTitle.
     * @param constitutionalTitle New value of property constitutionalTitle.
     *
     */
    public void setConstitutionalTitle(String constitutionalTitle) {
        this.constitutionalTitle = constitutionalTitle;
    }
    
}
