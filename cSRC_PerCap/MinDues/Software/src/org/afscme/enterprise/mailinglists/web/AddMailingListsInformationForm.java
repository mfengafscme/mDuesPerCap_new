package org.afscme.enterprise.mailinglists.web;

import java.util.Map;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;

/**
 * Form data from the Add Mailing Lists Information page.
 *
 * @struts:form name="addMailingListsInformationForm"
 */
public class AddMailingListsInformationForm extends ActionForm {
    
    private Integer pk;
    private Integer addressPk;
    private Integer mailingListPk;
    private String mailingListBulkCount;
    
    public AddMailingListsInformationForm() {
        pk = null;
        addressPk = null;
        mailingListPk = null;
        mailingListBulkCount = null;        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (this.mailingListPk == null || this.mailingListPk.intValue() == 0) {
            errors.add("mailingListPk", new ActionError("error.field.required.mailingListPk"));
        }
        if (getMailingListBulkCount() != null && !TextUtil.isEmpty(getMailingListBulkCount()) && !TextUtil.isInt(getMailingListBulkCount())) {
            errors.add("mailingListBulkCount", new ActionError("error.field.mustBeInt.generic", "Bulk Count"));            
        }
        return errors;
    }
    
    public String toString() {
        return
        "Pk=" + pk + ", " +
        "mailingListPk=" + mailingListPk + ", " +
        "addressPk=" + addressPk + ", " +
        "mailingListBulkCount=" + mailingListBulkCount;
    }
    
    /** Getter for property addressPk.
     * @return Value of property addressPk.
     *
     */
    public java.lang.Integer getAddressPk() {
        return addressPk;
    }
    
    /** Setter for property addressPk.
     * @param addressPk New value of property addressPk.
     *
     */
    public void setAddressPk(java.lang.Integer addressPk) {
        this.addressPk = addressPk;
    }
    
    /** Getter for property mailingListBulkCount.
     * @return Value of property mailingListBulkCount.
     *
     */
    public java.lang.String getMailingListBulkCount() {
        return mailingListBulkCount;
    }
    
    /** Setter for property mailingListBulkCount.
     * @param mailingListBulkCount New value of property mailingListBulkCount.
     *
     */
    public void setMailingListBulkCount(java.lang.String mailingListBulkCount) {
        this.mailingListBulkCount = mailingListBulkCount;
    }
    
    /** Getter for property mailingListPk.
     * @return Value of property mailingListPk.
     *
     */
    public java.lang.Integer getMailingListPk() {
        return mailingListPk;
    }
    
    /** Setter for property mailingListPk.
     * @param mailingListPk New value of property mailingListPk.
     *
     */
    public void setMailingListPk(java.lang.Integer mailingListPk) {
        this.mailingListPk = mailingListPk;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public java.lang.Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(java.lang.Integer pk) {
        this.pk = pk;
    }       
}
