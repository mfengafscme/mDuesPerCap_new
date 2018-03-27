package org.afscme.enterprise.reporting.specialized;

import java.io.Serializable;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;


/**
 * Represents elements on the Affiliate Officer Maintenance screen
 */
public class PreliminaryRosterAffiliate extends AffiliateIdentifier implements Serializable {
    
    private String affPk;
    boolean selected = false;
    
    public PreliminaryRosterAffiliate () {
        super();
    }
        
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.String getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.String affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property selected.
     * @return Value of property selected.
     *
     */
    public boolean isSelected() {
        return selected;
    }

    /** Getter for property selected.
     * @return Value of property selected.
     *
     */
    public boolean getSelected() {
        return selected;
    }
    
    /** Setter for property selected.
     * @param selected New value of property selected.
     *
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }    
}
