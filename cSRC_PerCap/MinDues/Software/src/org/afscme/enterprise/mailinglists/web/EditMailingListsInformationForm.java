package org.afscme.enterprise.mailinglists.web;

import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import javax.servlet.http.HttpServletRequest;

/** Holds the data on the Mailing Lists Information Edit screen
 * @struts:form name="editMailingListsInformationForm"
 */
public class EditMailingListsInformationForm extends ActionForm {
    
    protected Integer pk;
    protected Integer mailingListPk;
    protected String mailingListBulkCount;
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getMailingListBulkCount() != null && !TextUtil.isEmpty(getMailingListBulkCount()) && !TextUtil.isInt(getMailingListBulkCount())) {
            errors.add("mailingListBulkCount", new ActionError("error.field.mustBeInt.generic", "Bulk Count"));
        }  else if (getMailingListBulkCount() == null || TextUtil.isEmpty(getMailingListBulkCount()) || new Integer(getMailingListBulkCount()).intValue() <= 0) {
            errors.add("mailingListBulkCount", new ActionError("error.field.required.mailingListBulkCount"));
        }
        return errors;
    }
    
    public String toString() {
        return
        "Pk=" + pk + ", " +
        "mailingListPk=" + mailingListPk + ", " +
        "mailingListBulkCount=" + mailingListBulkCount;
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


