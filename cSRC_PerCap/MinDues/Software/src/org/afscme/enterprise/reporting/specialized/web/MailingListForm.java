
package org.afscme.enterprise.reporting.specialized.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:form name="mailingListForm"
 */
public class MailingListForm extends ActionForm
{

    /** Holds value of property mailingListPk. */
    private int mailingListPk;
    
    /**Holds the value of the property for filtering duplicate addresses*/
    private String filterDuplicateAddresses;

    /** Getter for property mailingListPk.
     * @return Value of property mailingListPk.
     *
     */
    public int getMailingListPk() {
        return this.mailingListPk;
    }

    /** Setter for property mailingListPk.
     * @param mailingListId New value of property mailingListId.
     *
     */
    public void setMailingListPk(int mailingListPk) {
        this.mailingListPk = mailingListPk;
    }
    
    /** Getter method for the property filterDuplicateAddresses
     * @return Returns the string value of the property should be either "true" or "false"
     */
    public String getFilterDuplicateAddresses()
    {
        return filterDuplicateAddresses;
    }
    
    /** Setter method for the filterDuplicateAddresses property
     * @param value The new string value either "true" or "false"
     */
    public void setFilterDuplicateAddresses(String value)
    {
        filterDuplicateAddresses = value;
    }
    
    /** Uses Bollean.valueOf(String) to determine if the filterDuplicateAddresses
     * property is true or false
     * @return true if filterDuplicateAddresses is set to "true"
     */    
    public boolean isFilterDuplicateAddressesEnabled()
    {
        return Boolean.valueOf(filterDuplicateAddresses).booleanValue();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
	    if (this.mailingListPk == 0) {
			errors.add("mailingListPk", new ActionError("error.field.required.mailingListPk"));
	   	}
		return errors;
    }
}
