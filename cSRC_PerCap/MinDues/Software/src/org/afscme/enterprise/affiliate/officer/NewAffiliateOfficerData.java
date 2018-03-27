package org.afscme.enterprise.affiliate.officer;

import java.util.Collection;

/**
 * Represents the data needed to Update all of the Affiliate's Officers. This 
 * directly maps to the Collection of Officers on the Affiliate Officer 
 * Maintenance Edit screen.
 */
public class NewAffiliateOfficerData 
{
    private boolean errors;
    private Collection theUpdateOfficerData;
    
// Getter and Setter Methods...
    
    /** Getter for property errors.
     * @return Value of property errors.
     *
     */
    public boolean isErrors() {
        return errors;
    }
    
    /** Setter for property errors.
     * @param errors New value of property errors.
     *
     */
    public void setErrors(boolean errors) {
        this.errors = errors;
    }
    
    /** Getter for property theUpdateOfficerData.
     * @return Value of property theUpdateOfficerData.
     *
     */
    public Collection getTheUpdateOfficerData() {
        return theUpdateOfficerData;
    }
    
    /** Setter for property theUpdateOfficerData.
     * @param theUpdateOfficerData New value of property theUpdateOfficerData.
     *
     */
    public void setTheUpdateOfficerData(Collection theUpdateOfficerData) {
        this.theUpdateOfficerData = theUpdateOfficerData;
    }
    
}
