package org.afscme.enterprise.reporting.specialized.web;

import org.afscme.enterprise.common.web.AffiliateSearchForm;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;



/**
 * @struts:form name="nameCountForm"
 */
public class NameCountForm extends AffiliateSearchForm
{
    
    /** Holds value of property firstName. */
    private String firstName;
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public String getFirstName() {
        return this.firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
}



